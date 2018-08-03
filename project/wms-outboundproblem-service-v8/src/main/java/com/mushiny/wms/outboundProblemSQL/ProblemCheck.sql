# 拣货历史记录
SELECT
     SL.ID          AS storageLocationId,
     SL.NAME        AS name,
      COALESCE(C.amount,0)       AS amount,
     COALESCE (B.actualAmount,0) AS actualAmount,
     COALESCE(A.total_amuont,0) AS totalAmount,
     B.clientId     AS clientId,
     A.clientName   AS clientName,
     B.lotId        AS lotId,
     B.itemId       AS itemDataId
    FROM
                  #   容器的历史上货容器
    (SELECT DISTINCT SUR.FROM_STORAGELOCATION AS name
            FROM INV_STOCKUNITRECORD SUR             
              LEFT JOIN INV_UNITLOAD U ON SUR.TO_UNITLOAD = U.LABEL        
            WHERE             
              U.ENTITY_LOCK = 1        
              AND SUR.TO_STORAGELOCATION = 'RW00000018B02' AND (SUR.RECORD_TOOL = 'Rebin' OR SUR.RECORD_TOOL = 'Pack')
    ) L
           LEFT JOIN MD_STORAGELOCATION SL ON L.name = SL.NAME             
#            LEFT JOIN MD_STORAGELOCATIONTYPE SLY ON SL.TYPE_ID = SLY.ID
           LEFT JOIN             
                 # 货位商品当前总数
           (SELECT DISTINCT        
              SL.ID         AS storageLocationId,
              C.NAME        AS clientName,
              SUM(S.AMOUNT) AS total_amuont             
            FROM INV_STOCKUNIT S             
              LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID             
              LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
              LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
            WHERE 1 = 1
            GROUP BY SL.ID,C.NAME) A ON SL.ID = A.storageLocationId
           LEFT JOIN
           (             
                  # 货位商品当前数量
             SELECT DISTINCT        
               SL.ID         AS storageLocationId,
               C.ID          AS clientId,
               C.NAME        AS clientName,
               L.ID          AS lotId,
               I.ID          AS itemId,
               SUM(S.AMOUNT) AS actualAmount
             FROM INV_STOCKUNIT S             
               LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID             
               LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID             
               LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
               LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID
               LEFT JOIN INV_LOT L ON S.LOT_ID = L.ID
             WHERE S.AMOUNT > 0
                   AND I.ITEM_NO = '324542005403443'
             GROUP BY SL.ID, I.ID, C.ID, C.NAME, L.ID
           ) B
             ON A.storageLocationId = B.storageLocationId
           LEFT JOIN             
           (             
                   #当前容器,当前商品的上货历史记录数据
             SELECT DISTINCT        
               SUR.FROM_STORAGELOCATION AS storageLocationName,             
               I.ID                   AS itemId,             
               SUM(SUR.AMOUNT)        AS amount        
             FROM INV_STOCKUNITRECORD SUR             
               LEFT JOIN INV_UNITLOAD U ON SUR.TO_UNITLOAD = U.LABEL        
               LEFT JOIN MD_ITEMDATA I ON SUR.ITEMDATA_ITEMNO = I.ITEM_NO             
             WHERE        
               SUR.TO_STORAGELOCATION = 'RW00000018B02'
#                AND SUR.AMOUNT > 0
               AND U.ENTITY_LOCK = 1        
               AND U.STORAGELOCATION_ID IS NOT NULL        
               AND SUR.ITEMDATA_ITEMNO = '324542005403443'
             GROUP BY SUR.FROM_STORAGELOCATION,        
               SUR.ITEMDATA_ITEMNO, I.ID        
           ) C             
             ON SL.NAME = C.storageLocationName             
          AND B.itemId = C.itemId
#            WHERE SLY.STORAGETYPE ='BIN'
#             AND A.total_amuont >0
         ORDER BY amount DESC     ;


SELECT
  S.AMOUNT AS actualAmount,
  SL.ID         AS storageLocationId,
SL.NAME
FROM INV_STOCKUNIT S
  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID
  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID
WHERE I.ITEM_NO = '324542005403443';


# 2.rebin车记录
SELECT  DISTINCT      
                      B.ItemNo               AS ItemNo,
                      B.ItemSku                  AS ItemSku,
                      SL.NAME        AS Wall,      
                      right(L.TO_STORAGELOCATION, 4)  AS Cell,      
                      COALESCE (B.actualAmount,0) AS Amount,      
                      COALESCE(A.total_amuont,0) AS RebinNumber      
                     FROM      
                          #   容器的历史上货容器      
      (SELECT DISTINCT SUR.TO_STORAGELOCATION AS TO_STORAGELOCATION
        /* SUR.ITEMDATA_ITEMNO               AS ITEMDATA_ITEMNO,
         SUR.ITEMDATA_SKU                  AS ITEMDATA_SKU    */
       FROM INV_STOCKUNITRECORD SUR      
         LEFT JOIN INV_UNITLOAD U ON ((SUR.FROM_UNITLOAD = U.ID) OR (SUR.FROM_UNITLOAD = U.LABEL))      
       WHERE      
         U.ENTITY_LOCK = 1      
         AND SUR.FROM_STORAGELOCATION = 'RW00000018B02' AND (SUR.RECORD_TOOL = 'Rebin' OR SUR.RECORD_TOOL = 'Pack')) L
      LEFT JOIN MD_STORAGELOCATION SL ON L.TO_STORAGELOCATION = SL.NAME      
      LEFT JOIN      
                          # 货位中各商品的当前总数量      
      (SELECT      
         SL.ID         AS storageLocationId ,
         SUM(S.AMOUNT) AS total_amuont      
       FROM INV_STOCKUNIT S      
         LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID      
         LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID
       WHERE S.AMOUNT > 0      
       GROUP BY SL.ID ) A ON SL.ID = A.storageLocationId
      LEFT JOIN      
      (      
                            # 货位中相应商品的当前数量      
        SELECT      
          SUM(S.AMOUNT) AS actualAmount,
          I.ITEM_NO     AS ItemNo,
          I.SKU_NO AS ItemSku,
          SL.ID AS storageLocationId      
        FROM INV_STOCKUNIT S      
          LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID      
          LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID      
          LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID      
        WHERE S.AMOUNT > 0      
              AND I.ITEM_NO = '324542005403443'
        GROUP BY storageLocationId,ItemSku,ItemNo
      ) B      
        ON A.storageLocationId = B.storageLocationId      
     ORDER BY ItemNo, ItemSku,Wall,Cell,Amount,RebinNumber DESC ;

# 3.对应分析 rebin
SELECT DISTINCT       
        obr.REBINFROMCONTAINER_NAME AS problemStoragelocation,       
        obr.AMOUNT_REBINED        AS amount,       
        po.PICKINGORDER_NO        AS batchId,       
        ul.NAME                   AS stationName,       
        u.USERNAME                AS rebinUser,       
        c.NAME                    AS customer,       
        br.MODIFIED_DATE          AS rebinDate       
       FROM OB_REBINREQUESTPOSITION obr       
       LEFT JOIN MD_ITEMDATA it ON obr.ITEMDATA_ID = it.ID       
       LEFT JOIN OB_REBINREQUEST br ON obr.REBINREQUEST_ID = br.ID       
       LEFT JOIN OB_PICKINGORDER po ON br.PICKINGORDER_ID = po.ID       
       LEFT JOIN OB_REBINSTATION ul ON br.REBINSTATION_ID = ul.ID       
       LEFT JOIN SYS_USER u ON br.OPERATOR_ID = u.ID       
       LEFT JOIN SYS_CLIENT c ON obr.CLIENT_ID = c.ID       
       WHERE it.ITEM_NO = :itemNo       
        AND obr.REBINFROMCONTAINER_NAME = :storageLocation       
        AND obr.AMOUNT_REBINED >0;


# 4.对应分析  Pack
 SELECT DISTINCT      
       sl.NAME                   AS storagelocation,       
       obr.AMOUNT_PACKED        AS amount,       
       POP.PICKINGORDER_NO        AS batchId,       
       ul.NAME                   AS stationName,       
       u.USERNAME                AS rebinUser,       
       c.NAME                    AS customer,       
       br.MODIFIED_DATE          AS rebinDate       
    FROM OB_PACKINGREQUESTPOSITION obr       
    LEFT JOIN MD_ITEMDATA it ON obr.ITEMDATA_ID = it.ID       
    LEFT JOIN OB_PACKINGREQUEST br ON obr.PACKINGREQUEST_ID = br.ID       
    LEFT JOIN INV_UNITLOAD ud ON br.FROMUNITLOAD_ID = ud.ID       
    LEFT JOIN MD_STORAGELOCATION sl ON ud.STORAGELOCATION_ID = sl.ID       
    LEFT JOIN OB_PICKINGORDERPOSITION POP ON obr.CUSTOMERSHIPMENTPOSITION_ID = POP.CUSTOMERSHIPMENTPOSITION_ID       
    LEFT JOIN OB_PACKINGSTATION ul ON br.PACKINGSTATION_ID = ul.ID       
    LEFT JOIN SYS_USER u ON br.OPERATOR_ID = u.ID       
    LEFT JOIN SYS_CLIENT c ON obr.CLIENT_ID = c.ID       
    WHERE it.ITEM_NO = :itemNo       
          AND sl.NAME = :storageLocation



