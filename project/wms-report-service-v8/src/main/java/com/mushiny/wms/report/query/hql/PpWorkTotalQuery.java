package com.mushiny.wms.report.query.hql;

import com.mushiny.wms.report.query.dto.pp_work.PpName;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Component
//Process Path-Work Pool  合计 查询
public class PpWorkTotalQuery implements Serializable {
    @PersistenceContext
    private EntityManager entityManager;

    /*获取 PpName列表 */
    public List<String> getByPpName() {
        String sql = " SELECT pp.NAME FROM  OB_PROCESSPATH AS pp  ";
        Query query = entityManager.createNativeQuery(sql);
        List<String> ppName = query.getResultList();
        return ppName;
    }

    /*total*/
    public List<PpName> getByTotal(LocalDateTime exsdTime) {
        String sql = " SELECT " +
                "  PP.NAME                      AS name, " +
                "  coalesce(SUM(POP.AMOUNT), 0) AS amount " +
                "FROM OB_PICKINGORDERPOSITION POP " +
                "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PP.ID = PO.PROCESSPATH_ID " +
                "WHERE POP.PICKINGORDER_ID IS NOT NULL " +
                "      AND POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "          IN (SELECT CSP.ID " +
                "              FROM OB_CUSTOMERSHIPMENT CS " +
                "                LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "              WHERE CS.DELIVERY_DATE = :exsdTime) " +
                "GROUP BY PP.NAME ";
//        String sql = " SELECT   " +
//                "  PP.NAME                      AS name,   " +
//                "  coalesce(SUM(POP.AMOUNT), 0) AS amount   " +
//                "FROM OB_PICKINGORDERPOSITION POP   " +
//                "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID   " +
//                "  LEFT JOIN OB_PROCESSPATH PP ON PP.ID = PO.PROCESSPATH_ID   " +
//                "WHERE POP.PICKINGORDER_ID IS NOT NULL   " +
//                "      AND POP.CUSTOMERSHIPMENTPOSITION_ID   " +
//                "          IN (SELECT CSP.ID   " +
//                "              FROM OB_CUSTOMERSHIPMENT CS   " +
//                "                LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID   " +
//                "              WHERE CS.DELIVERY_DATE = :exsdTime)   " +
//                "      AND POP.CUSTOMERSHIPMENTPOSITION_ID   " +
//                "GROUP BY PP.NAME ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(PpName.class));
        List<PpName> total = query.getResultList();
        return total;
    }

    /*Ready To Pick*/
    public List<PpName> getByReadyToPick(LocalDateTime exsdTime) {

        String sql = " SELECT " +
                "  PP.NAME                      AS name, " +
                "  COALESCE(SUM(POP.AMOUNT), 0) AS amount " +
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
                "              WHERE CS.DELIVERY_DATE = :exsdTime) " +
                "GROUP BY PP.NAME ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(PpName.class));
        List<PpName> readyToPick = query.getResultList();
        return readyToPick;
    }


    /*Picking Not Yet Picked*/
    public List<PpName> getByPickingNotYetPicked(LocalDateTime exsdTime) {

        String sql = " SELECT " +
                "  PP.NAME                                          AS name, " +
                "  COALESCE(SUM(POP.AMOUNT - POP.AMOUNT_PICKED), 0) AS amount " +
                "FROM OB_PICKINGORDERPOSITION POP " +
                "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PP.ID = PO.PROCESSPATH_ID " +
                "WHERE PO.STATE < 600 " +
                "      AND POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "          IN (SELECT CSP.ID " +
                "              FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
                "                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
                "              WHERE CS.DELIVERY_DATE = :exsdTime) " +
                "      AND POP.PICKINGORDER_ID IS NOT NULL " +
                "GROUP BY PP.NAME ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(PpName.class));
        List<PpName> pickingNotYetPickeds = query.getResultList();
        return pickingNotYetPickeds;
    }

    /* PickingPicked */
    public List<PpName> getByPickingPicked(LocalDateTime exsdTime) {
        String sql = " SELECT " +
                "  PP.NAME                             AS name, " +
                "  coalesce(sum(POP.AMOUNT_PICKED), 0) AS amount " +
                "FROM OB_PICKINGORDERPOSITION POP " +
                "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PP.ID = PO.PROCESSPATH_ID " +
                "WHERE POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "      IN (SELECT CSP.ID " +
                "          FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
                "            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
                "          WHERE CS.DELIVERY_DATE = :exsdTime " +
                "                AND CS.STATE IN ('500', '600')) " +
                "GROUP BY PP.NAME ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(PpName.class));
        List<PpName> pickingPickeds = query.getResultList();

        return pickingPickeds;
    }

    /*Rebatched*/
    public List<PpName> getByRebatched(LocalDateTime exsdTime) {
        String sql = " SELECT " +
                "  PP.NAME                      AS name, " +
                "  coalesce(sum(POP.AMOUNT), 0) AS amount " +
                "FROM OB_PICKINGORDERPOSITION POP " +
                "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PP.ID = PO.PROCESSPATH_ID " +
                "WHERE POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "      IN (SELECT CSP.ID " +
                "          FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
                "            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
                "          WHERE CS.DELIVERY_DATE = :exsdTime " +
                "                AND CS.STATE = 610) " +
                "GROUP BY PP.NAME ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(PpName.class));
        List<PpName> rebatcheds = query.getResultList();
        return rebatcheds;

    }

    /* Rebin Buffer */
    public List<PpName> getByRebinBuffer(LocalDateTime exsdTime) {
        String sql = "SELECT  " +
                "  PP.NAME                      AS name,  " +
                "  coalesce(SUM(RBP.AMOUNT), 0) AS amount  " +
                " FROM OB_REBINREQUESTPOSITION RBP  " +
                "  LEFT JOIN OB_REBINREQUEST RQ ON RBP.REBINREQUEST_ID = RQ.ID  " +
                "  LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID  " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID  " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON RBP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID  " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID  " +
                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID  " +
                "  LEFT JOIN MD_ITEMDATA ITD ON RBP.ITEMDATA_ID = ITD.ID  " +
                "  LEFT JOIN OB_REBINWALL RW ON RBP.REBINWALL_ID = RW.ID  " +
                " WHERE RBP.AMOUNT_REBINED = 0  " +
                "      AND RBP.CUSTOMERSHIPMENTPOSITION_ID  " +
                "          IN (SELECT CSP.ID  " +
                "              FROM OB_CUSTOMERSHIPMENTPOSITION CSP  " +
                "                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID  " +
                "              WHERE CSP.STATE = 620  " +
                "                    AND CS.DELIVERY_DATE = :exsdTime) " +
                " GROUP BY name ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(PpName.class));
        List<PpName> rebinBuffers = query.getResultList();
        return rebinBuffers;
    }

    /* Rebined */
    public List<PpName> getByRebined(LocalDateTime exsdTime) {
        String sql = " SELECT " +
                "  C.ppName                    AS name, " +
                "  coalesce(SUM(C.quality), 0) AS amount " +
                "  FROM " +
                "(SELECT  DISTINCT " +
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
                "  WHERE C.quality != 0 " +
                "  GROUP BY C.ppName ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(PpName.class));
        List<PpName> rebineds = query.getResultList();
        return rebineds;
    }

    /* Scan Verify */
    public List<PpName> getByScanVerify(LocalDateTime exsdTime) {
        String sql = " SELECT " +
                "  PP.NAME                      AS name, " +
                "  coalesce(SUM(PRP.AMOUNT), 0) AS amount " +
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
                "GROUP BY PP.NAME ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(PpName.class));
        List<PpName> scanVerifys = query.getResultList();
        return scanVerifys;
    }

    /* Packed */
    public List<PpName> getByPacked(LocalDateTime exsdTime) {
        String sql = " SELECT " +
                "  B.NAME                       AS name, " +
                "  coalesce(SUM(CSP.AMOUNT), 0) AS amount " +
                "FROM OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_PICKINGCATEGORY PC ON CS.PICKINGCATEGORY_ID = PC.ID " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PC.PROCESSPATH_ID = PP.ID " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "  LEFT JOIN " +
                "  (SELECT DISTINCT " +
                "     POP.CUSTOMERSHIPMENTPOSITION_ID, " +
                "     PO.PICKINGORDER_NO, " +
                "     PP.NAME " +
                "   FROM OB_PICKINGORDER PO " +
                "     LEFT JOIN OB_PICKINGORDERPOSITION POP ON PO.ID = POP.PICKINGORDER_ID " +
                "     LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID) B " +
                "    ON CSP.ID = B.CUSTOMERSHIPMENTPOSITION_ID " +
                "  LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID " +
                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
                "  LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID " +
                "WHERE CS.DELIVERY_DATE = :exsdTime " +
                "      AND CS.STATE = '650' " +
                "GROUP BY B.NAME ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(PpName.class));
        List<PpName> packeds = query.getResultList();
        return packeds;
    }

    /* Problem */
    public List<PpName> getByProblem(LocalDateTime exsdTime) {
        String sql = " SELECT " +
                "  A.PPNAME                   AS name, " +
                "  coalesce(SUM(A.AMOUNT), 0) AS amount " +
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
                "  WHERE A.AMOUNT > 0 " +
                "GROUP BY A.PPNAME ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(PpName.class));
        List<PpName> problems = query.getResultList();
        return problems;
    }

    /* Sorted */
    public List<PpName> getBySorted(LocalDateTime exsdTime) {
        String sql = " SELECT " +
                "  B.NAME                       AS name, " +
                "  coalesce(SUM(CSP.AMOUNT), 0) AS amount " +
                "FROM OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "  LEFT JOIN " +
                "  (SELECT DISTINCT " +
                "     POP.CUSTOMERSHIPMENTPOSITION_ID, " +
                "     PO.PICKINGORDER_NO, " +
                "     PP.NAME " +
                "   FROM OB_PICKINGORDER PO " +
                "     LEFT JOIN OB_PICKINGORDERPOSITION POP ON PO.ID = POP.PICKINGORDER_ID " +
                "     LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID) B " +
                "    ON CSP.ID = B.CUSTOMERSHIPMENTPOSITION_ID " +
                "  LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID " +
                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
                "  LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID " +
                "WHERE CS.DELIVERY_DATE = :exsdTime " +
                "      AND CS.STATE = '660' " +
                "GROUP BY B.NAME ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(PpName.class));
        List<PpName> sorteds = query.getResultList();
        return sorteds;
    }


}
