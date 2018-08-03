/****************************************************************/

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

# 获取Pod 名字，类型，当前位置，Pod中-> 所有面 商品总体积 + 个数
SELECT DISTINCT
  pod.NAME          AS podName,
  podt.NAME         AS podType,
  pod.PLACEMARK     AS location,
  potp.FACE         AS face,
  A.itemTotalM3     AS itemTotalM3,
  A.itemTotalAmount AS itemTotalAmount
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID
  LEFT JOIN
  (SELECT
     pod.NAME                     AS name,
     sl.FACE                      AS face,
     coalesce(sum(imd.VOLUME), 0) AS itemTotalM3,
     coalesce(sum(su.AMOUNT), 0)  AS itemTotalAmount
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   GROUP BY sl.POD_ID, sl.FACE) A
    ON pod.ID = A.name AND potp.FACE = A.face;

# 获取Pod 名字，类型，当前位置，itemNo数量
SELECT DISTINCT
  pod.NAME      AS podName,
  podt.NAME     AS podType,
  pod.PLACEMARK AS location,
  potp.FACE     AS face,
  B.itemNoUnit  AS itemNoUnit
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID
  LEFT JOIN
  (SELECT
     pod.NAME           AS podName,
     sl.FACE            AS face,
     count(imd.ITEM_NO) AS itemNoUnit
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   GROUP BY podName, face) B
    ON pod.NAME = B.podName AND potp.FACE = B.face;

# 获取Pod 名字，类型，当前位置， 所有面 skuNo数量
SELECT
  DISTINCT
  pod.NAME      AS podName,
  podt.NAME     AS podType,
  pod.PLACEMARK AS location,
  potp.FACE     AS face,
  C.skuNoUnit   AS skuNoUnit
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID
  LEFT JOIN
  (SELECT
     pod.NAME          AS podName,
     sl.FACE           AS face,
     count(imd.SKU_NO) AS skuNoUnit
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   GROUP BY podName, face) C
    ON pod.NAME = C.podName AND potp.FACE = C.face;

# 获取Pod 名字，类型，当前位置，Pod中-> 所有面 货位总体积+个数
SELECT DISTINCT
  pod.NAME      AS podName,
  podt.NAME     AS podType,
  pod.PLACEMARK AS location,
  potp.FACE     AS face,
  D.binTotalM3  AS binTotalM3,
  D.binTotal    AS binTotal
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID
  LEFT JOIN
  (SELECT
     pod.NAME                     AS name,
     sl.FACE                      AS face,
     coalesce(sum(slt.VOLUME), 0) AS binTotalM3,
     count(sl.ID)                 AS binTotal
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
   GROUP BY name, face
  ) D ON pod.NAME = D.name AND potp.FACE = D.face
ORDER BY podName, face;
# 获取Pod 名字，类型，当前位置，Pod中-> 所有面 已使用的货位总体积+个数
SELECT DISTINCT
  pod.NAME            AS podName,
  podt.NAME           AS podType,
  pod.PLACEMARK       AS location,
  potp.FACE           AS face,
  E.binNotNullTotalM3 AS binNotNullTotalM3,
  E.binNotNullTotal   AS binNotNullTotal
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID
  LEFT JOIN
  (SELECT
     pod.NAME                     AS podName,
     sl.FACE                      AS face,
     coalesce(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
     count(sl.ID)                 AS binNotNullTotal
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NOT NULL
   GROUP BY podName, face) E
    ON pod.NAME = E.podName AND potp.FACE = E.face;

# 获取Pod 名字，类型，当前位置，Pod中-> 所有面 未使用的货位总体积+个数
SELECT
  DISTINCT
  pod.NAME         AS podName,
  podt.NAME        AS podType,
  pod.PLACEMARK    AS location,
  potp.FACE        AS face,
  F.binNullTotalM3 AS binNullTotalM3,
  F.binNullTotal   AS binNullTotal
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID
  LEFT JOIN
  (SELECT
     pod.NAME                     AS podName,
     sl.FACE                      AS face,
     coalesce(sum(slt.VOLUME), 0) AS binNullTotalM3,
     count(sl.ID)                 AS binNullTotal
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NULL
   GROUP BY podName, face) F
    ON pod.NAME = F.podName AND potp.FACE = F.face;

# 获取Pod 名字，类型，当前位置，Pod中-> 所有面 未使用的 BufferBin的数量
SELECT
  pod.NAME                          AS podName,
  podt.NAME                         AS podType,
  pod.PLACEMARK                     AS location,
  potp.FACE                         AS face,
  coalesce(G.bufferBinNullTotal, 0) AS bufferBinNullTotal
FROM MD_POD pod
  LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
  LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID
  LEFT JOIN
  (SELECT
     pod.NAME     AS podName,
     sl.FACE      AS face,
     count(sl.ID) AS bufferBinNullTotal
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN MD_AREA ar ON sl.AREA_ID = ar.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NULL
         AND ar.USE_FOR_STORAGE = TRUE
   GROUP BY podName, face) G
    ON pod.NAME = G.podName AND potp.FACE = G.face;

/*************************************************/

# 所有 pod 下 所有面 数据查询
SELECT
  concat(P.podName, P.face)                     AS podName,
  P.podType                                     AS podType,
  P.location                                    AS location,
  P.face                                        AS face,
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
  (SELECT DISTINCT
     pod.NAME      AS podName,
     podt.NAME     AS podType,
     pod.PLACEMARK AS location,
     potp.FACE     AS face
   FROM MD_POD pod
     LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID
     LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID
  ) P
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中-> 所有面 商品总体积 + 个数
  (SELECT
     pod.NAME                                 AS podName,
     sl.FACE                                  AS face,
     coalesce(sum(su.AMOUNT * imd.VOLUME), 0) AS itemTotalM3,
     coalesce(sum(su.AMOUNT), 0)              AS itemTotalAmount
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   GROUP BY podName, face) A
    ON P.podName = A.podName AND P.face = A.face
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置， 所有面 itemNo数量
  (SELECT
     pod.NAME                    AS podName,
     sl.FACE                     AS face,
     count(DISTINCT imd.ITEM_NO) AS itemNoUnit
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   GROUP BY podName, face) B
    ON P.podName = B.podName AND P.face = B.face
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置， 所有面 skuNo数量
  (SELECT
     pod.NAME                   AS podName,
     sl.FACE                    AS face,
     count(DISTINCT imd.SKU_NO) AS skuNoUnit
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
     LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID
   GROUP BY podName, face) C
    ON P.podName = C.podName AND P.face = C.face
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中-> 所有面 货位总体积+个数
  (SELECT
     pod.NAME                     AS podName,
     sl.FACE                      AS face,
     coalesce(sum(slt.VOLUME), 0) AS binTotalM3,
     count(sl.ID)                 AS binTotal
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
   GROUP BY podName, face) D
    ON P.podName = D.podName AND P.face = D.face
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->所有面 已使用的货位总体积+个数
  (
    SELECT
      pod.NAME                     AS podName,
      sl.FACE                      AS face,
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
    GROUP BY podName, face
    #     SELECT
    #      pod.NAME                     AS podName,
    #      sl.FACE                      AS face,
    #      coalesce(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
    #      count(sl.ID)                 AS binNotNullTotal
    #    FROM MD_STORAGELOCATION sl
    #      LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
    #      LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
    #      LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
    #      LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
    #    WHERE su.UNITLOAD_ID IS NOT NULL
    #    GROUP BY podName, face
  ) E
    ON P.podName = E.podName AND P.face = E.face
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中->所有面 未使用的货位总体积+个数
  (SELECT
     pod.NAME                     AS podName,
     sl.FACE                      AS face,
     coalesce(sum(slt.VOLUME), 0) AS binNullTotalM3,
     count(sl.ID)                 AS binNullTotal
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NULL
   GROUP BY podName, face
  ) F
    ON P.podName = F.podName AND P.face = F.face
  LEFT JOIN
  # 获取Pod 名字，类型，当前位置，Pod中-> 所有面 未使用的 BufferBin的数量
  (SELECT
     pod.NAME     AS podName,
     sl.FACE      AS face,
     count(sl.ID) AS bufferBinNullTotal
   FROM MD_STORAGELOCATION sl
     LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
     LEFT JOIN MD_AREA ar ON sl.AREA_ID = ar.ID
     LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
     LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
     LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
   WHERE su.UNITLOAD_ID IS NULL
         AND ar.USE_FOR_STORAGE = TRUE
   GROUP BY podName, face) G
    ON P.podName = G.podName AND P.face = G.face
WHERE 1 = 1
ORDER BY podName, face;


SELECT
  pod.NAME                     AS podName,
  sl.FACE                      AS face,
  coalesce(sum(slt.VOLUME), 0) AS binNotNullTotalM3,
  count(sl.ID)                 AS binNotNullTotal
FROM MD_STORAGELOCATION sl
  LEFT JOIN MD_POD pod ON sl.POD_ID = pod.ID
  LEFT JOIN MD_STORAGELOCATIONTYPE slt ON sl.TYPE_ID = slt.ID
  LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID
  LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID
WHERE su.UNITLOAD_ID IS NOT NULL
GROUP BY podName, face;

SELECT
  pod.NAME                     AS podName,
  sl.FACE                      AS face,
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
GROUP BY podName, face;