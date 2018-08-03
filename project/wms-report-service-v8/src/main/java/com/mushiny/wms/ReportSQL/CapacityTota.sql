SELECT
  Z.NAME,
  SL.binType,
  SL.binTotal
FROM MD_ZONE Z
  LEFT JOIN
  (SELECT
     sl.ZONE_ID      AS zoneID,
     slt.STORAGETYPE AS binType,
     count(sl.ID)    AS binTotal
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
   GROUP BY sl.ZONE_ID, binType) SL
    ON Z.ID = SL.zoneID
WHERE binType <> container;


SELECT *
FROM MD_STORAGELOCATION sl
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID AND su.UNITLOAD_ID IS NOT NULL;


SELECT
  z.NAME                       AS zoneName,
  COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
FROM MD_STORAGELOCATION sl
  LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
WHERE su.UNITLOAD_ID IS NOT NULL
GROUP BY zoneName;

# 所有区域 各bin 类型的 不为空 bin的面积和商品的面积 **方法1
SELECT
  Z.NAME                                    AS zoneName,
  A.binType                                 AS binType,
  A.binNotNullTotalM3                       AS binNotNullTotalM3,
  C.itemTotalAmountM3                       AS itemTotalAmountM3,
  C.itemTotalAmountM3 / A.binNotNullTotalM3 AS utilization
FROM MD_ZONE Z
  #   所有区域 各bin类型 不为空 bin的总体积
  LEFT JOIN
  (SELECT
     zone.NAME                    AS zoneName,
     slt.STORAGETYPE              AS binType,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
   FROM MD_ZONE zone
     LEFT JOIN MD_STORAGELOCATION sl ON zone.ID = sl.ZONE_ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NOT NULL
         AND su.AMOUNT <> 0
   GROUP BY zoneName, binType) A
    ON Z.NAME = A.zoneName
  LEFT JOIN
  # 所有区域 各bin类型 商品总体积
  (SELECT
     z.NAME                     AS zoneName,
     slt.STORAGETYPE            AS binType,
     sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3
   FROM MD_ZONE z
     LEFT JOIN MD_STORAGELOCATION sl ON z.ID = sl.ZONE_ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
   WHERE su.AMOUNT <> 0
   GROUP BY zoneName, binType) C
    ON A.zoneName = C.zoneName
       AND A.binType = C.binType;

# 所有区域 各bin 类型的 不为空 bin的面积和商品的体积合计 **方法2
SELECT DISTINCT
  z.NAME                                    AS zoneName,
  slt.STORAGETYPE                           AS binType,
  B.itemTotalAmountM3 / A.binNotNullTotalM3 AS binTypeUtilization,
  D.itemTotalAmountM3 / C.binNotNullTotalM3 AS totalUtilization
FROM MD_STORAGELOCATION sl
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
  LEFT JOIN
  # 所有区域 各bin类型 不为空 bin的总体积
  (SELECT
     z.NAME                       AS zoneName,
     slt.STORAGETYPE              AS binType,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
     LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
   WHERE su.UNITLOAD_ID IS NOT NULL AND su.AMOUNT <> 0
   GROUP BY zoneName, binType) A
    ON slt.STORAGETYPE = A.binType AND z.NAME = A.zoneName
  LEFT JOIN
  (
    # 所有区域 各bin类型的  所有商品的面积合计
    SELECT
      z.NAME                     AS zoneName,
      slt.STORAGETYPE            AS binType,
      sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3
    FROM MD_STORAGELOCATION sl
      LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
      LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
      LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
    WHERE su.AMOUNT <> 0
    GROUP BY zoneName, binType) B
    ON A.zoneName = B.zoneName AND A.binType = B.binType
  # WHERE binNotNullTotalM3 IS NOT NULL
  LEFT JOIN
  # 所有区域的 不为空的bin 面积合计
  (SELECT
     z.NAME                       AS zoneName,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
     LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
   WHERE su.UNITLOAD_ID IS NOT NULL AND su.AMOUNT <> 0
   GROUP BY zoneName) C ON z.NAME = C.zoneName
  LEFT JOIN
  # 所有区域的 所有商品的面积合计
  (SELECT
     z.NAME                     AS zoneName,
     sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
     LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
   WHERE su.AMOUNT <> 0
   GROUP BY zoneName) D ON z.NAME = D.zoneName
WHERE A.binNotNullTotalM3 IS NOT NULL AND slt.STORAGETYPE <> container;

# 总计 --> 各bin 类型的 不为空 bin的面积和商品的面积合计 *
SELECT DISTINCT
  slt.NAME                                  AS binType,
  B.itemTotalAmountM3 / A.binNotNullTotalM3 AS binTypeUtilization,
  D.itemTotalAmountM3 / C.binNotNullTotalM3 AS totalUtilization
FROM MD_STORAGELOCATION sl
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
  LEFT JOIN
  # 所有区域 各bin类型 不为空 bin的总体积
  (SELECT
     slt.NAME                     AS binType,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
     LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
   WHERE su.UNITLOAD_ID IS NOT NULL AND su.AMOUNT <> 0 AND slt.STORAGETYPE = 'BIN'
   GROUP BY binType
  ) A
    ON slt.NAME = A.binType
  LEFT JOIN
  (
    # 所有区域 各bin类型的  所有商品的体积合计
    SELECT
      slt.NAME                   AS binType,
      sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3
    FROM MD_STORAGELOCATION sl
      LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
      LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
      LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
    WHERE su.AMOUNT <> 0
    GROUP BY binType
  ) B
    ON A.binType = B.binType
  # WHERE binNotNullTotalM3 IS NOT NULL
  LEFT JOIN
  # 所有区域的 不为空的bin 体积合计
  (SELECT
     slt.NAME                     AS binType,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
     LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
   WHERE su.UNITLOAD_ID IS NOT NULL AND su.AMOUNT <> 0
   GROUP BY binType) C ON slt.NAME = C.binType
  LEFT JOIN
  # 所有区域的 所有商品的体积合计
  (SELECT
     slt.NAME                   AS binType,
     sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
     LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
   WHERE su.AMOUNT <> 0
   GROUP BY binType) D ON slt.NAME = D.binType
WHERE A.binNotNullTotalM3 IS NOT NULL;

#  旧 所有区域 各bin类型 数据
SELECT DISTINCT
  z.NAME                                    AS zoneName,
  slt.NAME                                  AS binType,
  B.itemTotalAmountM3 / A.binNotNullTotalM3 AS binTypeUtilization,
  D.itemTotalAmountM3 / C.binNotNullTotalM3 AS totalUtilization
FROM MD_STORAGELOCATION sl
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
  LEFT JOIN
  # 所有区域 各bin类型 不为空 bin的总体积
  (SELECT
     z.NAME                       AS zoneName,
     slt.NAME                     AS binType,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
     LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
   WHERE su.UNITLOAD_ID IS NOT NULL AND su.AMOUNT <> 0
   GROUP BY zoneName, binType) A
    ON slt.NAME = A.binType AND z.NAME = A.zoneName
  LEFT JOIN
  (
    # 所有区域 各bin类型的  所有商品的面积合计
    SELECT
      z.NAME                     AS zoneName,
      slt.NAME                   AS binType,
      sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3
    FROM MD_STORAGELOCATION sl
      LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
      LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
      LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
    WHERE su.AMOUNT <> 0
    GROUP BY zoneName, binType) B
    ON A.zoneName = B.zoneName AND A.binType = B.binType
  LEFT JOIN
  # 所有区域的 不为空的bin 面积合计
  (SELECT
     z.NAME                       AS zoneName,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
     LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
   WHERE su.UNITLOAD_ID IS NOT NULL AND su.AMOUNT <> 0
   GROUP BY zoneName) C ON z.NAME = C.zoneName
  LEFT JOIN
  # 所有区域的 所有商品的面积合计
  (SELECT
     z.NAME                     AS zoneName,
     sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
     LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
   WHERE su.AMOUNT <> 0
   GROUP BY zoneName) D ON z.NAME = D.zoneName
WHERE A.binNotNullTotalM3 IS NOT NULL AND slt.STORAGETYPE = 'BIN';

#  更新 所有区域 各bin类型 数据
SELECT DISTINCT
  z.NAME                                    AS zoneName,
  slt.NAME                                  AS binType,
  B.itemTotalAmountM3 / A.binNotNullTotalM3 AS binTypeUtilization,
  D.itemTotalAmountM3 / C.binNotNullTotalM3 AS totalUtilization
FROM MD_ZONE z
  LEFT JOIN MD_STORAGELOCATION sl ON z.ID = sl.ZONE_ID
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN
  # 所有区域 各bin类型 不为空 bin的总体积
  (SELECT
     z.NAME                       AS zoneName,
     slt.NAME                     AS binType,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
   FROM MD_ZONE z
     LEFT JOIN MD_STORAGELOCATION sl ON z.ID = sl.ZONE_ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN (SELECT DISTINCT su.UNITLOAD_ID
                FROM INV_STOCKUNIT su
                WHERE su.AMOUNT > 0
                      AND su.ENTITY_LOCK = 0) su
       ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NOT NULL
         AND ul.ENTITY_LOCK = 0
         AND slt.STORAGETYPE = 'BIN'
   GROUP BY zoneName, binType) A
    ON z.NAME = A.zoneName
       AND slt.NAME = A.binType
  LEFT JOIN
  (
    # 所有区域 各bin类型的  所有商品的面积合计
    SELECT
      z.NAME                     AS zoneName,
      slt.NAME                   AS binType,
      sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3
    FROM MD_ZONE z
      LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID
      LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
      LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
    WHERE su.AMOUNT <> 0
    GROUP BY zoneName, binType) B
    ON A.zoneName = B.zoneName
       AND A.binType = B.binType
  LEFT JOIN
  # 所有区域的 不为空的bin 面积合计
  (SELECT
     z.NAME                       AS zoneName,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
   FROM MD_ZONE z
     LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN (SELECT DISTINCT su.UNITLOAD_ID
                FROM INV_STOCKUNIT su
                WHERE su.AMOUNT > 0
                      AND su.ENTITY_LOCK = 0) su
       ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NOT NULL
         AND ul.ENTITY_LOCK = 0
         AND slt.STORAGETYPE = 'BIN'
   GROUP BY zoneName) C ON z.NAME = C.zoneName
  LEFT JOIN
  # 所有区域的 所有商品的面积合计
  (SELECT
     z.NAME                     AS zoneName,
     sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3
   FROM MD_ZONE z
     LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
   WHERE su.AMOUNT <> 0 AND ul.ENTITY_LOCK = 0
   GROUP BY zoneName) D ON z.NAME = D.zoneName
WHERE A.binNotNullTotalM3 IS NOT NULL
      AND slt.STORAGETYPE = 'BIN';

#  旧   总计 --> 各bin 类型的 合计 *
SELECT DISTINCT
  slt.NAME                                  AS binType,
  B.itemTotalAmountM3 / A.binNotNullTotalM3 AS binTypeUtilization,
  D.itemTotalAmountM3 / C.binNotNullTotalM3 AS totalUtilization
FROM MD_STORAGELOCATION sl
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
  LEFT JOIN
  # 所有区域 各bin类型 不为空 bin的总体积
  (SELECT
     slt.NAME                     AS binType,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
     LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
   WHERE su.UNITLOAD_ID IS NOT NULL AND su.AMOUNT <> 0
   GROUP BY binType
  ) A
    ON slt.NAME = A.binType
  LEFT JOIN
  (
    # 所有区域 各bin类型的  所有商品的面积合计
    SELECT
      slt.NAME                   AS binType,
      sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3
    FROM MD_STORAGELOCATION sl
      LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
      LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
      LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
    WHERE su.AMOUNT <> 0
    GROUP BY binType
  ) B
    ON A.binType = B.binType
  LEFT JOIN
  # 所有区域的 不为空的bin 面积合计
  (SELECT
     slt.NAME                     AS binType,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
     LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
   WHERE su.UNITLOAD_ID IS NOT NULL AND su.AMOUNT <> 0
   GROUP BY binType) C ON slt.NAME = C.binType
  LEFT JOIN
  # 所有区域的 所有商品的面积合计
  (SELECT
     slt.NAME                   AS binType,
     sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
     LEFT JOIN MD_ZONE z ON sl.ZONE_ID = z.ID
   WHERE su.AMOUNT <> 0
   GROUP BY binType) D ON slt.NAME = D.binType
WHERE A.binNotNullTotalM3 IS NOT NULL
      AND slt.STORAGETYPE = 'BIN'
ORDER BY binType;

#  新   总计 --> 各bin 类型的 合计 *
SELECT DISTINCT
  slt.NAME                                  AS binType,
  B.itemTotalAmountM3 / A.binNotNullTotalM3 AS binTypeUtilization
FROM MD_ZONE z
  LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN
  # 所有区域 各bin类型 不为空 bin的总体积
  (SELECT
     slt.NAME                     AS binType,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
   FROM MD_ZONE z
     LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN (SELECT DISTINCT su.UNITLOAD_ID
                FROM INV_STOCKUNIT su
                WHERE su.AMOUNT > 0
                      AND su.ENTITY_LOCK = 0) su
       ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NOT NULL
         AND ul.ENTITY_LOCK = 0
         AND slt.STORAGETYPE = 'BIN'
   GROUP BY binType
  ) A
    ON slt.NAME = A.binType
  LEFT JOIN
  (
    # 所有区域 各bin类型的  所有商品的面积合计
    SELECT
      slt.NAME                   AS binType,
      sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3
    FROM MD_ZONE z
      LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID
      LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
      LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
    WHERE su.AMOUNT <> 0
          AND ul.ENTITY_LOCK = 0
          AND slt.STORAGETYPE = 'BIN'
    GROUP BY binType
  ) B
    ON A.binType = B.binType
WHERE A.binNotNullTotalM3 IS NOT NULL
      AND slt.STORAGETYPE = 'BIN'
ORDER BY slt.NAME;

#   新 合计的TOTAL  所有区域的 商品面积合计/不为空的bin 面积合计
SELECT D.itemTotalAmountM3 / C.binNotNullTotalM3 AS totalUtilization
FROM
  (SELECT COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3
   FROM MD_ZONE z
     LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN (SELECT DISTINCT su.UNITLOAD_ID
                FROM INV_STOCKUNIT su
                WHERE su.AMOUNT > 0
                      AND su.ENTITY_LOCK = 0) su
       ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NOT NULL
         AND ul.ENTITY_LOCK = 0
         AND slt.STORAGETYPE = 'BIN'
  ) C,
  # 所有区域的 所有商品的面积合计
  (SELECT sum(su.AMOUNT * it.VOLUME) AS itemTotalAmountM3
   FROM MD_ZONE z
     LEFT JOIN MD_STORAGELOCATION sl ON sl.ZONE_ID = z.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA it ON su.ITEMDATA_ID = it.ID
   WHERE su.AMOUNT <> 0 AND slt.STORAGETYPE = 'BIN'
  ) D;


