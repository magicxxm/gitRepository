# 获取Pod 名字，类型，当前位置，Pod中商品总数量
SELECT
  pod.NAME                    AS podName,
  podt.NAME                   AS podType,
  pod.PLACEMARK               AS location,
  coalesce(sum(su.AMOUNT), 0) AS itemTotalAmount
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
GROUP BY podName, podType, location;

# 获取Pod 名字，类型，当前位置，itemNo数量
SELECT
  pod.NAME           AS podName,
  podt.NAME          AS podType,
  pod.PLACEMARK      AS location,
  count(imd.ITEM_NO) AS itemNoUnit
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
  LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
GROUP BY podName, podType, location, imd.ITEM_NO;

# 获取Pod 名字，类型，当前位置，skuNo数量
SELECT
  pod.NAME          AS podName,
  podt.NAME         AS podType,
  pod.PLACEMARK     AS location,
  count(imd.SKU_NO) AS skuNoUnit
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
  LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
GROUP BY podName, podType, location, imd.SKU_NO;

# 获取Pod 名字，类型，当前位置，Pod中->所有商品总体积 + 个数
SELECT
  pod.NAME                                 AS podName,
  podt.NAME                                AS podType,
  pod.PLACEMARK                            AS location,
  coalesce(sum(su.AMOUNT * imd.VOLUME), 0) AS itemTotalM3,
  coalesce(sum(su.AMOUNT), 0)              AS itemTotalAmount
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
  LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
GROUP BY podName, podType, location;

# 获取Pod 名字，类型，当前位置，Pod中->所有货位的总体积+个数
SELECT
  pod.NAME                     AS podName,
  podt.NAME                    AS podType,
  pod.PLACEMARK                AS location,
  coalesce(sum(slt.VOLUME), 0) AS binTotalM3,
  count(sl.ID)                 AS binTotal
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
GROUP BY podName, podType, location;

# 获取Pod 名字，类型，当前位置，Pod中->所有已使用的货位总体积+个数
SELECT
  pod.NAME                     AS podName,
  podt.NAME                    AS podType,
  pod.PLACEMARK                AS location,
  coalesce(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
  count(sl.ID)                 AS binNotNullTotal
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
WHERE su.UNITLOAD_ID IS NOT NULL
GROUP BY podName, podType, location;

# 获取Pod 名字，类型，当前位置，Pod中->所有未使用的货位总体积+个数
SELECT
  pod.NAME                     AS podName,
  podt.NAME                    AS podType,
  pod.PLACEMARK                AS location,
  coalesce(sum(slt.VOLUME), 0) AS binNullTotalM3,
  count(sl.ID)                 AS binNullTotal
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
WHERE su.UNITLOAD_ID IS NULL
GROUP BY podName, podType, location;

# 获取Pod 名字，类型，当前位置，Pod中-> 所有 未使用的 BufferBin的数量
SELECT
  pod.NAME      AS podName,
  podt.NAME     AS podType,
  pod.PLACEMARK AS location,
  count(sl.ID)  AS bufferBinNullTotal
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
  LEFT JOIN MD_AREA ar ON sl.AREA_ID = ar.ID
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
WHERE su.UNITLOAD_ID IS NULL
      AND ar.USE_FOR_STORAGE = TRUE
GROUP BY podName, podType, location;


/*以上SQL 查询字段 合并*/

SELECT
  A.podName,
  A.podType,
  A.location,
  A.itemTotalAmount,
  C.skuNoUnit,
  B.itemNoUnit,
  A.itemTotalM3,
  D.binTotalM3,
  E.binNotNullTotalM3,
  F.binNullTotalM3,
  D.binTotal,
  E.binNotNullTotal,
  F.binNullTotal,
  G.bufferBinNullTotal
FROM
  # 获取Pod 名字，类型，当前位置，Pod中->所有商品总体积 + 个数
  (SELECT
     pod.NAME                                 AS podName,
     podt.NAME                                AS podType,
     pod.PLACEMARK                            AS location,
     coalesce(sum(su.AMOUNT * imd.VOLUME), 0) AS itemTotalM3,
     coalesce(sum(su.AMOUNT), 0)              AS itemTotalAmount
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   GROUP BY podName, podType, location) A
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，itemNo数量
  (SELECT
     pod.NAME           AS podName,
     podt.NAME          AS podType,
     pod.PLACEMARK      AS location,
     count(imd.ITEM_NO) AS itemNoUnit
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   GROUP BY podName, podType, location, imd.ITEM_NO) B
    ON A.podName = B.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，skuNo数量
  (SELECT
     pod.NAME          AS podName,
     podt.NAME         AS podType,
     pod.PLACEMARK     AS location,
     count(imd.SKU_NO) AS skuNoUnit
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   GROUP BY podName, podType, location, imd.SKU_NO) C
    ON A.podName = C.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->所有货位的总体积+个数
  (SELECT
     pod.NAME                     AS podName,
     podt.NAME                    AS podType,
     pod.PLACEMARK                AS location,
     coalesce(sum(slt.VOLUME), 0) AS binTotalM3,
     count(sl.ID)                 AS binTotal
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
   GROUP BY podName, podType, location) D
    ON A.podName = D.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->所有已使用的货位总体积+个数
  (SELECT
     pod.NAME                     AS podName,
     podt.NAME                    AS podType,
     pod.PLACEMARK                AS location,
     coalesce(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
     count(sl.ID)                 AS binNotNullTotal
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NOT NULL
   GROUP BY podName, podType, location) E
    ON A.podName = E.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->所有未使用的货位总体积+个数
  (SELECT
     pod.NAME                     AS podName,
     podt.NAME                    AS podType,
     pod.PLACEMARK                AS location,
     coalesce(sum(slt.VOLUME), 0) AS binNullTotalM3,
     count(sl.ID)                 AS binNullTotal
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NULL
   GROUP BY podName, podType, location) F
    ON A.podName = F.podName
  LEFT JOIN

  # 获取Pod 名字，类型，当前位置，Pod中-> 所有 未使用的 BufferBin的数量
  (SELECT
     pod.NAME      AS podName,
     podt.NAME     AS podType,
     pod.PLACEMARK AS location,
     count(sl.ID)  AS bufferBinNullTotal
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN MD_AREA ar ON sl.AREA_ID = ar.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NULL
         AND ar.USE_FOR_STORAGE = TRUE
   GROUP BY podName, podType, location) G
    ON A.podName = G.podName;

/****************************************************/

# 所有 pod（小车） /podType(小车类型) /podLocation (小车位置)
SELECT DISTINCT
  pod.NAME      AS podName,
  podt.NAME     AS podType,
  pod.PLACEMARK AS location
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID
WHERE 1 = 1;

# 某个pod 下->所有面
SELECT DISTINCT
  pod.NAME      AS podName,
  podt.NAME     AS podType,
  pod.PLACEMARK AS location,
  potp.FACE     AS face
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID
WHERE 1 = 1 AND pod.NAME = '00000001';

# 某个pod下 -> 某个面下 -> 所有bin(货位)
SELECT DISTINCT
  pod.NAME      AS podName,
  podt.NAME     AS podType,
  pod.PLACEMARK AS location,
  potp.FACE     AS face,
  sl.NAME       AS slName
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID
  LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
WHERE 1 = 1 AND pod.NAME = '00000001';

/****************************************************************/
# 所有 pod 数据查询
SELECT
  P.podName                         AS podName,
  P.podType                         AS podType,
  P.location                        AS location,
  coalesce(A.itemTotalAmount, 0)    AS itemTotalAmount,
  coalesce(C.skuNoUnit, 0)          AS skuNoUnit,
  coalesce(B.itemNoUnit, 0)         AS itemNoUnit,
  coalesce(A.itemTotalM3, 0)        AS itemTotalM3,
  coalesce(D.binTotalM3, 0)         AS binTotalM3,
  coalesce(E.binNotNullTotalM3, 0)  AS binNotNullTotalM3,
  coalesce(F.binNullTotalM3, 0)     AS binNullTotalM3,
  coalesce(D.binTotal, 0)           AS binTotal,
  coalesce(E.binNotNullTotal, 0)    AS binNotNullTotal,
  coalesce(F.binNullTotal, 0)       AS binNullTotal,
  coalesce(G.bufferBinNullTotal, 0) AS bufferBinNullTotal
FROM
  (SELECT DISTINCT
     pod.NAME      AS podName,
     podt.NAME     AS podType,
     pod.PLACEMARK AS location
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID
   WHERE 1 = 1
   ORDER BY podName, podType, location) P

  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->所有商品总体积 + 个数
  (
    SELECT
      pod.NAME                                 AS podName,
      podt.NAME                                AS podType,
      pod.PLACEMARK                            AS location,
      coalesce(sum(su.AMOUNT * imd.VOLUME), 0) AS itemTotalM3,
      coalesce(sum(su.AMOUNT), 0)              AS itemTotalAmount
    FROM MD_POD pod
      LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
      LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
      LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
    WHERE su.AMOUNT <> 0
    GROUP BY podName, podType, location) A
    ON P.podName = A.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，itemNo数量
  (
    SELECT
      pod.NAME                    AS podName,
      podt.NAME                   AS podType,
      pod.PLACEMARK               AS location,
      count(DISTINCT imd.ITEM_NO) AS itemNoUnit
    FROM MD_POD pod
      LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
      LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
      LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
    WHERE su.AMOUNT <> 0
    GROUP BY podName, podType, location
  ) B
    ON P.podName = B.podName

  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，skuNo数量
  (SELECT
     pod.NAME                   AS podName,
     podt.NAME                  AS podType,
     pod.PLACEMARK              AS location,
     COUNT(DISTINCT imd.SKU_NO) AS skuNoUnit
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   WHERE su.AMOUNT <> 0
   GROUP BY podName, podType, location) C
    ON P.podName = C.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->所有货位的总体积+个数
  (SELECT
     pod.NAME                     AS podName,
     podt.NAME                    AS podType,
     pod.PLACEMARK                AS location,
     COALESCE(sum(slt.VOLUME), 0) AS binTotalM3,
     COUNT(sl.ID)                 AS binTotal
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
   GROUP BY podName, podType, location) D
    ON P.podName = D.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->所有已使用的货位总体积+个数
  (SELECT
     pod.NAME                     AS podName,
     podt.NAME                    AS podType,
     pod.PLACEMARK                AS location,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
     COUNT(DISTINCT sl.ID)        AS binNotNullTotal
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NOT NULL AND ul.ENTITY_LOCK = 0 AND su.AMOUNT > 0
   GROUP BY podName, podType, location) E
    ON P.podName = E.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->所有未使用的货位总体积+个数
  (SELECT
     pod.NAME                     AS podName,
     podt.NAME                    AS podType,
     pod.PLACEMARK                AS location,
     COALESCE(sum(slt.VOLUME), 0) AS binNullTotalM3,
     COUNT(sl.ID)                 AS binNullTotal
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NULL
   GROUP BY podName, podType, location) F
    ON P.podName = F.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中-> 所有 未使用的 BufferBin的数量
  (SELECT
     pod.NAME      AS podName,
     podt.NAME     AS podType,
     pod.PLACEMARK AS location,
     COUNT(sl.ID)  AS bufferBinNullTotal
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN MD_AREA ar ON sl.AREA_ID = ar.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NULL
         AND ar.USE_FOR_STORAGE = TRUE
   GROUP BY podName, podType, location) G
    ON P.podName = G.podName;

# 修改
/****************************************************************/
# 所有 pod 数据查询
SELECT
  P.podName                         AS podName,
  P.podType                         AS podType,
  P.location                        AS location,
  coalesce(A.itemTotalAmount, 0)    AS itemTotalAmount,
  coalesce(C.skuNoUnit, 0)          AS skuNoUnit,
  coalesce(B.itemNoUnit, 0)         AS itemNoUnit,
  coalesce(A.itemTotalM3, 0)        AS itemTotalM3,
  coalesce(D.binTotalM3, 0)         AS binTotalM3,
  coalesce(E.binNotNullTotalM3, 0)  AS binNotNullTotalM3,
  coalesce(F.binNullTotalM3, 0)     AS binNullTotalM3,
  coalesce(D.binTotal, 0)           AS binTotal,
  coalesce(E.binNotNullTotal, 0)    AS binNotNullTotal,
  coalesce(F.binNullTotal, 0)       AS binNullTotal,
  coalesce(G.bufferBinNullTotal, 0) AS bufferBinNullTotal
FROM
  (SELECT DISTINCT
     pod.NAME      AS podName,
     podt.NAME     AS podType,
     pod.PLACEMARK AS location
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID
   WHERE 1 = 1
   ORDER BY podName, podType, location) P

  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->所有商品总体积 + 个数
  (
    SELECT
      pod.NAME                                 AS podName,
      podt.NAME                                AS podType,
      pod.PLACEMARK                            AS location,
      coalesce(sum(su.AMOUNT * imd.VOLUME), 0) AS itemTotalM3,
      coalesce(sum(su.AMOUNT), 0)              AS itemTotalAmount
    FROM MD_POD pod
      LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
      LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
      LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
    WHERE su.AMOUNT <> 0
    GROUP BY podName, podType, location) A
    ON P.podName = A.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，itemNo数量
  (
    SELECT
      pod.NAME                    AS podName,
      podt.NAME                   AS podType,
      pod.PLACEMARK               AS location,
      count(DISTINCT imd.ITEM_NO) AS itemNoUnit
    FROM MD_POD pod
      LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
      LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
      LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
    WHERE su.AMOUNT <> 0
    GROUP BY podName, podType, location
  ) B
    ON P.podName = B.podName

  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，skuNo数量
  (SELECT
     pod.NAME                   AS podName,
     podt.NAME                  AS podType,
     pod.PLACEMARK              AS location,
     COUNT(DISTINCT imd.SKU_NO) AS skuNoUnit
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   WHERE su.AMOUNT <> 0
   GROUP BY podName, podType, location) C
    ON P.podName = C.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->所有货位的总体积+个数
  (SELECT
     pod.NAME                     AS podName,
     podt.NAME                    AS podType,
     pod.PLACEMARK                AS location,
     COALESCE(sum(slt.VOLUME), 0) AS binTotalM3,
     COUNT(sl.ID)                 AS binTotal
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
   GROUP BY podName, podType, location) D
    ON P.podName = D.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->所有已使用的货位总体积+个数
  (
    SELECT
      pod.NAME                     AS podName,
      podt.NAME                    AS podType,
      pod.PLACEMARK                AS location,
      COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
      COUNT(DISTINCT sl.ID)        AS binNotNullTotal
    FROM MD_POD pod
      LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
      LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
      LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
      LEFT JOIN (SELECT DISTINCT su.UNITLOAD_ID
                 FROM INV_STOCKUNIT su
                 WHERE su.AMOUNT > 0
                       AND su.ENTITY_LOCK = 0) su
        ON ul.ID = su.UNITLOAD_ID
    WHERE su.UNITLOAD_ID IS NOT NULL
          AND ul.ENTITY_LOCK = 0
    GROUP BY podName, podType, location
  ) E
    ON P.podName = E.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->所有未使用的货位总体积+个数
  (SELECT
     pod.NAME                     AS podName,
     podt.NAME                    AS podType,
     pod.PLACEMARK                AS location,
     COALESCE(sum(slt.VOLUME), 0) AS binNullTotalM3,
     COUNT(DISTINCT sl.ID)        AS binNullTotal
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NULL
   GROUP BY podName, podType, location) F
    ON P.podName = F.podName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中-> 所有 未使用的 BufferBin的数量
  (SELECT
     pod.NAME      AS podName,
     podt.NAME     AS podType,
     pod.PLACEMARK AS location,
     COUNT(sl.ID)  AS bufferBinNullTotal
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
     LEFT JOIN MD_AREA ar ON sl.AREA_ID = ar.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NULL
         AND ar.USE_FOR_STORAGE = TRUE
   GROUP BY podName, podType, location) G
    ON P.podName = G.podName
ORDER BY podName;


SELECT
  pod.NAME                     AS podName,
  podt.NAME                    AS podType,
  pod.PLACEMARK                AS location,
  COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
  COUNT(sl.ID)                 AS binNotNullTotal
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN (SELECT DISTINCT su.UNITLOAD_ID
             FROM INV_STOCKUNIT su
             WHERE su.AMOUNT > 0
                   AND su.ENTITY_LOCK = 0) su
    ON ul.ID = su.UNITLOAD_ID
WHERE su.UNITLOAD_ID IS NOT NULL
      AND ul.ENTITY_LOCK = 0
GROUP BY podName, podType, location;


SELECT
  pod.NAME                     AS podName,
  podt.NAME                    AS podType,
  pod.PLACEMARK                AS location,
  COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
  COUNT(DISTINCT sl.ID)        AS binNotNullTotal
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
WHERE su.UNITLOAD_ID IS NOT NULL
      AND ul.ENTITY_LOCK = 0
      AND su.AMOUNT > 0
GROUP BY podName, podType, location;


SELECT SL.NAME
FROM MD_STORAGELOCATION SL
WHERE SL.NAME LIKE 'P0000025%';


