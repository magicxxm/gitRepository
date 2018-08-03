/*车牌遗留数据*/
SELECT DISTINCT
  I.SKU_NO                              AS skuNo,
  I.ITEM_NO                             AS skuId,
  I.NAME                                AS skuName,
  C.NAME                                AS clientName,
  SL.NAME                               AS containerName,
  S.AMOUNT                              AS amount,
  SR.RECORD_CODE                        AS activityCode,
  (CASE
   WHEN SR.RECORD_CODE = 'EACH RECEIVE'
     THEN '收货'
   WHEN SR.RECORD_CODE = 'PALLET RECEIVE'
     THEN '收货'
   WHEN SR.RECORD_CODE = 'InventoryOverage'
     THEN '存货盘盈'
   WHEN SR.RECORD_CODE = 'Stow'
     THEN '上架'
   WHEN SR.RECORD_CODE = 'StowLoss'
     THEN '上架盘亏'
   WHEN SR.RECORD_CODE = 'InventoryLoss'
     THEN '存货盘亏'
   WHEN SR.RECORD_CODE = 'StowOverage'
     THEN '上架盘盈'
   WHEN SR.RECORD_CODE = 'ReceiveCubiScan'
     THEN '测量'
   WHEN SR.RECORD_CODE = 'CUBI SCAN'
     THEN '测量'
   ELSE ' '
   END)                                 AS state,
  S.CREATED_DATE                        AS modifiedDate,
  format(
      (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE,
                     NOW())) / 1440, 2) AS totalTime
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
WHERE U.STORAGELOCATION_ID IS NOT NULL
      AND SR.RECORD_CODE IN ('EACH RECEIVE', 'PALLET RECEIVE', 'CUBI SCAN')
      AND S.AMOUNT > 0;

/* FUD  库存明细*/
SELECT DISTINCT
  S.ITEMDATA_ID                         AS itemDataId,
  I.SKU_NO                              AS skuNo,
  I.ITEM_NO                             AS skuId,
  I.NAME                                AS skuName,
  C.NAME                                AS clientName,
  SL.NAME                               AS containerName,
  S.AMOUNT                              AS amount,
  SR.RECORD_CODE                        AS activityCode,
  (CASE
   WHEN SR.RECORD_CODE = 'EACH RECEIVE'
     THEN '收货'
   WHEN SR.RECORD_CODE = 'PALLET RECEIVE'
     THEN '收货'
   WHEN SR.RECORD_CODE = 'InventoryOverage'
     THEN '存货盘盈'
   WHEN SR.RECORD_CODE = 'Stow'
     THEN '上架'
   WHEN SR.RECORD_CODE = 'StowLoss'
     THEN '上架盘亏'
   WHEN SR.RECORD_CODE = 'InventoryLoss'
     THEN '存货盘亏'
   WHEN SR.RECORD_CODE = 'StowOverage'
     THEN '上架盘盈'
   WHEN SR.RECORD_CODE = 'ReceiveCubiScan'
     THEN '测量'
   WHEN SR.RECORD_CODE = 'CUBI SCAN'
     THEN '测量'
   ELSE ' '
   END)                                 AS state,
  S.MODIFIED_DATE                       AS modifiedDate,
  format(
      (TIMESTAMPDIFF(MINUTE, S.MODIFIED_DATE,
                     NOW())) / 1440, 2) AS totalTime
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
WHERE U.STORAGELOCATION_ID IS NOT NULL
      AND SR.RECORD_CODE IN ('EACH RECEIVE', 'PALLET RECEIVE', 'CUBI SCAN')
      AND S.AMOUNT > 0
      AND S.ITEMDATA_ID = :itemDataID;

/* FUD 订单明细*/
SELECT
  A.ITEMDATA_ID AS itemDataID,
  B.overTime    AS overTime,
  A.AMOUNT      AS amount
FROM
  (SELECT
     CSP.ITEMDATA_ID,
     coalesce(SUM(CSP.AMOUNT), 0) AS AMOUNT
   FROM OB_CUSTOMERSHIPMENT CS
     LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP
       ON CS.ID = CSP.SHIPMENT_ID
   WHERE CS.STATE = 550
   GROUP BY CSP.ITEMDATA_ID) A,
  (SELECT
     ITEMDATA_ID,
     MAX(overTime) AS overTime
   FROM (SELECT
           CSP.ITEMDATA_ID,
           FORMAT((TIMESTAMPDIFF(MINUTE, CS.CREATED_DATE,
                                 NOW())) / 1440, 2) AS overTime
         FROM OB_CUSTOMERSHIPMENT CS
           LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP
             ON CS.ID = CSP.SHIPMENT_ID
         WHERE CS.STATE = 550) AS d
   GROUP BY d.ITEMDATA_ID) B
WHERE A.ITEMDATA_ID = B.ITEMDATA_ID;

# 旧 遗留数据
SELECT DISTINCT
  I.SKU_NO                              AS skuNo,
  I.ITEM_NO                             AS skuId,
  I.NAME                                AS skuName,
  C.NAME                                AS clientName,
  SL.NAME                               AS containerName,
  S.AMOUNT                              AS amount,
  SR.RECORD_TYPE                        AS activityCode,
  (CASE
   WHEN SR.RECORD_TYPE = 'EACH RECEIVE'
     THEN '收货'
   WHEN SR.RECORD_TYPE = 'PALLET RECEIVE'
     THEN '收货'
   WHEN SR.RECORD_TYPE = 'EACH_RECEIVE_TO_TOTE '
     THEN '收货'
   WHEN SR.RECORD_TYPE = 'INVENTORY OVERAGE'
     THEN '存货盘盈'
   WHEN SR.RECORD_TYPE = 'Stow'
     THEN '上架'
   WHEN SR.RECORD_TYPE = 'Stow Loss'
     THEN '上架盘亏'
   WHEN SR.RECORD_TYPE = 'INVENTORY LOSS'
     THEN '存货盘亏'
   WHEN SR.RECORD_TYPE = 'STOW OVERAGE'
     THEN '上架盘盈'
   WHEN SR.RECORD_TYPE = 'ReceiveCubiScan'
     THEN '测量'
   WHEN SR.RECORD_TYPE = 'MEASURE'
     THEN '测量'
   ELSE ' '
   END)                                 AS state,
  S.CREATED_DATE                        AS modifiedDate,
  format(
      (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE,
                     NOW())) / 1440, 2) AS totalTime
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
WHERE U.STORAGELOCATION_ID IS NOT NULL
      AND SR.RECORD_TYPE IN ('EACH_RECEIVE_TO_TOTE', 'MEASURE')
      AND S.AMOUNT > 0;

# 更新 遗留数据
SELECT DISTINCT
  I.SKU_NO                              AS skuNo,
  I.ITEM_NO                             AS skuId,
  I.NAME                                AS skuName,
  C.NAME                                AS clientName,
  SL.NAME                               AS containerName,
  S.AMOUNT                              AS amount,
  SR.TO_STATE                           AS activityCode,
  (CASE
   WHEN SR.TO_STATE = 'Inventory'
     THEN '正品'
   WHEN SR.TO_STATE = 'Pending'
     THEN '待调查'
   WHEN SR.TO_STATE = 'Damage'
     THEN '残品'
   WHEN SR.TO_STATE = 'MEASURED'
     THEN '测量'
   ELSE ' '
   END)                                 AS state,
  S.CREATED_DATE                        AS modifiedDate,
  format(
      (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE,
                     NOW())) / 1440, 2) AS totalTime
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
WHERE
  U.STORAGELOCATION_ID IS NOT NULL
  AND SLT.STORAGETYPE = 'CONTAINER'
  AND SR.TO_STATE IN ('MEASURED', 'Inventory', 'Pending', 'Damage')
  AND S.AMOUNT > 0
ORDER BY SL.NAME;

#  旧 FUD
SELECT DISTINCT
  S.ITEMDATA_ID                         AS itemDataId,
  I.SKU_NO                              AS skuNo,
  I.ITEM_NO                             AS skuId,
  I.NAME                                AS skuName,
  C.NAME                                AS clientName,
  SL.NAME                               AS containerName,
  S.AMOUNT                              AS amount,
  SR.RECORD_TYPE                        AS activityCode,
  (CASE
   WHEN SR.RECORD_TYPE = 'EACH RECEIVE'
     THEN '收货'
   WHEN SR.RECORD_TYPE = 'PALLET RECEIVE'
     THEN '收货'
   WHEN SR.RECORD_TYPE = 'EACH_RECEIVE_TO_TOTE '
     THEN '收货'
   WHEN SR.RECORD_TYPE = 'INVENTORY OVERAGE'
     THEN '存货盘盈'
   WHEN SR.RECORD_TYPE = 'Stow'
     THEN '上架'
   WHEN SR.RECORD_TYPE = 'Stow Loss'
     THEN '上架盘亏'
   WHEN SR.RECORD_TYPE = 'INVENTORY LOSS'
     THEN '存货盘亏'
   WHEN SR.RECORD_TYPE = 'STOW OVERAGE'
     THEN '上架盘盈'
   WHEN SR.RECORD_TYPE = 'ReceiveCubiScan'
     THEN '测量'
   WHEN SR.RECORD_TYPE = 'MEASURE'
     THEN '测量'
   ELSE ' '
   END)                                 AS state,
  S.MODIFIED_DATE                       AS modifiedDate,
  format(
      (TIMESTAMPDIFF(MINUTE, S.MODIFIED_DATE,
                     NOW())) / 1440, 2) AS totalTime
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
WHERE U.STORAGELOCATION_ID IS NOT NULL
      AND SR.RECORD_TYPE IN ('EACH_RECEIVE_TO_TOTE', 'MEASURE')
      AND S.AMOUNT > 0
      AND S.ITEMDATA_ID = :itemDataID;

#  更新 FUD
SELECT DISTINCT
  S.ITEMDATA_ID                         AS itemDataId,
  I.SKU_NO                              AS skuNo,
  I.ITEM_NO                             AS skuId,
  I.NAME                                AS skuName,
  C.NAME                                AS clientName,
  SL.NAME                               AS containerName,
  S.AMOUNT                              AS amount,
  SR.TO_STATE                           AS activityCode,
  (CASE
   WHEN SR.TO_STATE = 'Inventory'
     THEN '正品'
   WHEN SR.TO_STATE = 'Pending'
     THEN '待调查'
   WHEN SR.TO_STATE = 'Damage'
     THEN '残品'
   WHEN SR.TO_STATE = 'MEASURED'
     THEN '测量'
   ELSE ' '
   END)                                 AS state,
  S.MODIFIED_DATE                       AS modifiedDate,
  format(
      (TIMESTAMPDIFF(MINUTE, S.MODIFIED_DATE,
                     NOW())) / 1440, 2) AS totalTime
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
WHERE U.STORAGELOCATION_ID IS NOT NULL
      AND SR.TO_STATE IN ('MEASURED', 'Inventory')
      AND S.AMOUNT > 0
      AND S.ITEMDATA_ID = :itemDataID;


SELECT
  S.STATE,
  S.AMOUNT,
  I.ITEM_NO,
  SR.RECORD_TYPE,
  SR.AMOUNT,
  SR.RECORD_TOOL,
  SR.TO_STATE
FROM INV_STOCKUNIT S
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
WHERE I.ITEM_NO = '576389317006461';

# 更新1
SELECT DISTINCT
  I.SKU_NO                                                                         AS skuNo,
  I.ITEM_NO                                                                        AS skuId,
  I.NAME                                                                           AS skuName,
  C.NAME                                                                           AS clientName,
  SL.NAME                                                                          AS containerName,
  S.AMOUNT                                                                         AS amount,
  SR.RECORD_TYPE                                                                   AS activityCode,
  (CASE
   WHEN SR.TO_STATE = 'Inventory'
     THEN '正品'
   WHEN SR.TO_STATE = 'Pending'
     THEN '待调查'
   WHEN SR.TO_STATE = 'Damage'
     THEN '残品'
   WHEN SR.TO_STATE = 'MEASURED'
     THEN '测量'
   ELSE ' '
   END)                                                                            AS state,
  S.CREATED_DATE                                                                   AS modifiedDate,
  format(
      (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE, '   LocalDateTime.now()   
                       ')) / 1440, 2) AS totalTime
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
WHERE U.STORAGELOCATION_ID IS NOT NULL
      AND SLT.STORAGETYPE = 'CONTAINER'
      AND SR.RECORD_TYPE NOT IN
          ('SORT', 'PACK LOSS', 'PACK OVERAGE', 'REBIN LOSS', 'REBIN OVERAGE', 'REBATCH', 'INVENTORY LOSS')
      OR ((SR.RECORD_TYPE LIKE 'PICK PICK%' AND SR.TO_STATE <> 'INVENTORY')
          OR (SR.RECORD_TYPE LIKE 'REBIN REBIN%' AND SR.TO_STATE <> 'INVENTORY')
          OR (SR.RECORD_TYPE IN ('PACK VERIFY', 'PACK DIRECT') AND SR.TO_STATE <> 'INVENTORY'))
         AND S.AMOUNT > 0
ORDER BY SL.NAME;

# 更新2
SELECT DISTINCT
  I.SKU_NO                                                                         AS skuNo,
  I.ITEM_NO                                                                        AS skuId,
  I.NAME                                                                           AS skuName,
  C.NAME                                                                           AS clientName,
  SL.NAME                                                                          AS containerName,
  S.AMOUNT                                                                         AS amount,
  SR.RECORD_TYPE                                                                   AS activityCode,
  (CASE
   WHEN SR.TO_STATE = 'Inventory'
     THEN '正品'
   WHEN SR.TO_STATE = 'Pending'
     THEN '待调查'
   WHEN SR.TO_STATE = 'Damage'
     THEN '残品'
   WHEN SR.TO_STATE = 'MEASURED'
     THEN '测量'
   ELSE ' '
   END)                                                                            AS state,
  S.CREATED_DATE                                                                   AS modifiedDate,
  format(
      (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE, '   LocalDateTime.now()   
                       ')) / 1440, 2) AS totalTime
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
WHERE U.STORAGELOCATION_ID IS NOT NULL
      AND S.AMOUNT > 0
      AND SLT.STORAGETYPE = 'CONTAINER'
      AND (SR.RECORD_TYPE NOT IN
           ('SORT', 'PACK LOSS', 'PACK OVERAGE', 'REBIN LOSS', 'REBIN OVERAGE', 'REBATCH', 'INVENTORY LOSS')
           OR ((SR.RECORD_TYPE LIKE 'PICK PICK%' AND SR.TO_STATE <> 'INVENTORY')
               OR (SR.RECORD_TYPE LIKE 'REBIN REBIN%' AND SR.TO_STATE <> 'INVENTORY')
               OR (SR.RECORD_TYPE IN ('PACK VERIFY', 'PACK DIRECT') AND SR.TO_STATE <> 'INVENTORY')))
ORDER BY SL.NAME;

# 更新3
SELECT DISTINCT
  I.SKU_NO                                                                         AS skuNo,
  I.ITEM_NO                                                                        AS skuId,
  I.NAME                                                                           AS skuName,
  C.NAME                                                                           AS clientName,
  SL.NAME                                                                          AS containerName,
  S.AMOUNT                                                                         AS amount,
  SR.RECORD_TYPE                                                                   AS activityCode,
  (CASE
   WHEN SR.TO_STATE = 'Inventory'
     THEN '正品'
   WHEN SR.TO_STATE = 'Pending'
     THEN '待调查'
   WHEN SR.TO_STATE = 'Damage'
     THEN '残品'
   WHEN SR.TO_STATE = 'MEASURED'
     THEN '测量'
   ELSE ' '
   END)                                                                            AS state,
  S.CREATED_DATE                                                                   AS modifiedDate,
  format(
      (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE, '   LocalDateTime.now()   
                       ')) / 1440, 2) AS totalTime
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
WHERE U.STORAGELOCATION_ID IS NOT NULL
      AND SLT.STORAGETYPE = 'CONTAINER'
      AND S.AMOUNT > 0
      AND (SR.RECORD_TYPE NOT IN
           ('SORT', 'PACK LOSS', 'PACK OVERAGE', 'REBIN LOSS', 'REBIN OVERAGE', 'REBATCH', 'INVENTORY LOSS')
           OR ((SR.RECORD_TYPE LIKE 'PICK PICK%' AND SR.TO_STATE <> 'INVENTORY')
               OR (SR.RECORD_TYPE LIKE 'REBIN REBIN%' AND SR.TO_STATE <> 'INVENTORY')
               OR (SR.RECORD_TYPE IN ('PACK VERIFY', 'PACK DIRECT') AND SR.TO_STATE <> 'INVENTORY'))
           OR ((SR.TO_STATE = 'Inventory' AND SL.NAME <> 'Problem Solved')
               OR (SR.TO_STATE = 'Inventory' AND SR.RECORD_TYPE = 'OB PROBLEM SOLVE' AND SL.NAME NOT LIKE 'OBPSW%'))
      )
#       or  (SR.RECORD_TYPE = 'OB PROBLEM SOLVE' AND SL.NAME NOT IN ('Problem Solved','OBPSW0001-F15'))
ORDER BY S.CREATED_DATE;


SELECT *
FROM MD_STORAGELOCATION SL
  LEFT JOIN MD_STORAGELOCATIONTYPE STY ON SL.TYPE_ID = STY.ID
WHERE SL.NAME = 'OB PROBLEM SOLVE';


SELECT DISTINCT
  I.SKU_NO                                                                         AS skuNo,
  I.ITEM_NO                                                                        AS skuId,
  I.NAME                                                                           AS skuName,
  C.NAME                                                                           AS clientName,
  SL.NAME                                                                          AS containerName,
  S.AMOUNT                                                                         AS amount,
  SR.RECORD_TYPE                                                                   AS activityCode,
  (CASE
   WHEN SR.TO_STATE = 'Inventory'
     THEN '正品'
   WHEN SR.TO_STATE = 'Pending'
     THEN '待调查'
   WHEN SR.TO_STATE = 'Damage'
     THEN '残品'
   WHEN SR.TO_STATE = 'MEASURED'
     THEN '测量'
   ELSE ' '
   END)                                                                            AS state,
  S.CREATED_DATE                                                                   AS modifiedDate,
  format(
      (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE, '   LocalDateTime.now()   
                       ')) / 1440, 2) AS totalTime
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
WHERE U.STORAGELOCATION_ID IS NOT NULL
      AND S.AMOUNT > 0
      AND SLT.STORAGETYPE = 'CONTAINER'
      AND ((SR.RECORD_TYPE NOT IN
            ('SORT', 'PACK LOSS', 'PACK OVERAGE', 'REBIN LOSS', 'REBIN OVERAGE', 'REBATCH', 'INVENTORY LOSS', 'REBIN-PACK')
) SL.NAME NOT LIKE 'OB%')
      OR ((SR.RECORD_TYPE LIKE 'PICK PICK%' AND SR.TO_STATE <> 'INVENTORY')
          OR (SR.RECORD_TYPE LIKE 'REBIN REBIN%' AND SR.TO_STATE <> 'INVENTORY')
          OR (SR.RECORD_TYPE IN ('PACK VERIFY', 'PACK DIRECT') AND SR.TO_STATE <> 'INVENTORY')
          OR ((SR.TO_STATE = 'Inventory' AND SL.NAME <> 'Problem Solved')
              OR (SR.TO_STATE = 'Inventory' AND SR.RECORD_TYPE = 'OB PROBLEM SOLVE' AND SL.NAME NOT LIKE 'OBPSW%'))
      )
);


SELECT *
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
WHERE SR.TO_STATE = 'Inventory' AND SR.RECORD_TYPE = 'OB PROBLEM SOLVE' AND SL.NAME NOT LIKE 'OB%';


SELECT DISTINCT
  I.SKU_NO                                                                           AS skuNo,
  I.ITEM_NO                                                                          AS skuId,
  I.NAME                                                                             AS skuName,
  C.NAME                                                                             AS clientName,
  SL.NAME                                                                            AS containerName,
  S.AMOUNT                                                                           AS amount,
  SR.RECORD_TYPE                                                                     AS activityCode,
  SR.TO_STATE                                                                        AS STATE,
  (CASE
   WHEN SR.TO_STATE = 'Inventory'
     THEN '正品'
   WHEN SR.TO_STATE = 'Pending'
     THEN '待调查'
   WHEN SR.TO_STATE = 'Damage'
     THEN '残品'
   WHEN SR.TO_STATE = 'MEASURED'
     THEN '测量'
   ELSE ' '
   END)                                                                              AS state,
  S.CREATED_DATE                                                                     AS modifiedDate,
  format(
      (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE, '       LocalDateTime.now()
                         ')) / 1440, 2) AS totalTime
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
WHERE U.STORAGELOCATION_ID IS NOT NULL
      AND S.AMOUNT > 0
      AND SLT.STORAGETYPE = 'CONTAINER'
      AND SR.RECORD_TYPE NOT IN
          ('SORT', 'PACK LOSS', 'PACK OVERAGE', 'REBIN LOSS', 'REBIN OVERAGE', 'REBATCH', 'INVENTORY LOSS', 'REBIN-PACK')
      AND ((SR.RECORD_TYPE LIKE 'PACK VERIFY%' AND SR.TO_STATE <> 'INVENTORY')
           AND (SR.TO_STATE = 'Inventory' AND SL.NAME <> 'Problem Solved')
           AND (SR.TO_STATE = 'Inventory' AND SR.RECORD_TYPE = 'OB PROBLEM SOLVE' AND SL.NAME NOT LIKE 'OBPSW%'));
/*AND( (SR.RECORD_TYPE NOT IN
     ('SORT', 'PACK LOSS', 'PACK OVERAGE', 'REBIN LOSS', 'REBIN OVERAGE', 'REBATCH', 'INVENTORY LOSS','REBIN-PACK'))
     AND (((SR.RECORD_TYPE LIKE 'PICK PICK%' AND SR.TO_STATE <> 'INVENTORY')
         OR (SR.RECORD_TYPE LIKE 'REBIN REBIN%' AND SR.TO_STATE <> 'INVENTORY')
         OR (SR.RECORD_TYPE IN ('PACK VERIFY', 'PACK DIRECT') AND SR.TO_STATE <> 'INVENTORY')
         OR ((SR.TO_STATE  = 'Inventory' AND SL.NAME <> 'Problem Solved')
         OR (SR.TO_STATE  = 'Inventory' AND SR.RECORD_TYPE = 'OB PROBLEM SOLVE' AND SL.NAME NOT LIKE 'OBPSW%'))

     ))      )*/



ORDER BY S.CREATED_DATE;


SELECT *
FROM (SELECT TAB3.ID
      FROM (SELECT DISTINCT
              I.SKU_NO                                                                           AS skuNo,
              I.ITEM_NO                                                                          AS skuId,
              I.NAME                                                                             AS skuName,
              C.NAME                                                                             AS clientName,
              SL.NAME                                                                            AS containerName,
              S.AMOUNT                                                                           AS amount,
              SR.RECORD_TYPE                                                                     AS activityCode,
              SR.TO_STATE                                                                        AS STATE1,
              SR.RECORD_TYPE                                                                     AS RECORDTYPE,
              (CASE
               WHEN SR.TO_STATE = 'Inventory'
                 THEN '正品'
               WHEN SR.TO_STATE = 'Pending'
                 THEN '待调查'
               WHEN SR.TO_STATE = 'Damage'
                 THEN '残品'
               WHEN SR.TO_STATE = 'MEASURED'
                 THEN '测量'
               ELSE ' '
               END)                                                                              AS state,
              S.CREATED_DATE                                                                     AS modifiedDate,
              format(
                  (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE, '       LocalDateTime.now()
                         ')) / 1440, 2) AS totalTime
            FROM INV_STOCKUNIT S
              LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
              LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
              LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
              LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
              LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID
              LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
            WHERE U.STORAGELOCATION_ID IS NOT NULL
                  AND S.AMOUNT > 0
                  AND SLT.STORAGETYPE = 'CONTAINER'
                  AND SR.RECORD_TYPE NOT IN
                      ('SORT', 'PACK LOSS', 'PACK OVERAGE', 'REBIN LOSS', 'REBIN OVERAGE', 'REBATCH', 'INVENTORY LOSS', 'REBIN-PACK')) TAB3) TAB5
WHERE TAB5.ID NOT IN
WHERE (TAB3.STATE = 'INVENTORY'
AND (TAB3.activityCode LIKE 'PICK PICK%' OR SR.RECORD_TYPE LIKE 'REBIN REBIN%' OR SR.RECORD_TYPE = 'PACK VERIFY'
OR SR.RECORD_TYPE = 'PACK DIRECT' OR TAB3.containerName = 'Problem Solved'
OR (TAB3.activityCode = 'OB PROBLEM SOLVE' AND TAB3.containerName NOT LIKE 'OBPSW%')));


SELECT *
FROM (SELECT TAB3.ID
      FROM (SELECT DISTINCT
              I.SKU_NO                                                                           AS skuNo,
              I.ITEM_NO                                                                          AS skuId,
              I.NAME                                                                             AS skuName,
              C.NAME                                                                             AS clientName,
              SL.NAME                                                                            AS containerName,
              S.AMOUNT                                                                           AS amount,
              SR.RECORD_TYPE                                                                     AS activityCode,
              SR.TO_STATE                                                                        AS STATE1,
              SR.RECORD_TYPE                                                                     AS RECORDTYPE,
              (CASE
               WHEN SR.TO_STATE = 'Inventory'
                 THEN '正品'
               WHEN SR.TO_STATE = 'Pending'
                 THEN '待调查'
               WHEN SR.TO_STATE = 'Damage'
                 THEN '残品'
               WHEN SR.TO_STATE = 'MEASURED'
                 THEN '测量'
               ELSE ' '
               END)                                                                              AS state,
              S.CREATED_DATE                                                                     AS modifiedDate,
              format(
                  (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE, '       LocalDateTime.now()
                         ')) / 1440, 2) AS totalTime
            FROM INV_STOCKUNIT S
              LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
              LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
              LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
              LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
              LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID
              LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
            WHERE U.STORAGELOCATION_ID IS NOT NULL
                  AND S.AMOUNT > 0
                  AND SLT.STORAGETYPE = 'CONTAINER'
                  AND SR.RECORD_TYPE NOT IN
                      ('SORT', 'PACK LOSS', 'PACK OVERAGE', 'REBIN LOSS', 'REBIN OVERAGE', 'REBATCH', 'INVENTORY LOSS', 'REBIN-PACK')) TAB3
      WHERE (TAB3.STATE = 'INVENTORY'
             AND (TAB3.activityCode LIKE 'PICK PICK%' OR SR.RECORD_TYPE LIKE 'REBIN REBIN%' OR
                  SR.RECORD_TYPE = 'PACK VERIFY'
                  OR SR.RECORD_TYPE = 'PACK DIRECT' OR TAB3.containerName = 'Problem Solved'
                  OR (TAB3.activityCode = 'OB PROBLEM SOLVE' AND TAB3.containerName NOT LIKE 'OBPSW%')))) TAB5
WHERE TAB5.ID NOT IN TAB5;


SELECT *
FROM TAB5
WHERE TAB5.ID NOT IN
      (SELECT TAB3.ID
       FROM TAB3
       WHERE (TAB3.STATE = 'INVENTORY'
              AND (TAB3.activityCode LIKE 'PICK PICK%' OR SR.RECORD_TYPE LIKE 'REBIN REBIN%' OR
                   SR.RECORD_TYPE = 'PACK VERIFY'
                   OR SR.RECORD_TYPE = 'PACK DIRECT' OR TAB3.containerName = 'Problem Solved'
                   OR (TAB3.activityCode = 'OB PROBLEM SOLVE' AND TAB3.containerName NOT LIKE 'OBPSW%'))));


SELECT *
FROM TAB5
WHERE TAB5.ID NOT IN
      (SELECT TAB3.ID
       FROM
         (SELECT DISTINCT
            S.ID                                                          AS ID,
            I.SKU_NO                                                      AS skuNo,
            I.ITEM_NO                                                     AS skuId,
            I.NAME                                                        AS skuName,
            C.NAME                                                        AS clientName,
            SL.NAME                                                       AS containerName,
            S.AMOUNT                                                      AS amount,
            SR.RECORD_TYPE                                                AS activityCode,
            SR.TO_STATE                                                   AS STATE1,
            (CASE
             WHEN SR.TO_STATE = 'Inventory'
               THEN '正品'
             WHEN SR.TO_STATE = 'Pending'
               THEN '待调查'
             WHEN SR.TO_STATE = 'Damage'
               THEN '残品'
             WHEN SR.TO_STATE = 'MEASURED'
               THEN '测量'
             ELSE ' '
             END)                                                         AS state,
            S.CREATED_DATE                                                AS modifiedDate,
            format(
                (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE, '       LocalDateTime.now()
    ')) / 1440, 2) AS totalTime
          FROM INV_STOCKUNIT S
            LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
            LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
            LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
            LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
            LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID
            LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
          WHERE U.STORAGELOCATION_ID IS NOT NULL
                AND S.AMOUNT > 0
                AND SLT.STORAGETYPE = 'CONTAINER'
                AND SR.RECORD_TYPE NOT IN
                    ('SORT', 'PACK LOSS', 'PACK OVERAGE', 'REBIN LOSS', 'REBIN OVERAGE', 'REBATCH', 'INVENTORY LOSS', 'REBIN-PACK')) TAB3
       WHERE (TAB3.STATE = 'INVENTORY'
              AND (TAB3.activityCode LIKE 'PICK PICK%' OR SR.RECORD_TYPE LIKE 'REBIN REBIN%' OR
                   SR.RECORD_TYPE = 'PACK VERIFY'
                   OR SR.RECORD_TYPE = 'PACK DIRECT' OR
                   (TAB3.activityCode = 'OB PROBLEM SOLVE' AND TAB3.containerName = 'Problem Solved')
                   OR (TAB3.activityCode = 'OB PROBLEM SOLVE' AND TAB3.containerName NOT LIKE 'OBPSW%'))));
TAB5;

SELECT DISTINCT
  S.ID                                                          AS ID,
  I.SKU_NO                                                      AS skuNo,
  I.ITEM_NO                                                     AS skuId,
  I.NAME                                                        AS skuName,
  C.NAME                                                        AS clientName,
  SL.NAME                                                       AS containerName,
  S.AMOUNT                                                      AS amount,
  SR.RECORD_TYPE                                                AS activityCode,
  SR.TO_STATE                                                   AS STATE1,
  (CASE
   WHEN SR.TO_STATE = 'Inventory'
     THEN '正品'
   WHEN SR.TO_STATE = 'Pending'
     THEN '待调查'
   WHEN SR.TO_STATE = 'Damage'
     THEN '残品'
   WHEN SR.TO_STATE = 'MEASURED'
     THEN '测量'
   ELSE ' '
   END)                                                         AS state,
  S.CREATED_DATE                                                AS modifiedDate,
  format(
      (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE, '       LocalDateTime.now()
    ')) / 1440, 2) AS totalTime
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
WHERE U.STORAGELOCATION_ID IS NOT NULL
      AND S.AMOUNT > 0
      #       AND ( SL.NAME NOT LIKE 'OB%')
      AND SR.RECORD_TYPE NOT IN
          ('SORT', 'PACK LOSS', 'PACK OVERAGE', 'REBIN LOSS', 'REBIN OVERAGE', 'REBATCH', 'INVENTORY LOSS', 'REBIN-PACK')
      AND (
        (SR.RECORD_TYPE NOT IN ('PACK VERIFY', 'PACK DIRECT')
         OR SR.RECORD_TYPE NOT LIKE 'PICK PICK%'
         OR SR.RECORD_TYPE NOT LIKE 'REBIN REBIN%'
         OR SL.NAME <> 'Problem Solved'
         OR SL.NAME <> 'Problem Solving'
         OR (SR.RECORD_TYPE <> 'OB PROBLEM SOLVE' AND SL.NAME NOT LIKE 'OB%')

        )
        AND SR.TO_STATE = 'INVENTORY'

      );


SELECT DISTINCT
  I.SKU_NO                                                                         AS skuNo,
  I.ITEM_NO                                                                        AS skuId,
  I.NAME                                                                           AS skuName,
  C.NAME                                                                           AS clientName,
  SL.NAME                                                                          AS containerName,
  S.AMOUNT                                                                         AS amount,
  SR.RECORD_TYPE                                                                   AS activityCode,
  (CASE
   WHEN SR.TO_STATE = 'Inventory'
     THEN '正品'
   WHEN SR.TO_STATE = 'Pending'
     THEN '待调查'
   WHEN SR.TO_STATE = 'Damage'
     THEN '残品'
   WHEN SR.TO_STATE = 'MEASURED'
     THEN '测量'
   ELSE ' '
   END)                                                                            AS state,
  SLT.INVENTORY_STATE,
  S.CREATED_DATE                                                                   AS modifiedDate,
  format(
      (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE, '   LocalDateTime.now()
                       ')) / 1440, 2) AS totalTime
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID
  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT
#   LEFT JOIN OBP_OBPCELL cell ON cell.NAME <> SL.NAME

WHERE U.STORAGELOCATION_ID IS NOT NULL
      AND SLT.STORAGETYPE = 'CONTAINER'
      AND S.AMOUNT > 0
      AND ((SR.RECORD_TYPE NOT IN
            ('SORT', 'PACK LOSS', 'PACK OVERAGE', 'REBIN LOSS', 'REBIN OVERAGE', 'REBATCH', 'INVENTORY LOSS'))
           OR (SR.RECORD_TYPE LIKE 'PICK PICK%' AND SR.TO_STATE <> 'INVENTORY')
           OR (SR.RECORD_TYPE LIKE 'REBIN REBIN%' AND SR.TO_STATE <> 'INVENTORY')
           OR (SR.RECORD_TYPE IN ('PACK VERIFY', 'PACK DIRECT') AND SR.TO_STATE <> 'INVENTORY')
           OR (SR.RECORD_TYPE = 'OB PROBLEM SOLVE' AND SR.TO_STATE <> 'Inventory')
      )
ORDER BY S.CREATED_DATE;

#  FUD 订单明细 sql
SELECT
  A.ITEMDATA_ID AS itemDataID,
  B.overTime    AS overTime,
  A.AMOUNT      AS amount
FROM
  (SELECT
     CSP.ITEMDATA_ID,
     coalesce(SUM(CSP.AMOUNT), 0) AS AMOUNT
   FROM OB_CUSTOMERSHIPMENT CS
     LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP
       ON CS.ID = CSP.SHIPMENT_ID
   WHERE CS.STATE = 550
   GROUP BY CSP.ITEMDATA_ID) A,
  (SELECT
     ITEMDATA_ID,
     MAX(overTime) AS overTime
   FROM (SELECT
           CSP.ITEMDATA_ID,
           FORMAT((TIMESTAMPDIFF(MINUTE, CS.CREATED_DATE, '  LocalDateTime.now() +
                                ')) / 1440, 2) AS overTime
         FROM OB_CUSTOMERSHIPMENT CS
           LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP
             ON CS.ID = CSP.SHIPMENT_ID
         WHERE CS.STATE = 550) AS d
   GROUP BY d.ITEMDATA_ID) B
WHERE A.ITEMDATA_ID = B.ITEMDATA_ID;


SELECT
  A.ITEMDATA_ID AS itemDataID,
  B.overTime    AS overTime,
  A.AMOUNT      AS amount
FROM
  (
    SELECT
      POP.ITEMDATA_ID,
      coalesce(SUM(POP.AMOUNT - POP.AMOUNT_PICKED), 0) AS AMOUNT
    FROM OB_PICKINGORDERPOSITION POP
      LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
    WHERE PO.STATE < 600
          AND POP.CUSTOMERSHIPMENTPOSITION_ID
              IN (SELECT CSP.ID
                  FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                    LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID)
          AND POP.PICKINGORDER_ID IS NOT NULL
    GROUP BY POP.ITEMDATA_ID
  ) A,
  (SELECT
     ITEMDATA_ID,
     MAX(overTime) AS overTime
   FROM (
          SELECT
            POP.ITEMDATA_ID,
            FORMAT((TIMESTAMPDIFF(MINUTE, CS.CREATED_DATE, LocalDateTime.now()
            )) / 1440, 2) AS overTime
          FROM OB_PICKINGORDERPOSITION POP
            LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
            LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID
            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
          WHERE PO.STATE < 600
                AND POP.CUSTOMERSHIPMENTPOSITION_ID
                    IN (SELECT CSP.ID
                        FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                          LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID)
                AND POP.PICKINGORDER_ID IS NOT NULL
          GROUP BY POP.ITEMDATA_ID
        ) AS d
   GROUP BY d.ITEMDATA_ID) B
WHERE A.ITEMDATA_ID = B.ITEMDATA_ID;


SELECT
  POP.ITEMDATA_ID,
  FORMAT((TIMESTAMPDIFF(MINUTE, CS.CREATED_DATE, LocalDateTime.now()
  )) / 1440, 2) AS overTime
FROM OB_PICKINGORDERPOSITION POP
  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
WHERE PO.STATE < 600
      AND POP.CUSTOMERSHIPMENTPOSITION_ID
          IN (SELECT CSP.ID
              FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID)
      AND POP.PICKINGORDER_ID IS NOT NULL
GROUP BY POP.ITEMDATA_ID;

# PickingNotYetPicked 8代
SELECT
  #   POP.ITEMDATA_ID,
  coalesce(SUM(POP.AMOUNT - POP.AMOUNT_PICKED), 0) AS AMOUNT
FROM OB_PICKINGORDERPOSITION POP
  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
WHERE PO.STATE < 600
      AND POP.CUSTOMERSHIPMENTPOSITION_ID
          IN (SELECT CSP.ID
              FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID)
      AND POP.PICKINGORDER_ID IS NOT NULL
GROUP BY POP.ITEMDATA_ID;

#  8代最新版 FUD  2017.11.20
SELECT
  A.ITEMDATA_ID AS itemDataID,
  B.overTime    AS overTime,
  A.AMOUNT      AS amount
FROM
  (
    SELECT
      POP.ITEMDATA_ID,
      coalesce(SUM(POP.AMOUNT - POP.AMOUNT_PICKED), 0) AS AMOUNT
    FROM OB_PICKINGORDERPOSITION POP
      LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
    WHERE PO.STATE < 600
          AND POP.CUSTOMERSHIPMENTPOSITION_ID
              IN (SELECT CSP.ID
                  FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                    LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID)
          AND POP.PICKINGORDER_ID IS NOT NULL
    GROUP BY POP.ITEMDATA_ID
  ) A,
  (SELECT
     ITEMDATA_ID,
     MAX(overTime) AS overTime
   FROM (
          SELECT
            POP.ITEMDATA_ID,
            FORMAT((TIMESTAMPDIFF(MINUTE, CS.CREATED_DATE, '   LocalDateTime.now() +
                                                 ')) / 1440, 2) AS overTime
          FROM OB_PICKINGORDERPOSITION POP
            LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
            LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID
            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
          WHERE PO.STATE < 600
                AND POP.CUSTOMERSHIPMENTPOSITION_ID
                    IN (SELECT CSP.ID
                        FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                          LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID)
                AND POP.PICKINGORDER_ID IS NOT NULL
          GROUP BY POP.ITEMDATA_ID
        ) AS d
   GROUP BY d.ITEMDATA_ID) B
WHERE A.ITEMDATA_ID = B.ITEMDATA_ID;


