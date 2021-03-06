/* 未处理记录 StockEnough  Replenishing 通用 */
SELECT
  CSP.ITEMDATA_ID              AS itemDataId,
  coalesce(SUM(CSP.AMOUNT), 0) AS amount
FROM OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP
    ON CS.ID = CSP.SHIPMENT_ID
WHERE CS.STATE = 550
      AND CS.DELIVERY_DATE = ''
GROUP BY CSP.ITEMDATA_ID;

/*StockEnough*/
SELECT coalesce(SUM(STU.AMOUNT - STU.RESERVED_AMOUNT), 0) AS StockEnough
FROM INV_STOCKUNIT STU
  LEFT JOIN INV_UNITLOAD AS UL ON STU.UNITLOAD_ID = UL.ID
WHERE UL.STORAGELOCATION_ID
      IN (SELECT STL.ID
          FROM MD_STORAGELOCATION STL
            LEFT JOIN MD_AREA A ON STL.AREA_ID = A.ID
          WHERE A.NAME = 'Picking_Zone')
      AND STU.ITEMDATA_ID = '';

/* Replenishing */
SELECT coalesce(SUM(STU.AMOUNT), 0) AS Replenishing
FROM INV_STOCKUNIT STU
  LEFT JOIN INV_STOCKUNITRECORD SUR ON STU.ID = SUR.TO_STOCKUNIT
WHERE SUR.FROM_STORAGELOCATION
      IN (SELECT STL.NAME
          FROM MD_STORAGELOCATION STL
            LEFT JOIN MD_AREA A ON STL.AREA_ID = A.ID
          WHERE A.NAME = 'Buffer_Zone')
      AND STU.ITEMDATA_ID = '';

/*TotalReplenishment*/
SELECT coalesce(SUM(CSP.AMOUNT), 0) AS TotalReplenishment
FROM OB_CUSTOMERSHIPMENT AS CS,
  OB_CUSTOMERSHIPMENTPOSITION AS CSP,
  OB_CUSTOMERORDER AS CO
WHERE CS.ID = CSP.SHIPMENT_ID
      AND CS.ORDER_ID = CO.ID
      AND CS.STATE = 550
      AND CO.DELIVERY_DATE = ''
GROUP BY CO.DELIVERY_DATE;

/*ReadyToPick*/
SELECT coalesce(SUM(POP.AMOUNT), 0) AS ReadyToPick
FROM OB_PICKINGORDERPOSITION POP
WHERE POP.PICKINGORDER_ID IS NULL
      AND POP.CUSTOMERSHIPMENTPOSITION_ID
          IN (SELECT CSP.ID
              FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
              WHERE CS.DELIVERY_DATE = '');

/*PickingNotYetPicked*/
SELECT coalesce(SUM(POP.AMOUNT - POP.AMOUNT_PICKED), 0) AS PickingNotYetPicked
FROM OB_PICKINGORDERPOSITION POP
  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
WHERE PO.STATE < 600
      AND POP.CUSTOMERSHIPMENTPOSITION_ID
          IN (SELECT CSP.ID
              FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
              WHERE CS.DELIVERY_DATE = '2017-08-15 12:00:00')
      AND POP.PICKINGORDER_ID IS NOT NULL;


/*PickingNotYetPicked    new    2017.08.08  */
SELECT coalesce(SUM(CSP.AMOUNT - CSP.AMOUNT_PICKED), 0) AS PickingNotYetPicked
FROM
  OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
WHERE CS.DELIVERY_DATE = '2017-09-02 13:30:00'
      AND CSP.STATE <= '600';


/*更新 PickingPicked*/
SELECT coalesce(SUM(POP.AMOUNT_PICKED), 0) AS PickingPicked
FROM
  OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
  LEFT JOIN OB_PICKINGORDERPOSITION POP ON CSP.ID = POP.CUSTOMERSHIPMENTPOSITION_ID
WHERE
  CS.DELIVERY_DATE = '2017-09-02 00:00:00'
  AND POP.AMOUNT_PICKED > 0
  AND CSP.ID NOT IN
      (SELECT P.CUSTOMERSHIPMENTPOSITION_ID
       FROM OB_PICKINGORDERPOSITION P);

# /*更新 PickingPicked 2017.8.7  new
SELECT coalesce(SUM(POP.AMOUNT_PICKED), 0) AS PickingPicked
FROM
  OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
  LEFT JOIN OB_PICKINGORDERPOSITION POP ON CSP.ID = POP.CUSTOMERSHIPMENTPOSITION_ID
WHERE
  CS.DELIVERY_DATE = '2017-09-02 00:00:00'
  AND POP.AMOUNT_PICKED > 0
  AND CSP.ID NOT IN
      (SELECT P.CUSTOMERSHIPMENTPOSITION_ID
       FROM OB_PICKINGORDERPOSITION P);

# 1.# /*更新 PickingPicked 2017.8.8  new new
SELECT coalesce(SUM(CSP.AMOUNT_PICKED), 0) AS PickingPicked
FROM
  OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
WHERE
  CS.DELIVERY_DATE = '2017-08-10 12:00:00'
  AND CSP.STATE <= '600';

# 2.
SELECT coalesce(SUM(POP.AMOUNT_PICKED), 0) AS PickingPicked
FROM
  OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
  LEFT JOIN OB_PICKINGORDERPOSITION POP ON CSP.ID = POP.CUSTOMERSHIPMENTPOSITION_ID
WHERE
  CS.DELIVERY_DATE = '2017-09-02 00:00:00'
  AND POP.AMOUNT_PICKED > 0
  AND CS.STATE = '600';
#   AND CSP.ID NOT IN
#       (SELECT P.CUSTOMERSHIPMENTPOSITION_ID
#        FROM OB_PICKINGORDERPOSITION P);



/*PickingPicked*/
SELECT coalesce(SUM(POP.AMOUNT_PICKED), 0) AS PickingPicked
FROM OB_PICKINGORDERPOSITION POP
WHERE POP.CUSTOMERSHIPMENTPOSITION_ID
      IN (SELECT CSP.ID
          FROM OB_CUSTOMERSHIPMENTPOSITION CSP
            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
          WHERE CS.DELIVERY_DATE = '2017-09-02 00:00:00'
                AND CS.STATE IN ('500', '600'));

/*Rebatched*/
SELECT coalesce(SUM(POP.AMOUNT_PICKED), 0) AS Rebatched
FROM OB_PICKINGORDERPOSITION POP
WHERE POP.CUSTOMERSHIPMENTPOSITION_ID
      IN (SELECT CSP.ID
          FROM OB_CUSTOMERSHIPMENTPOSITION CSP
            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
          WHERE CS.DELIVERY_DATE = '2017-08-16 03:00:00'
                AND CS.STATE = 610);

/*RebinBuffer*/
SELECT coalesce(SUM(RBP.AMOUNT), 0) AS RebinBuffer
FROM OB_REBINREQUESTPOSITION RBP
WHERE RBP.STATE = 'RAW'
      #   RBP.AMOUNT_REBINED = 0
      AND RBP.CUSTOMERSHIPMENTPOSITION_ID
          IN (SELECT CSP.ID
              FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
              WHERE CS.STATE = 620
                    AND CS.DELIVERY_DATE = '2017-08-16 03:00:00')
GROUP BY RBP.CUSTOMERSHIPMENTPOSITION_ID, RBP.ID;

/*Rebined*/
SELECT (CASE
        WHEN (a.Rebined1 - b.Rebined2) < 0
          THEN 0
        ELSE (a.Rebined1 - b.Rebined2)
        END) AS Rebined
FROM (SELECT coalesce(SUM(RP.AMOUNT), 0) AS Rebined1
      FROM OB_REBINREQUESTPOSITION RP
      WHERE RP.CUSTOMERSHIPMENTPOSITION_ID
            IN (SELECT CSP.ID
                FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
                WHERE CS.DELIVERY_DATE = '2017-07-05 15:00:00')) a,
  (SELECT coalesce(SUM(AMOUNT), 0) AS Rebined2
   FROM OB_PACKINGREQUESTPOSITION
   WHERE CUSTOMERSHIPMENTPOSITION_ID
         IN (SELECT CSP.ID
             FROM OB_CUSTOMERSHIPMENTPOSITION CSP
               LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
             WHERE CS.DELIVERY_DATE = '2017-07-05 15:00:00')) b;

# Rebined new
SELECT
  COALESCE(SUM(RBP.AMOUNT_REBINED), 0) AS Rebined,
  POP.ID                               AS id
FROM OB_REBINREQUESTPOSITION RBP
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION POP ON RBP.CUSTOMERSHIPMENTPOSITION_ID = POP.ID
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID
WHERE RBP.STATE = 'FINISHED'
      #       AND CS.STATE >= 610
      AND (POP.STATE = 630 OR POP.STATE = '620')
      AND CS.DELIVERY_DATE = '2017-09-01 00:00:00'
GROUP BY id;

# Rebined new new  new 2017.8.7
SELECT
  COALESCE(SUM(RBP.AMOUNT_REBINED), 0) AS Rebined,
  POP.ID                               AS id,
  RBP.STATE                            AS state,
  POP.STATE                            AS state
FROM OB_CUSTOMERSHIPMENTPOSITION POP
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID
  LEFT JOIN OB_REBINREQUESTPOSITION RBP ON POP.ID = RBP.CUSTOMERSHIPMENTPOSITION_ID
WHERE
  (POP.STATE = '620' OR CS.STATE = '630')
  AND CS.DELIVERY_DATE = '2017-09-01 00:00:00'
  AND RBP.STATE = 'FINISHED'
GROUP BY id;

# Rebined new new  new 2017.8.8
SELECT
  COALESCE(SUM(RBP.AMOUNT_REBINED), 0) AS Rebined,
  POP.ID                               AS id,
  RBP.STATE                            AS state,
  POP.STATE                            AS state
FROM OB_CUSTOMERSHIPMENTPOSITION POP
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID
  LEFT JOIN OB_REBINREQUESTPOSITION RBP ON POP.ID = RBP.CUSTOMERSHIPMENTPOSITION_ID
WHERE
  (POP.STATE = '620' OR CS.STATE = '630')
  AND CS.DELIVERY_DATE = '2017-09-01 00:00:00'
  AND RBP.STATE = 'FINISHED'
GROUP BY id;

# Rebined new new  new  new new 2017.8.8
SELECT COALESCE(SUM(RBP.AMOUNT_REBINED), 0) AS Rebined
#        POP.ID AS id,
#        RBP.STATE as  state,
#        POP.STATE as state
FROM OB_CUSTOMERSHIPMENTPOSITION POP
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID
  LEFT JOIN OB_REBINREQUESTPOSITION RBP ON POP.ID = RBP.CUSTOMERSHIPMENTPOSITION_ID
WHERE
  CS.ID NOT IN (SELECT CS.ID
                FROM OB_CUSTOMERSHIPMENT CS
                WHERE CS.STATE = '1100')
  AND (POP.STATE = '620' OR CS.STATE = '630')
  AND CS.DELIVERY_DATE = '2017-09-02 00:00:00'
  AND RBP.STATE = 'FINISHED';
# GROUP BY id;

# RebinBuffer new new  new  new new 2017.8.9 未用
SELECT COALESCE(SUM(POP.AMOUNT - POP.AMOUNT_REBINED), 0) AS Rebined
FROM OB_CUSTOMERSHIPMENTPOSITION POP
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID
WHERE CS.DELIVERY_DATE = '2017-09-01 00:00:00';

# Rebined new new  new  new new 2017.8.9 未用
SELECT COALESCE(SUM(amount), 0) AS Rebined
FROM (

       SELECT COALESCE(SUM(POP.AMOUNT_REBINED), 0) AS amount
       FROM OB_CUSTOMERSHIPMENTPOSITION POP
         LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID
         LEFT JOIN OB_PICKINGORDERPOSITION P ON POP.ID = P.CUSTOMERSHIPMENTPOSITION_ID
         LEFT JOIN OB_PICKINGORDER PO ON P.PICKINGORDER_ID = PO.ID
       WHERE CS.DELIVERY_DATE = '2017-08-16 03:00:00'
             AND PO.STATE = '700'
             AND CS.ID NOT IN (SELECT CS.ID
                               FROM OB_CUSTOMERSHIPMENT CS
                               WHERE CS.STATE = '1100')

       UNION ALL

       SELECT COALESCE(SUM(POP.AMOUNT_REBINED), 0) AS amount
       FROM OB_CUSTOMERSHIPMENTPOSITION POP
         LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID
       WHERE CS.DELIVERY_DATE = '2017-08-16 03:00:00') A;

#  Rebined  2017.8.9  下午  最新版
SELECT COALESCE(SUM(amount), 0) AS Rebined
FROM (;
(SELECT COALESCE(SUM(POP.AMOUNT_REBINED), 0) AS amount
 FROM OB_CUSTOMERSHIPMENTPOSITION POP
   LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID
 WHERE
   CS.DELIVERY_DATE = '2017-08-23 02:00:00'
   AND POP.ID IN (
     SELECT DISTINCT POP.ID
     FROM OB_CUSTOMERSHIPMENTPOSITION POP
       LEFT JOIN OB_PICKINGORDERPOSITION P ON POP.ID = P.CUSTOMERSHIPMENTPOSITION_ID
       LEFT JOIN OB_PICKINGORDER PO ON P.PICKINGORDER_ID = PO.ID

     WHERE PO.STATE = '700'
           AND CS.STATE = '630'
   )
   AND CS.ID NOT IN (SELECT CS.ID
                     FROM OB_CUSTOMERSHIPMENT CS
                     WHERE CS.STATE = '1100'));
UNION ALL
;
(SELECT COALESCE(SUM(POP.AMOUNT_REBINED), 0) AS amount
 FROM OB_CUSTOMERSHIPMENTPOSITION POP
   LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID
 WHERE
   CS.DELIVERY_DATE = '2017-08-23 02:00:00'

   AND POP.ID IN (
     SELECT DISTINCT POP.ID
     FROM OB_CUSTOMERSHIPMENTPOSITION POP
       LEFT JOIN OB_PICKINGORDERPOSITION P ON POP.ID = P.CUSTOMERSHIPMENTPOSITION_ID
       LEFT JOIN OB_PICKINGORDER PO ON P.PICKINGORDER_ID = PO.ID
       LEFT JOIN OB_REBINREQUESTPOSITION RBP ON POP.ID = RBP.CUSTOMERSHIPMENTPOSITION_ID
     WHERE PO.STATE <> '700'
     #                   AND RBP.STATE <> 'RAW'

   )
);
) A;


SELECT DISTINCT COALESCE(SUM(CSP.AMOUNT_REBINED), 0) AS Rebined

FROM OB_REBINREQUESTPOSITION RBP
  LEFT JOIN OB_REBINREQUEST RQ ON RBP.REBINREQUEST_ID = RQ.ID
  LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID
  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON RBP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
WHERE 1 = 1
      AND ((PO.STATE <> '700') OR (PO.STATE = '700' AND CS.ID NOT IN (SELECT CS.ID
                                                                      FROM OB_CUSTOMERSHIPMENT CS
                                                                      WHERE CS.STATE = '1100')))
      AND CS.DELIVERY_DATE = '2017-08-23 02:00:00'
      AND RBP.STATE <> 'RAW';


#rebin Buffer  2017.08.10 最新版
SELECT COALESCE(sum(POP.AMOUNT - POP.AMOUNT_REBINED), 0) AS amount
FROM OB_CUSTOMERSHIPMENTPOSITION POP
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID
WHERE CS.DELIVERY_DATE = '2017-09-02 00:00:00'
      AND POP.STATE = '620'
      AND POP.ID NOT IN
          (SELECT RBP.CUSTOMERSHIPMENTPOSITION_ID
           FROM OB_REBINREQUESTPOSITION RBP
           WHERE RBP.STATE = 'LOSE');
#   GROUP BY CS.ID,CS.STATE;


#rebined
SELECT COALESCE(SUM(POP.AMOUNT_REBINED), 0) AS amount
FROM OB_CUSTOMERSHIPMENTPOSITION POP
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID
WHERE CS.DELIVERY_DATE = '2017-09-02 00:00:00';






/*更新 ScanVerify*/
SELECT
  coalesce(SUM(CSP.AMOUNT), 0) AS ScanVerify,
  CS.STATE
FROM OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_PACKINGREQUEST PR ON CS.ID = PR.CUSTOMERSHIPMENT_ID
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
WHERE
  #PR.WEIGHT IS NOT NULL AND
  CS.DELIVERY_DATE = '2017-08-16 03:00:00'
  #   AND CS.STATE >= 600
  AND CS.STATE = 640
GROUP BY CS.STATE;


/*ScanVerify*/
SELECT coalesce(SUM(PRP.AMOUNT), 0) AS ScanVerify
FROM OB_PACKINGREQUEST PR
  LEFT JOIN OB_PACKINGREQUESTPOSITION PRP ON PR.ID = PRP.PACKINGREQUEST_ID
WHERE PRP.CUSTOMERSHIPMENTPOSITION_ID
      IN (SELECT CSP.ID
          FROM OB_CUSTOMERSHIPMENT CS
            LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
          WHERE CS.STATE = '640'
                AND CS.DELIVERY_DATE = '');

/*Packed*/
SELECT coalesce(SUM(CSP.AMOUNT), 0) AS Packed
FROM OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
WHERE CS.STATE = '650'
      AND CS.DELIVERY_DATE = '';


/* 2017.8.8 new  new 更新 Problem*/
SELECT DISTINCT coalesce(SUM(CSP.AMOUNT), 0) AS Problem
FROM OBP_OBPROBLEM OB
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON OB.SHIPMENT_ID = CS.ID
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
  LEFT JOIN OB_PICKINGORDERPOSITION POP ON CSP.ID = POP.CUSTOMERSHIPMENTPOSITION_ID
  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
WHERE OB.STATE = 'unsolved'
      AND PO.STATE = '700'
      AND CS.DELIVERY_DATE = '2017-09-01 00:00:00';

/* 更新 Problem*/
SELECT coalesce(SUM(CSP.AMOUNT), 0) AS Problem
FROM OBP_OBPROBLEM OB
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON OB.SHIPMENT_ID = CS.ID
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
WHERE OB.STATE = 'unsolved'
      AND CS.DELIVERY_DATE = '2017-09-01 00:00:00';

/* 更新 Problem*/
SELECT CS.SHIPMENT_NO AS shipmentID
FROM OBP_OBPROBLEM OB
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON OB.SHIPMENT_ID = CS.ID
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
WHERE OB.STATE = 'unsolved'
      AND CS.DELIVERY_DATE = '2017-09-01 00:00:00';


/*12. 更新 Problem 明细*/
SELECT DISTINCT
  A.SHIPMENT_NO     AS shipmentID,
  A.ORDER_NO        AS orderID,
  A.NAME            AS boxType,
  A.SKU_NO          AS SKUNO,
  A.ITEM_NO         AS SKUID,
  A.AMOUNT          AS quality,
  A.DELIVERY_DATE   AS planDepartTime,
  A.SHIPMENT_NO     AS stockPosition1,
  A.SORT_CODE       AS stockPosition2,
  'Problem'         AS workFlowStatus,
  A.PPNAME          AS ppName,
  A.PICKINGORDER_NO AS batchNo
FROM
  (SELECT DISTINCT
     CS.ID,
     CS.SHIPMENT_NO,
     CO.ORDER_NO,
     BT.NAME,
     ITD.SKU_NO,
     ITD.ITEM_NO,
     CSP.AMOUNT,
     CS.DELIVERY_DATE,
     CS.SORT_CODE,
     B.NAME AS PPNAME,
     B.PICKINGORDER_NO
   FROM OB_CUSTOMERSHIPMENT CS
     LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
     LEFT JOIN (SELECT DISTINCT
                  POP.CUSTOMERSHIPMENTPOSITION_ID,
                  PO.PICKINGORDER_NO,
                  PP.NAME
                FROM OB_PICKINGORDER PO
                  LEFT JOIN OB_PICKINGORDERPOSITION POP ON PO.ID = POP.PICKINGORDER_ID
                  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID) B
       ON CSP.ID = B.CUSTOMERSHIPMENTPOSITION_ID
     LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID
     LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID
     LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID
   WHERE CS.DELIVERY_DATE = '2017-09-01 00:00:00') A
  LEFT JOIN OBP_OBPROBLEM AO
    ON A.ID = AO.SHIPMENT_ID
WHERE AO.STATE = 'unsolved';
      AND A.PPNAME = '';


/*Problem  test  new new 2017.8.9*/
SELECT DISTINCT coalesce(SUM(CSP.AMOUNT), 0) AS Problem
#   CSP.SHIPMENT_ID,
#   CS.state,
# #   PC.CUSTOMERSHIPMENTPOSITION_ID,
#   CSP.AMOUNT
FROM OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
WHERE
  CS.DELIVERY_DATE = '2017-08-25 02:00:00'
  AND CSP.ID
      IN (SELECT DISTINCT CSP.ID
          FROM OB_CUSTOMERSHIPMENTPOSITION CSP
            LEFT JOIN OB_PICKINGORDERPOSITION PC ON CSP.ID = PC.CUSTOMERSHIPMENTPOSITION_ID
            LEFT JOIN OB_PICKINGORDER PR ON PC.PICKINGORDER_ID = PR.ID
          WHERE CS.STATE = '1100'
        #           and PR.STATE = '700'
      );
GROUP BY CSP.SHIPMENT_ID, CS.state, CSP.AMOUNT;
PC.CUSTOMERSHIPMENTPOSITION_ID;


SELECT DISTINCT
  coalesce(SUM(CSP.AMOUNT), 0) AS Problem,
  CSP.ID,
  CSP.AMOUNT
FROM OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID

  LEFT JOIN OB_PICKINGORDERPOSITION PC ON CSP.ID = PC.CUSTOMERSHIPMENTPOSITION_ID
  LEFT JOIN OB_PICKINGORDER PR ON PC.PICKINGORDER_ID = PR.ID
WHERE
  CS.DELIVERY_DATE = '2017-09-01 00:00:00'
  AND CS.STATE = '1100'
  AND PR.STATE = '700'
GROUP BY CSP.ID;



/*Sorted*/
SELECT coalesce(SUM(CSP.AMOUNT), 0) AS Sorted
FROM OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
WHERE CS.STATE = '660'
      AND CS.DELIVERY_DATE = '';

/*loaded*/
SELECT coalesce(SUM(CSP.AMOUNT), 0) AS Loaded
FROM OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
WHERE CS.STATE = '670'
      AND CS.DELIVERY_DATE = '2017-09-06 00:00:00';








