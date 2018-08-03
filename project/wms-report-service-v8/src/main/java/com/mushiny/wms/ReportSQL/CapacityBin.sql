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
WHERE 1 = 1 AND pod.NAME = 'P0000001';

# 某个pod下 -> 某个面下 -> 所有bin(货位)
SELECT DISTINCT
  sl.NAME       AS binName,
  slt.NAME      AS binType,
  pod.PLACEMARK AS location
FROM
  MD_STORAGELOCATION sl
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
WHERE sl.NAME LIKE 'P0000012A%';

SELECT DISTINCT
  sl.NAME           AS binName,
  slt.STORAGETYPE   AS binType,
  pod.PLACEMARK     AS location,
  A.itemTotalM3     AS itemTotalM3,
  A.itemTotalAmount AS itemTotalAmount
FROM
  MD_STORAGELOCATION sl
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
  LEFT JOIN
  (SELECT
     sl.POD_ID,
     sl.FACE                      AS face,
     coalesce(sum(imd.VOLUME), 0) AS itemTotalM3,
     coalesce(sum(su.AMOUNT), 0)  AS itemTotalAmount
   FROM MD_STORAGELOCATION sl
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   GROUP BY POD_ID, face
  ) A ON sl.POD_ID = A.POD_ID AND sl.FACE = A.face
WHERE sl.NAME LIKE 'P0000001A%';


/*************************************************/

# 某个pod下 -> 某个面下 -> 所有bin(货位) 数据
SELECT
  P.binName                                     AS binName,
  P.binType                                     AS binType,
  P.location                                    AS location,
  A.itemTotalAmount                             AS itemTotalAmount,
  C.skuNoUnit                                   AS skuNoUnit,
  B.itemNoUnit                                  AS itemNoUnit,
  A.itemTotalM3 / 1000000000                    AS itemTotalM3,
  D.binTotalM3 / 1000000000                     AS binTotalM3,
  coalesce(E.binNotNullTotalM3 / 1000000000, 0) AS binNotNullTotalM3,
  F.binNullTotalM3 / 1000000000                 AS binNullTotalM3,
  D.binTotal                                    AS binTotal,
  coalesce(E.binNotNullTotal, 0)                AS binNotNullTotal,
  F.binNullTotal                                AS binNullTotal,
  coalesce(G.bufferBinNullTotal, 0)             AS bufferBinNullTotal
FROM
  # 某个pod下 -> 某个面下 -> 所有bin(货位)
  (SELECT DISTINCT
     pod.NAME        AS podName,
     sl.FACE         AS face,
     sl.NAME         AS binName,
     slt.NAME        AS binType,
     slt.STORAGETYPE AS bin,
     pod.PLACEMARK   AS location
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
   WHERE slt.STORAGETYPE = 'BIN'
  ) P
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中-> 某个面下 商品总体积 + 个数
  (SELECT
     sl.NAME                                  AS binName,
     COALESCE(sum(imd.VOLUME * su.AMOUNT), 0) AS itemTotalM3,
     COALESCE(sum(su.AMOUNT), 0)              AS itemTotalAmount
   FROM MD_STORAGELOCATION sl
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   GROUP BY binName) A
    ON P.binName = A.binName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置， 某个面下 itemNo数量
  (SELECT
     sl.NAME                     AS binName,
     COUNT(DISTINCT imd.ITEM_NO) AS itemNoUnit
   FROM MD_STORAGELOCATION sl
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   GROUP BY binName) B
    ON P.binName = B.binName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置， 某个面下 skuNo数量
  (SELECT
     sl.NAME                    AS binName,
     COUNT(DISTINCT imd.SKU_NO) AS skuNoUnit
   FROM MD_STORAGELOCATION sl
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   GROUP BY binName) C
    ON P.binName = C.binName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中-> 某个面 货位总体积+个数
  (SELECT
     sl.NAME                      AS binName,
     COALESCE(sum(slt.VOLUME), 0) AS binTotalM3,
     COUNT(sl.ID)                 AS binTotal
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
   GROUP BY binName) D
    ON P.binName = D.binName
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->某个面 已使用的货位总体积+个数
  (SELECT
     sl.NAME                      AS binName,
     COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
     COUNT(sl.ID)                 AS binNotNullTotal
   FROM
     MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN (SELECT DISTINCT su.UNITLOAD_ID
                FROM INV_STOCKUNIT su
                WHERE su.AMOUNT > 0
                      AND su.ENTITY_LOCK = 0) su
       ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NOT NULL
         AND ul.ENTITY_LOCK = 0
   GROUP BY binName

    #     SELECT
    #      sl.NAME                      AS binName,
    #      COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
    #      COUNT(sl.ID)                 AS binNotNullTotal
    #    FROM MD_STORAGELOCATION sl
    #      LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
    #      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
    #      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
    #    WHERE su.UNITLOAD_ID IS NOT NULL
    #    GROUP BY binName
  ) E ON P.binName = E.binName

  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->某个面 未使用的货位总体积+个数
  (SELECT
     sl.NAME                      AS binName,
     COALESCE(sum(slt.VOLUME), 0) AS binNullTotalM3,
     COUNT(sl.ID)                 AS binNullTotal
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NULL
   GROUP BY binName) F
    ON P.binName = F.binName

  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中-> 某个面 未使用的 BufferBin的数量
  (SELECT
     sl.NAME      AS binName,
     COUNT(sl.ID) AS bufferBinNullTotal
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN MD_AREA ar ON sl.AREA_ID = ar.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NULL
         AND ar.USE_FOR_STORAGE = TRUE
   GROUP BY binName) G
    ON P.binName = G.binName
WHERE 1 = 1 AND P.binName LIKE 'P0000009AA02'
ORDER BY binName;


SELECT
  sl.NAME AS binName,
  ul.ID
#   COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
#   COUNT(sl.ID)                 AS binNotNullTotal
FROM MD_STORAGELOCATION sl
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
WHERE su.UNITLOAD_ID IS NOT NULL
GROUP BY binName;

# 获取Pod 名字，类型，当前位置，Pod中->某个面 已使用的货位总体积+个数
SELECT
  sl.NAME                      AS binName,
  COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
  COUNT(DISTINCT sl.ID)        AS binNotNullTotal
FROM MD_STORAGELOCATION sl
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
WHERE ul.ENTITY_LOCK = 0 AND su.AMOUNT > 0
GROUP BY binName;


SELECT
  sl.NAME                      AS binName,
  COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
  COUNT(sl.ID)                 AS binNotNullTotal
FROM MD_STORAGELOCATION sl
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
WHERE su.UNITLOAD_ID IS NOT NULL
GROUP BY binName;


SELECT
  sl.NAME                      AS binName,
  COALESCE(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
  COUNT(sl.ID)                 AS binNotNullTotal
FROM
  MD_STORAGELOCATION sl
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN (SELECT DISTINCT su.UNITLOAD_ID
             FROM INV_STOCKUNIT su
             WHERE su.AMOUNT > 0
                   AND su.ENTITY_LOCK = 0) su
    ON ul.ID = su.UNITLOAD_ID
WHERE su.UNITLOAD_ID IS NOT NULL
      AND ul.ENTITY_LOCK = 0
GROUP BY binName;

SELECT
  sl.NAME AS binName,
  imd.NAME,
  su.AMOUNT,
  imd.VOLUME,
  imd.ID
#   COALESCE(sum(imd.VOLUME * su.AMOUNT), 0) AS itemTotalM3,
#   COALESCE(sum(su.AMOUNT), 0)              AS itemTotalAmount
FROM MD_STORAGELOCATION sl
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
  LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
WHERE sl.NAME = 'P0000051AA01';
GROUP BY binName;