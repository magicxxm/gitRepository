/*total*/
SELECT
  PP.NAME                      AS name,
  coalesce(SUM(POP.AMOUNT), 0) AS amount,
  POP.ID
FROM OB_PICKINGORDERPOSITION POP
  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
  LEFT JOIN OB_PROCESSPATH PP ON PP.ID = PO.PROCESSPATH_ID
WHERE POP.PICKINGORDER_ID IS NOT NULL
      AND POP.CUSTOMERSHIPMENTPOSITION_ID
          IN (SELECT CSP.ID
              FROM OB_CUSTOMERSHIPMENT CS
                LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
              WHERE CS.DELIVERY_DATE = '2017-08-10 12:00:00')
#       AND POP.CUSTOMERSHIPMENTPOSITION_ID
#           NOT IN (SELECT coalesce(SUM(CSP.AMOUNT_PICKED), 0) AS amount
#                   FROM OB_CUSTOMERSHIPMENTPOSITION CSP
#                     LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
#                   WHERE CS.STATE = '1000' OR CS.STATE = '800')
GROUP BY PP.NAME, POP.ID;

# new total
SELECT
  PP.NAME                      AS name,
  coalesce(SUM(POP.AMOUNT), 0) AS amount
FROM OB_PICKINGORDERPOSITION POP
  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
  LEFT JOIN OB_PROCESSPATH PP ON PP.ID = PO.PROCESSPATH_ID
WHERE POP.PICKINGORDER_ID IS NOT NULL
      AND POP.CUSTOMERSHIPMENTPOSITION_ID
          NOT IN (SELECT coalesce(SUM(CSP.AMOUNT_PICKED), 0) AS amount
                  FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                    LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
                  WHERE CS.STATE = '1000' OR CS.STATE = '800')
      AND POP.CUSTOMERSHIPMENTPOSITION_ID
          IN (SELECT CSP.ID
              FROM OB_CUSTOMERSHIPMENT CS
                LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
              WHERE CS.DELIVERY_DATE = '2017-08-15 12:00:00')
      AND POP.CUSTOMERSHIPMENTPOSITION_ID
GROUP BY PP.NAME;


/*Ready To Pick*/
SELECT
  PP.NAME                      AS NAME,
  COALESCE(SUM(POP.AMOUNT), 0) AS amount
FROM OB_PICKINGORDERPOSITION POP
  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
  LEFT JOIN OB_PICKINGCATEGORY PC ON CS.PICKINGCATEGORY_ID = PC.ID
  LEFT JOIN OB_PROCESSPATH PP ON PC.PROCESSPATH_ID = PP.ID
  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID
  LEFT JOIN MD_ITEMDATA ITD ON POP.ITEMDATA_ID = ITD.ID
  LEFT JOIN INV_STOCKUNIT ST ON POP.PICKFROMSTOCKUNIT_ID = ST.ID
  LEFT JOIN INV_UNITLOAD UL ON ST.UNITLOAD_ID = UL.ID
  LEFT JOIN MD_STORAGELOCATION SL ON UL.STORAGELOCATION_ID = SL.ID
WHERE POP.STATE = '200'
      AND POP.CUSTOMERSHIPMENTPOSITION_ID
          IN (SELECT CSP.ID
              FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
              WHERE CS.DELIVERY_DATE = :exsdTime)
GROUP BY PP.NAME;

/*Picking Not Yet Picked*/
SELECT
  PP.NAME                                          AS NAME,
  COALESCE(SUM(POP.AMOUNT - POP.AMOUNT_PICKED), 0) AS amount
FROM OB_PICKINGORDERPOSITION POP
  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
  LEFT JOIN OB_PROCESSPATH PP ON PP.ID = PO.PROCESSPATH_ID
WHERE PO.STATE < 600
      AND POP.CUSTOMERSHIPMENTPOSITION_ID
          IN (SELECT CSP.ID
              FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
              WHERE CS.DELIVERY_DATE = '2017-08-15 12:00:00')
      AND POP.PICKINGORDER_ID IS NOT NULL
GROUP BY PP.NAME;

/* PickingPicked */
SELECT
  PP.NAME                             AS name,
  coalesce(sum(POP.AMOUNT_PICKED), 0) AS amount
FROM OB_PICKINGORDERPOSITION POP
  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
  LEFT JOIN OB_PROCESSPATH PP ON PP.ID = PO.PROCESSPATH_ID
WHERE POP.CUSTOMERSHIPMENTPOSITION_ID
      IN (SELECT CSP.ID
          FROM OB_CUSTOMERSHIPMENTPOSITION CSP
            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
          WHERE CS.DELIVERY_DATE = :exsdTime
                AND CS.STATE IN ('500', '600'))
GROUP BY PP.NAME;

/*Rebatched*/
SELECT
  PP.NAME                      AS name,
  coalesce(sum(POP.AMOUNT), 0) AS amount
FROM OB_PICKINGORDERPOSITION POP
  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
  LEFT JOIN OB_PROCESSPATH PP ON PP.ID = PO.PROCESSPATH_ID
WHERE POP.CUSTOMERSHIPMENTPOSITION_ID
      IN (SELECT CSP.ID
          FROM OB_CUSTOMERSHIPMENTPOSITION CSP
            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
          WHERE CS.DELIVERY_DATE = :exsdTime
                AND CS.STATE = 610)
GROUP BY PP.NAME;

/* Rebin Buffer */
SELECT
  PP.NAME                      AS name,
  coalesce(sum(POP.AMOUNT), 0) AS amount
FROM OB_PICKINGORDERPOSITION POP
  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
  LEFT JOIN OB_PROCESSPATH PP ON PP.ID = PO.PROCESSPATH_ID
WHERE POP.CUSTOMERSHIPMENTPOSITION_ID
      IN (SELECT CSP.ID
          FROM OB_CUSTOMERSHIPMENTPOSITION CSP
            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
          WHERE CS.DELIVERY_DATE = '2017-08-16 03:00:00'
                AND CSP.STATE = 620)
GROUP BY PP.NAME;

# rebined buffer 2017.8.7
SELECT
  PP.NAME                      AS name,
  coalesce(SUM(RBP.AMOUNT), 0) AS amount
FROM OB_REBINREQUESTPOSITION RBP
  LEFT JOIN OB_REBINREQUEST RQ ON RBP.REBINREQUEST_ID = RQ.ID
  LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID
  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON RBP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID
  LEFT JOIN MD_ITEMDATA ITD ON RBP.ITEMDATA_ID = ITD.ID
  LEFT JOIN OB_REBINWALL RW ON RBP.REBINWALL_ID = RW.ID
WHERE RBP.AMOUNT_REBINED = 0
      AND RBP.CUSTOMERSHIPMENTPOSITION_ID
          IN (SELECT CSP.ID
              FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
              WHERE CSP.STATE = 620
                    AND CS.DELIVERY_DATE = '2017-08-16 03:00:00')
GROUP BY name;



/* Rebined */
SELECT
  C.ppName                    AS name,
  coalesce(SUM(C.quality), 0) AS amount
FROM (SELECT
        A.shipmentID,
        A.orderID,
        A.boxType,
        A.SKUNO,
        A.SKUID,
        coalesce((CASE WHEN (A.quality1 - B.AMOUNT) IS NULL
          THEN A.quality1
                  ELSE (A.quality1 - B.AMOUNT) END), 0) AS quality,
        A.planDepartTime,
        A.stockPosition1,
        A.workFlowStatus,
        A.ppName,
        A.batchNo
      FROM
        (SELECT
           CS.SHIPMENT_NO                        AS shipmentID,
           CS.CUSTOMER_NAME                      AS orderID,
           BT.NAME                               AS boxType,
           ITD.SKU_NO                            AS SKUNO,
           ITD.ITEM_NO                           AS SKUID,
           coalesce((SUM(RBP.AMOUNT)), 0)        AS quality1,
           CS.DELIVERY_DATE                      AS planDepartTime,
           concat(RW.NAME, RBP.REBINTOCELL_NAME) AS stockPosition1,
           'Rebined'                             AS workFlowStatus,
           PP.NAME                               AS ppName,
           PO.PICKINGORDER_NO                    AS batchNo
         FROM OB_REBINREQUESTPOSITION RBP
           LEFT JOIN OB_REBINREQUEST RQ ON RBP.REBINREQUEST_ID = RQ.ID
           LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID
           LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID
           LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON RBP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID
           LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
           LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID
           LEFT JOIN MD_ITEMDATA ITD ON RBP.ITEMDATA_ID = ITD.ID
           LEFT JOIN OB_REBINWALL RW ON RBP.REBINWALL_ID = RW.ID
         WHERE RBP.AMOUNT > 0
               AND RBP.CUSTOMERSHIPMENTPOSITION_ID
                   IN (SELECT CSP.ID
                       FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                         LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
                       WHERE CS.DELIVERY_DATE = '2017-08-16 03:00:00')
         GROUP BY shipmentID, orderID, boxType, SKUNO, SKUID, planDepartTime, stockPosition1,
           workFlowStatus, ppName, batchNo) A LEFT JOIN
        (SELECT
           CS.SHIPMENT_NO,
           ITD.SKU_NO,
           coalesce((SUM(PRP.AMOUNT)), 0) AS AMOUNT
         FROM OB_PACKINGREQUESTPOSITION PRP
           LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON PRP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID
           LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
           LEFT JOIN MD_ITEMDATA ITD ON PRP.ITEMDATA_ID = ITD.ID
         WHERE PRP.CUSTOMERSHIPMENTPOSITION_ID
               IN (SELECT CSP.ID
                   FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                     LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
                   WHERE CS.DELIVERY_DATE = '2017-08-16 03:00:00')
         GROUP BY SHIPMENT_NO, SKU_NO) B
          ON A.shipmentID = B.SHIPMENT_NO
             AND A.SKUNO = B.SKU_NO) C
WHERE C.quality != 0
GROUP BY C.ppName;

#rebined new new 2017.8.7
SELECT
  C.ppName                    AS name,
  coalesce(SUM(C.quality), 0) AS amount
FROM
  (SELECT DISTINCT
     CS.SHIPMENT_NO                        AS shipmentID,
     CS.CUSTOMER_NAME                      AS orderID,
     BT.NAME                               AS boxType,
     ITD.SKU_NO                            AS SKUNO,
     ITD.ITEM_NO                           AS SKUID,
     coalesce((SUM(RBP.AMOUNT)), 0)        AS quality,
     CS.DELIVERY_DATE                      AS planDepartTime,
     concat(RW.NAME, RBP.REBINTOCELL_NAME) AS stockPosition1,
     ''                                    AS stockPosition2,
     'Rebined'                             AS workFlowStatus,
     PP.NAME                               AS ppName,
     PO.PICKINGORDER_NO                    AS batchNo
   FROM OB_REBINREQUESTPOSITION RBP
     LEFT JOIN OB_REBINREQUEST RQ ON RBP.REBINREQUEST_ID = RQ.ID
     LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID
     LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID
     LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON RBP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID
     LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
     LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID
     LEFT JOIN MD_ITEMDATA ITD ON RBP.ITEMDATA_ID = ITD.ID
     LEFT JOIN OB_REBINWALL RW ON RBP.REBINWALL_ID = RW.ID
   WHERE (CSP.STATE = '620' OR CS.STATE = '630')
         AND CS.DELIVERY_DATE = '2017-08-16 03:00:00'
         AND RBP.STATE = 'FINISHED'
   GROUP BY shipmentID, orderID, boxType, SKUNO, SKUID, planDepartTime, stockPosition1,
     workFlowStatus, PP.NAME, PO.PICKINGORDER_NO) C
WHERE C.quality != 0
GROUP BY C.ppName;


/* Scan Verify */
SELECT
  PP.NAME                      AS name,
  coalesce(SUM(PRP.AMOUNT), 0) AS amount
FROM OB_PACKINGREQUEST PR
  LEFT JOIN OB_PACKINGREQUESTPOSITION PRP ON PR.ID = PRP.PACKINGREQUEST_ID
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON PRP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID
  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID
  LEFT JOIN MD_ITEMDATA ITD ON PRP.ITEMDATA_ID = ITD.ID
  LEFT JOIN OB_REBINCUSTOMERSHIPMENT RCS ON CS.ID = RCS.CUSTOMERSHIPMENT_ID
  LEFT JOIN OB_REBINREQUEST RQ ON RCS.REBINREQUEST_ID = RQ.ID
  LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID
  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID
  LEFT JOIN OB_REBINWALL RW1 ON RQ.REBINWALL1_ID = RW1.ID
  LEFT JOIN OB_REBINWALL RW2 ON RQ.REBINWALL2_ID = RW2.ID
WHERE PRP.CUSTOMERSHIPMENTPOSITION_ID
      IN (SELECT CSP.ID
          FROM OB_CUSTOMERSHIPMENT CS
            LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
          WHERE CS.STATE = '640'
                AND CS.DELIVERY_DATE = :exsdTime)
GROUP BY PP.NAME;

/* Packed */
SELECT
  B.NAME                       AS name,
  coalesce(SUM(CSP.AMOUNT), 0) AS amount
FROM OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_PICKINGCATEGORY PC ON CS.PICKINGCATEGORY_ID = PC.ID
  LEFT JOIN OB_PROCESSPATH PP ON PC.PROCESSPATH_ID = PP.ID
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
  LEFT JOIN
  (SELECT DISTINCT
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
WHERE CS.DELIVERY_DATE = :exsdTime
      AND CS.STATE = '650'
GROUP BY B.NAME;

/* Problem */
SELECT
  A.PPNAME                   AS name,
  coalesce(SUM(A.AMOUNT), 0) AS amount
# FROM ANDONOUTBOUND AO
FROM OBP_OBPROBLEM AO
  LEFT JOIN OB_CUSTOMERSHIPMENT CSM ON AO.SHIPMENT_ID = CSM.ID
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSMP ON CSM.ID = CSMP.SHIPMENT_ID
  LEFT JOIN
  (SELECT
     CSP.ID,
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
     LEFT JOIN
     (SELECT DISTINCT
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
   WHERE CS.DELIVERY_DATE = '2017-08-16 03:00:00') A
  #     ON AO.CUSTOMERSHIPMENTPOSITION_ID = A.ID
    ON CSMP.ID = A.ID
WHERE A.AMOUNT > 0
GROUP BY A.PPNAME;

/* Sorted */
SELECT
  B.NAME                       AS name,
  coalesce(SUM(CSP.AMOUNT), 0) AS amount
FROM OB_CUSTOMERSHIPMENT CS
  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID
  LEFT JOIN
  (SELECT DISTINCT
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
WHERE CS.DELIVERY_DATE = :exsdTime
      AND CS.STATE = '660'
GROUP BY B.NAME;


SELECT
  PP.NAME                                          AS NAME,
  COALESCE(SUM(POP.AMOUNT - POP.AMOUNT_PICKED), 0) AS amount
FROM OB_PICKINGORDERPOSITION POP
  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID
  LEFT JOIN OB_PROCESSPATH PP ON PP.ID = PO.PROCESSPATH_ID
WHERE PO.STATE < 600
      AND POP.CUSTOMERSHIPMENTPOSITION_ID
          IN (SELECT CSP.ID
              FROM OB_CUSTOMERSHIPMENTPOSITION CSP
                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID
            #               WHERE CS.DELIVERY_DATE = :exsdTime
          )
      AND POP.PICKINGORDER_ID IS NOT NULL
GROUP BY PP.NAME;