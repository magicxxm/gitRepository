package com.mushiny.wms.report.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.report.domain.DeliveryTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryTimeRepository extends BaseRepository<DeliveryTime, String> {

    /*4. ReadyToPick  合计*/
    @Query(nativeQuery = true, value = " SELECT coalesce(sum(POP.AMOUNT), 0) AS ReadyToPick " +
            "FROM OB_PICKINGORDERPOSITION POP " +
            "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
            "  LEFT JOIN OB_PICKINGCATEGORY PC ON CS.PICKINGCATEGORY_ID = PC.ID " +
            "  LEFT JOIN OB_PROCESSPATH PP ON PC.PROCESSPATH_ID = PP.ID " +
            "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
            "  LEFT JOIN MD_ITEMDATA ITD ON POP.ITEMDATA_ID = ITD.ID " +
            "  LEFT JOIN INV_STOCKUNIT ST ON POP.PICKFROMSTOCKUNIT_ID = ST.ID " +
            "  LEFT JOIN INV_UNITLOAD UL ON ST.UNITLOAD_ID = UL.ID " +
            "  LEFT JOIN MD_STORAGELOCATION SL ON UL.STORAGELOCATION_ID = SL.ID " +
            "WHERE POP.STATE = '200' " +
            "      AND POP.CUSTOMERSHIPMENTPOSITION_ID " +
            "          IN (SELECT CSP.ID " +
            "              FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
            "                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
            "              WHERE CS.DELIVERY_DATE = :exsdTime) AND PP.NAME = :ppName ")
    BigDecimal getByReadyToPick(@Param("ppName") String ppName, @Param("exsdTime") LocalDateTime exsdTime);

    /*5. PickingNotYetPicked  合计*/
    @Query(nativeQuery = true, value = " SELECT coalesce(sum((POP.AMOUNT - POP.AMOUNT_PICKED)), 0) AS PickingNotYetPicked " +
            "FROM OB_PICKINGORDERPOSITION POP " +
            "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID " +
            "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
            "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
            "  LEFT JOIN MD_ITEMDATA ITD ON POP.ITEMDATA_ID = ITD.ID " +
            "  LEFT JOIN INV_STOCKUNIT ST ON POP.PICKFROMSTOCKUNIT_ID = ST.ID " +
            "  LEFT JOIN INV_UNITLOAD UL ON ST.UNITLOAD_ID = UL.ID " +
            "  LEFT JOIN MD_STORAGELOCATION SL ON UL.STORAGELOCATION_ID = SL.ID " +
            "WHERE PO.STATE < 600 " +
            "      AND POP.CUSTOMERSHIPMENTPOSITION_ID " +
            "          IN (SELECT CSP.ID " +
            "              FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
            "                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
            "              WHERE CS.DELIVERY_DATE = :exsdTime) " +
            "      AND POP.PICKINGORDER_ID IS NOT NULL " +
            "      AND (POP.AMOUNT - POP.AMOUNT_PICKED) > 0 " +
            "      AND PP.NAME = :ppName ")
    BigDecimal getByPickingNotYetPicked(@Param("ppName") String ppName, @Param("exsdTime") LocalDateTime exsdTime);


    /*6. PickingPicked  合计*/
    @Query(nativeQuery = true, value = " SELECT coalesce(sum(POP.AMOUNT_PICKED), 0) AS PickingPicked " +
            "FROM OB_PICKINGORDERPOSITION POP " +
            "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID " +
            "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
            "  LEFT JOIN OB_PICKINGUNITLOAD PUL ON POP.PICKTOUNITLOAD_ID = PUL.ID " +
            "  LEFT JOIN INV_UNITLOAD UL ON PUL.UNITLOAD_ID = UL.ID " +
            "  LEFT JOIN MD_STORAGELOCATION SL ON UL.STORAGELOCATION_ID = SL.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
            "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
            "  LEFT JOIN MD_ITEMDATA ITD ON POP.ITEMDATA_ID = ITD.ID " +
            "WHERE POP.CUSTOMERSHIPMENTPOSITION_ID " +
            "      IN (SELECT CSP.ID " +
            "          FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
            "            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
            "          WHERE CS.DELIVERY_DATE = :exsdTime " +
            "                AND CS.STATE IN ('500', '600')) " +
            "      AND POP.AMOUNT_PICKED > 0 " +
            "      AND PP.NAME = :ppName ")
    BigDecimal getByPickingPicked(@Param("ppName") String ppName, @Param("exsdTime") LocalDateTime exsdTime);


    /*7. Rebatched  合计*/
    @Query(nativeQuery = true, value = " SELECT coalesce(sum(POP.AMOUNT_PICKED), 0) AS Rebatched " +
            "FROM OB_PICKINGORDERPOSITION POP " +
            "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID " +
            "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
            "  LEFT JOIN OB_PICKINGUNITLOAD PUL ON POP.PICKTOUNITLOAD_ID = PUL.ID " +
            "  LEFT JOIN INV_UNITLOAD UL ON PUL.UNITLOAD_ID = UL.ID " +
            "  LEFT JOIN MD_STORAGELOCATION SL ON UL.STORAGELOCATION_ID = SL.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
            "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
            "  LEFT JOIN MD_ITEMDATA ITD ON POP.ITEMDATA_ID = ITD.ID " +
            "  LEFT JOIN OB_REBATCHREQUESTPOSITION RQP ON UL.ID = RQP.SOURCE_ID " +
            "  LEFT JOIN OB_REBATCHSLOT RSL ON RQP.REBATCHSLOT_ID = RSL.ID " +
            "WHERE POP.CUSTOMERSHIPMENTPOSITION_ID " +
            "      IN (SELECT CSP.ID " +
            "          FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
            "            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
            "          WHERE CS.DELIVERY_DATE = :exsdTime " +
            "                AND CS.STATE IN ('610')) " +
            "      AND PP.NAME = :ppName ")
    BigDecimal getByRebatched(@Param("ppName") String ppName, @Param("exsdTime") LocalDateTime exsdTime);


    /*8. RebinBuffer  合计*/
    @Query(nativeQuery = true, value = " SELECT coalesce(SUM(RBP.AMOUNT), 0) AS RebinBuffer " +
            "FROM OB_REBINREQUESTPOSITION RBP " +
            "  LEFT JOIN OB_REBINREQUEST RQ ON RBP.REBINREQUEST_ID = RQ.ID " +
            "  LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID " +
            "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON RBP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
            "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
            "  LEFT JOIN MD_ITEMDATA ITD ON RBP.ITEMDATA_ID = ITD.ID " +
            "  LEFT JOIN OB_REBINWALL RW ON RBP.REBINWALL_ID = RW.ID " +
            "WHERE RBP.AMOUNT_REBINED = 0 " +
            "      AND RBP.CUSTOMERSHIPMENTPOSITION_ID " +
            "          IN (SELECT CSP.ID " +
            "              FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
            "                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
            "              WHERE CSP.STATE = 620 " +
            "                    AND CS.DELIVERY_DATE = :exsdTime) " +
            "      AND PP.NAME = :ppName ")
    BigDecimal getByRebinBuffer(@Param("ppName") String ppName, @Param("exsdTime") LocalDateTime exsdTime);


    /*9. Rebined  合计*/
    @Query(nativeQuery = true, value = " SELECT coalesce(SUM(C.quality), 0) AS Rebined " +
            "FROM " +
            "  (SELECT  DISTINCT " +
            "  CS.SHIPMENT_NO                        AS shipmentID, " +
            "  CS.CUSTOMER_NAME                      AS orderID, " +
            "  BT.NAME                               AS boxType, " +
            "  ITD.SKU_NO                            AS SKUNO, " +
            "  ITD.ITEM_NO                           AS SKUID, " +
            "  coalesce((SUM(RBP.AMOUNT)), 0)        AS quality, " +
            "  CS.DELIVERY_DATE                      AS planDepartTime, " +
            "  concat(RW.NAME, RBP.REBINTOCELL_NAME) AS stockPosition1, " +
            "  ''                                    AS stockPosition2, " +
            "  'Rebined'                            AS workFlowStatus, " +
            "  PP.NAME                               AS ppName, " +
            "  PO.PICKINGORDER_NO                    AS batchNo " +
            "FROM OB_REBINREQUESTPOSITION RBP " +
            "  LEFT JOIN OB_REBINREQUEST RQ ON RBP.REBINREQUEST_ID = RQ.ID " +
            "  LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID " +
            "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON RBP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
            "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
            "  LEFT JOIN MD_ITEMDATA ITD ON RBP.ITEMDATA_ID = ITD.ID " +
            "  LEFT JOIN OB_REBINWALL RW ON RBP.REBINWALL_ID = RW.ID " +
            "WHERE (CSP.STATE = '620' OR CS.STATE = '630') " +
            "      AND CS.DELIVERY_DATE = :exsdTime " +
            "      and RBP.STATE = 'FINISHED' " +
            "GROUP BY shipmentID, orderID, boxType, SKUNO, SKUID, planDepartTime, stockPosition1, " +
            "  workFlowStatus, PP.NAME, PO.PICKINGORDER_NO ) C " +
            "WHERE C.quality != 0 " +
            "      AND C.ppName = :ppName ")
    BigDecimal getByRebined(@Param("ppName") String ppName, @Param("exsdTime") LocalDateTime exsdTime);


    /*10. ScanVerify  合计*/
    @Query(nativeQuery = true, value = " SELECT coalesce(SUM(PRP.AMOUNT), 0) AS ScanVerify " +
            "FROM OB_PACKINGREQUEST PR " +
            "  LEFT JOIN OB_PACKINGREQUESTPOSITION PRP ON PR.ID = PRP.PACKINGREQUEST_ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON PRP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
            "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
            "  LEFT JOIN MD_ITEMDATA ITD ON PRP.ITEMDATA_ID = ITD.ID " +
            "  LEFT JOIN OB_REBINCUSTOMERSHIPMENT RCS ON CS.ID = RCS.CUSTOMERSHIPMENT_ID " +
            "  LEFT JOIN OB_REBINREQUEST RQ ON RCS.REBINREQUEST_ID = RQ.ID " +
            "  LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID " +
            "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
            "  LEFT JOIN OB_REBINWALL RW1 ON RQ.REBINWALL1_ID = RW1.ID " +
            "  LEFT JOIN OB_REBINWALL RW2 ON RQ.REBINWALL2_ID = RW2.ID " +
            "WHERE PRP.CUSTOMERSHIPMENTPOSITION_ID " +
            "      IN (SELECT CSP.ID " +
            "          FROM OB_CUSTOMERSHIPMENT CS " +
            "            LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
            "          WHERE CS.STATE = '640' " +
            "                AND CS.DELIVERY_DATE = :exsdTime) " +
            "      AND PP.NAME = :ppName ")
    BigDecimal getByScanVerify(@Param("ppName") String ppName, @Param("exsdTime") LocalDateTime exsdTime);

    /*11. Packed  合计*/
    @Query(nativeQuery = true, value = " SELECT coalesce(SUM(A.AMOUNT), 0) AS Packed " +
            "FROM " +
            "  (SELECT " +
            "     CSP.ID, " +
            "     CS.SHIPMENT_NO, " +
            "     CO.ORDER_NO, " +
            "     BT.NAME, " +
            "     ITD.SKU_NO, " +
            "     ITD.ITEM_NO, " +
            "     CSP.AMOUNT, " +
            "     CS.DELIVERY_DATE, " +
            "     B.NAME            AS ppName, " +
            "     B.PICKINGORDER_NO AS batchNo " +
            "   FROM OB_CUSTOMERSHIPMENT CS " +
            "     LEFT JOIN OB_PICKINGCATEGORY PC ON CS.PICKINGCATEGORY_ID = PC.ID " +
            "     LEFT JOIN OB_PROCESSPATH PP ON PC.PROCESSPATH_ID = PP.ID " +
            "     LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
            "     LEFT JOIN " +
            "     (SELECT DISTINCT " +
            "        POP.CUSTOMERSHIPMENTPOSITION_ID, " +
            "        PO.PICKINGORDER_NO, " +
            "        PP.NAME " +
            "      FROM OB_PICKINGORDER PO " +
            "        LEFT JOIN OB_PICKINGORDERPOSITION POP ON PO.ID = POP.PICKINGORDER_ID " +
            "        LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID) B " +
            "       ON CSP.ID = B.CUSTOMERSHIPMENTPOSITION_ID " +
            "     LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID " +
            "     LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
            "     LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID " +
            "   WHERE CS.DELIVERY_DATE = :exsdTime " +
            "         AND CS.STATE = '650') A " +
            "WHERE A.ppName = :ppName ")
    BigDecimal getByPacked(@Param("ppName") String ppName, @Param("exsdTime") LocalDateTime exsdTime);


    /*12. Problem  合计*/
    @Query(nativeQuery = true, value = " SELECT coalesce(SUM(A.AMOUNT), 0) AS Problem " +
            "FROM OBP_OBPROBLEM AO " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENT CSM ON AO.SHIPMENT_ID = CSM.ID " +
            "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSMP ON CSM.ID = CSMP.SHIPMENT_ID " +
            "  LEFT JOIN " +
            "  (SELECT " +
            "     CSP.ID, " +
            "     CS.SHIPMENT_NO, " +
            "     CO.ORDER_NO, " +
            "     BT.NAME, " +
            "     ITD.SKU_NO, " +
            "     ITD.ITEM_NO, " +
            "     CSP.AMOUNT, " +
            "     CS.DELIVERY_DATE, " +
            "     CS.SORT_CODE, " +
            "     B.NAME AS PPNAME, " +
            "     B.PICKINGORDER_NO " +
            "   FROM OB_CUSTOMERSHIPMENT CS " +
            "     LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
            "     LEFT JOIN " +
            "     (SELECT DISTINCT " +
            "        POP.CUSTOMERSHIPMENTPOSITION_ID, " +
            "        PO.PICKINGORDER_NO, " +
            "        PP.NAME " +
            "      FROM OB_PICKINGORDER PO " +
            "        LEFT JOIN OB_PICKINGORDERPOSITION POP ON PO.ID = POP.PICKINGORDER_ID " +
            "        LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID) B " +
            "       ON CSP.ID = B.CUSTOMERSHIPMENTPOSITION_ID " +
            "     LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID " +
            "     LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
            "     LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID " +
            "   WHERE CS.DELIVERY_DATE = :exsdTime) A " +
            "    ON CSMP.ID = A.ID " +
            "       AND A.PPNAME = :ppName ")
    BigDecimal getByProblem(@Param("ppName") String ppName, @Param("exsdTime") LocalDateTime exsdTime);

    /*13. Sorted  合计*/
    @Query(nativeQuery = true, value = " SELECT coalesce(SUM(A.AMOUNT), 0) AS Sorted " +
            "FROM (SELECT " +
            "        CSP.ID, " +
            "        CS.SHIPMENT_NO, " +
            "        CO.ORDER_NO, " +
            "        BT.NAME, " +
            "        ITD.SKU_NO, " +
            "        ITD.ITEM_NO, " +
            "        CSP.AMOUNT, " +
            "        CS.DELIVERY_DATE, " +
            "        CS.SORT_CODE, " +
            "        B.NAME AS PPNAME, " +
            "        B.PICKINGORDER_NO " +
            "      FROM OB_CUSTOMERSHIPMENT CS " +
            "        LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
            "        LEFT JOIN " +
            "        (SELECT DISTINCT " +
            "           POP.CUSTOMERSHIPMENTPOSITION_ID, " +
            "           PO.PICKINGORDER_NO, " +
            "           PP.NAME " +
            "         FROM OB_PICKINGORDER PO " +
            "           LEFT JOIN OB_PICKINGORDERPOSITION POP ON PO.ID = POP.PICKINGORDER_ID " +
            "           LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID) B " +
            "          ON CSP.ID = B.CUSTOMERSHIPMENTPOSITION_ID " +
            "        LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID " +
            "        LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
            "        LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID " +
            "      WHERE CS.DELIVERY_DATE = :exsdTime " +
            "            AND CS.STATE = '660') A " +
            "WHERE A.PPNAME = :ppName ")
    BigDecimal getBySorted(@Param("ppName") String ppName, @Param("exsdTime") LocalDateTime exsdTime);


}
