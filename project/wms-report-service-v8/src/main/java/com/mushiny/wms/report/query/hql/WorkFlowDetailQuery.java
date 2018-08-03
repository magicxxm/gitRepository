package com.mushiny.wms.report.query.hql;

import com.mushiny.wms.report.query.dto.WorkFlowDetailDTO;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//WorkFlow明细查询
@Component
public class WorkFlowDetailQuery implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    //获取所有时间段的明细
    public List<WorkFlowDetailDTO> getWorkflowTotalDeail(String ppName, String workflowType) {
        List<WorkFlowDetailDTO> totalDeail = new ArrayList<>();
        List<LocalDateTime> exsdTimes = getByExsdTimes();
        for (LocalDateTime exsdTime : exsdTimes) {
            totalDeail.addAll(getWorkflowDetail(ppName, exsdTime.toString(), workflowType));
        }
        return totalDeail;
    }

    //获取所有时间
    public List<LocalDateTime> getByExsdTimes() {
        String sql = "SELECT distinct e.deliveryTime FROM CustomerShipment AS e order by e.deliveryTime ";

        Query query = entityManager.createQuery(sql);
        List<LocalDateTime> exsdTimes = query.getResultList();

        return exsdTimes;
    }

    //获取某个时间点的 明细
    public List<WorkFlowDetailDTO> getWorkflowDetail(String ppName, String exsdTime, String workflowType) {

        List<WorkFlowDetailDTO> dtos = new ArrayList<>();
        if ("stockEnough".equalsIgnoreCase(workflowType)) {
            dtos = getByStockEnoughs(ppName, exsdTime);
        }
        if ("replenishing".equalsIgnoreCase(workflowType)) {
            dtos = getByReplenishings(ppName, exsdTime);
        }
        if ("totalReplenishment".equalsIgnoreCase(workflowType)) {
            dtos = getByTotalReplenishments(ppName, exsdTime);
        }

        if ("pending".equalsIgnoreCase(workflowType)) {
            dtos = getByPendng(ppName, exsdTime);
        }
        if ("readyToPick".equalsIgnoreCase(workflowType)) {
            dtos = getByReadyToPicks(ppName, exsdTime);
        }
        if ("pickingNotYetPicked".equalsIgnoreCase(workflowType)) {
            dtos = getByPickingNotYetPickeds(ppName, exsdTime);
        }
        if ("totalPicking".equalsIgnoreCase(workflowType)) {
            dtos = getByTotalPickings(ppName, exsdTime);
        }
        if ("pickingPicked".equalsIgnoreCase(workflowType)) {
            dtos = getByPickingPickeds(ppName, exsdTime);
        }
        if ("rebatched".equalsIgnoreCase(workflowType)) {
            dtos = getByRebatcheds(ppName, exsdTime);
        }
        if ("rebinBuffer".equalsIgnoreCase(workflowType)) {
            dtos = getByRebinBuffers(ppName, exsdTime);
        }
        if ("rebined".equalsIgnoreCase(workflowType)) {
            dtos = getByRebineds(ppName, exsdTime);
        }
        if ("scanVerify".equalsIgnoreCase(workflowType)) {
            dtos = getByScanVerifys(ppName, exsdTime);
        }
        if ("packed".equalsIgnoreCase(workflowType)) {
            dtos = getByPackeds(ppName, exsdTime);
        }
        if ("totalWorkInProcess".equalsIgnoreCase(workflowType)) {
            dtos = getByTotalWorkInProcess(ppName, exsdTime);
        }
        if ("problem".equalsIgnoreCase(workflowType)) {
            dtos = getByProblems(ppName, exsdTime);
        }
        if ("sorted".equalsIgnoreCase(workflowType)) {
            dtos = getBySorteds(ppName, exsdTime);
        }
        if ("loaded".equalsIgnoreCase(workflowType)) {
            dtos = getByLoadeds(ppName, exsdTime);
        }
        if ("manifested".equalsIgnoreCase(workflowType)) {
            dtos = getByManifested(ppName, exsdTime);
        }
        if ("totalShipping".equalsIgnoreCase(workflowType)) {
            dtos = getByTotalShippings(ppName, exsdTime);
        }
        if ("total".equalsIgnoreCase(workflowType)) {
            dtos = getByTotal(ppName, exsdTime);
        }
        return dtos;
    }


    /*1. StockEnough 明细 */
    public List<WorkFlowDetailDTO> getByStockEnoughs(String ppName, String exsdTime) {
        //无ppName
        if (ppName != null) {
            return new ArrayList<>();
        }
        String sql = "SELECT DISTINCT " +
                "  CS.SHIPMENT_NO   AS shipmentID, " +
                "  CO.ORDER_NO      AS orderID, " +
                "  BT.NAME          AS boxType, " +
                "  ITD.SKU_NO       AS SKUNO, " +
                "  ITD.ITEM_NO      AS SKUID, " +
                "  CSP.AMOUNT       AS quality, " +
                "  CS.DELIVERY_DATE AS planDepartTime, " +
                "  ''               AS stockPosition1, " +
                "  ''               AS stockPosition2, " +
                "  'Stock enough'   AS workFlowStatus, " +
                "  ''               AS utilization, " +
                "  ''               AS batchNo " +
                "FROM OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "  LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID " +
                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
                "  LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID " +
                "  LEFT JOIN OB_PICKINGORDERPOSITION POP ON CSP.ID = POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "  LEFT JOIN INV_STOCKUNIT SU ON POP.PICKFROMSTOCKUNIT_ID = SU.ID " +
                "  LEFT JOIN OB_PICKINGUNITLOAD RUL ON POP.PICKTOUNITLOAD_ID = RUL.ID " +
                "  LEFT JOIN INV_UNITLOAD UL ON RUL.UNITLOAD_ID = UL.ID " +
                "  LEFT JOIN MD_STORAGELOCATION SL ON UL.STORAGELOCATION_ID = SL.ID " +
                "WHERE CS.STATE = 550 " +
                "      AND CS.DELIVERY_DATE =:exsdTime " +
                "      AND UL.STORAGELOCATION_ID " +
                "          IN (SELECT STL.ID " +
                "              FROM MD_STORAGELOCATION STL LEFT JOIN MD_AREA A " +
                "                  ON STL.AREA_ID = A.ID " +
                "              WHERE A.NAME = 'Picking_Zone') ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return dtos;
    }

    /*2. Replenishing 明细*/
    public List<WorkFlowDetailDTO> getByReplenishings(String ppName, String exsdTime) {
        //无ppName
        if (ppName != null) {
            return new ArrayList<>();
        }
        String sql = "SELECT DISTINCT " +
                "  A.SHIPMENT_NO          AS shipmentID, " +
                "  A.ORDER_NO             AS orderID, " +
                "  A.NAME                 AS boxType, " +
                "  A.SKU_NO               AS SKUNO, " +
                "  A.ITEM_NO              AS SKUID, " +
                "  A.AMOUNT               AS quality, " +
                "  A.DELIVERY_DATE        AS planDepartTime, " +
                "  B.FROM_STORAGELOCATION AS stockPosition1, " +
                "  B.STORAGELOCATION_ID   AS stockPosition2, " +
                "  'Replenishing'         AS workFlowStatus, " +
                "  ''                     AS ppName, " +
                "  ''                     AS batchNo " +
                "FROM " +
                "  (SELECT " +
                "     CSP.ITEMDATA_ID, " +
                "     CSP.ID, " +
                "     CS.SHIPMENT_NO, " +
                "     CO.ORDER_NO, " +
                "     BT.NAME, " +
                "     ITD.SKU_NO, " +
                "     ITD.ITEM_NO, " +
                "     CSP.AMOUNT, " +
                "     CS.DELIVERY_DATE, " +
                "     CS.SORT_CODE " +
                "   FROM OB_CUSTOMERSHIPMENT CS " +
                "     LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "     LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID " +
                "     LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
                "     LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID " +
                "   WHERE CS.DELIVERY_DATE =:exsdTime " +
                "         AND CS.STATE = 0) A, " +
                "  (SELECT " +
                "     STU.ITEMDATA_ID, " +
                "     SUR.FROM_STORAGELOCATION, " +
                "     UL.STORAGELOCATION_ID " +
                "   FROM INV_STOCKUNIT STU " +
                "     LEFT JOIN INV_STOCKUNITRECORD SUR ON STU.ID = SUR.TO_STOCKUNIT " +
                "     LEFT JOIN INV_UNITLOAD UL ON STU.UNITLOAD_ID = UL.ID " +
                "   WHERE SUR.FROM_STORAGELOCATION " +
                "         IN (SELECT STL.NAME " +
                "             FROM MD_STORAGELOCATION STL " +
                "               LEFT JOIN MD_AREA A ON STL.AREA_ID = A.ID " +
                "             WHERE A.NAME = 'Buffer_Zone')) B " +
                "WHERE A.ITEMDATA_ID = B.ITEMDATA_ID ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return dtos;
    }

    /*3. TotalReplenishment 明细*/
    public List<WorkFlowDetailDTO> getByTotalReplenishments(String ppName, String exsdTime) {
        //无ppName
        if (ppName != null) {
            return new ArrayList<>();
        }

        String sql = "SELECT DISTINCT " +
                "  CS.SHIPMENT_NO        AS shipmentID, " +
                "  CO.ORDER_NO           AS orderID, " +
                "  BT.NAME               AS boxType, " +
                "  ITD.SKU_NO            AS SKUNO, " +
                "  ITD.ITEM_NO           AS SKUID, " +
                "  CSP.AMOUNT            AS quality, " +
                "  CS.DELIVERY_DATE      AS planDepartTime, " +
                "  ''                    AS stockPosition1, " +
                "  ''                    AS stockPosition2, " +
                "  'Total Replenishment' AS workFlowStatus, " +
                "  ''                    AS ppName, " +
                "  ''                    AS batchNo " +
                "FROM OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "  LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID " +
                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
                "  LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID " +
                "WHERE CS.STATE = 550 " +
                "      AND CS.DELIVERY_DATE =:exsdTime ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return dtos;
    }

    /*.新增 Pending  明细*/
    public List<WorkFlowDetailDTO> getByPendng(String ppName, String exsdTime) {
        String sql = " SELECT" +
                "  CS.SHIPMENT_NO     AS shipmentID, " +
                "  CS.CUSTOMER_NAME   AS orderID, " +
                "  BT.NAME            AS boxType, " +
                "  ITD.SKU_NO         AS SKUNO, " +
                "  ITD.ITEM_NO        AS SKUID, " +
                "  COALESCE((CSP.AMOUNT),0)         AS quality, " +
                "  CS.DELIVERY_DATE   AS planDepartTime, " +
                "  SL.NAME            AS stockPosition1, " +
                "  ''                 AS stockPosition2, " +
                "  'Pending'    AS workFlowStatus, " +
                "  PP.NAME            AS ppName, " +
                "  PO.PICKINGORDER_NO AS batchNo, " +
                "  CSP.LOT_DATE AS lotDate " +
                " FROM OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "  LEFT JOIN OB_PICKINGORDERPOSITION POP ON CSP.ID = POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID " +
                "  LEFT JOIN OB_PICKINGCATEGORY PC ON CS.PICKINGCATEGORY_ID = PC.ID " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PC.PROCESSPATH_ID = PP.ID " +
                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
                "  LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID " +
                "  LEFT JOIN INV_STOCKUNIT ST ON POP.PICKFROMSTOCKUNIT_ID = ST.ID " +
                "  LEFT JOIN INV_UNITLOAD UL ON ST.UNITLOAD_ID = UL.ID " +
                "  LEFT JOIN MD_STORAGELOCATION SL ON UL.STORAGELOCATION_ID = SL.ID " +
                "WHERE CSP.ID not IN " +
                "      (SELECT DISTINCT POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "       FROM OB_PICKINGORDERPOSITION POP " +
                "         LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID) " +
                "      AND CS.DELIVERY_DATE = :exsdTime AND CSP.STATE <= '300' ";
        if (ppName != null) {
            sql = sql + " AND PP.NAME =:ppName ";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        if (ppName != null) {
            query.setParameter("ppName", ppName);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return dtos;
    }

    /*4. ReadyToPick 明细*/
    public List<WorkFlowDetailDTO> getByReadyToPicks(String ppName, String exsdTime) {
        String sql = "SELECT DISTINCT " +
                "  CS.SHIPMENT_NO     AS shipmentID, " +
                "  CS.CUSTOMER_NAME   AS orderID, " +
                "  BT.NAME            AS boxType, " +
                "  ITD.SKU_NO         AS SKUNO, " +
                "  ITD.ITEM_NO        AS SKUID, " +
                "  POP.AMOUNT         AS quality, " +
                "  CS.DELIVERY_DATE   AS planDepartTime, " +
                "  SL.NAME            AS stockPosition1, " +
                "  ''                 AS stockPosition2, " +
                "  'Ready to Pick'    AS workFlowStatus, " +
                "  PP.NAME            AS ppName, " +
                "  PO.PICKINGORDER_NO AS batchNo " +
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
                "WHERE POP.PICKINGORDER_ID IS NULL  " +
                "      AND POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "          IN (SELECT CSP.ID " +
                "              FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
                "                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
                "              WHERE CS.DELIVERY_DATE =:exsdTime) ";
        if (ppName != null) {
            sql = sql + " AND PP.NAME =:ppName ";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        if (ppName != null) {
            query.setParameter("ppName", ppName);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return dtos;
    }

    /*5. PickingNotYetPicked 明细*/
    public List<WorkFlowDetailDTO> getByPickingNotYetPickeds(String ppName, String exsdTime) {
        String sql = "SELECT " +
                "  CS.SHIPMENT_NO                   AS shipmentID, " +
                "  CS.CUSTOMER_NAME                 AS orderID, " +
                "  BT.NAME                          AS boxType, " +
                "  ITD.SKU_NO                       AS SKUNO, " +
                "  ITD.ITEM_NO                      AS SKUID, " +
                "  (POP.AMOUNT - POP.AMOUNT_PICKED) AS quality, " +
                "  CS.DELIVERY_DATE                 AS planDepartTime, " +
                "  POP.PICKFROMLOCATION_NAME         AS stockPosition1, " +
                "  ''                               AS stockPosition2, " +
                "  'Picking Not Yet Picked'         AS workFlowStatus, " +
                "  PP.NAME                          AS ppName, " +
                "  PO.PICKINGORDER_NO               AS batchNo, " +
                "  CSP.LOT_DATE AS lotDate " +
                "FROM OB_PICKINGORDERPOSITION POP " +
                "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
                "  LEFT JOIN MD_ITEMDATA ITD ON POP.ITEMDATA_ID = ITD.ID " +
//                "  LEFT JOIN INV_STOCKUNIT ST ON POP.PICKFROMSTOCKUNIT_ID = ST.ID " +
//                "  LEFT JOIN INV_UNITLOAD UL ON ST.UNITLOAD_ID = UL.ID " +
//                "  LEFT JOIN MD_STORAGELOCATION SL ON UL.STORAGELOCATION_ID = SL.ID " +
                "WHERE PO.STATE < 600 AND POP.STATE < 600 " +
                "      AND POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "          IN (SELECT CSP.ID " +
                "              FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
                "                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
                "              WHERE CS.DELIVERY_DATE =:exsdTime) " +
                "      AND POP.PICKINGORDER_ID IS NOT NULL " +
                "      AND (POP.AMOUNT - POP.AMOUNT_PICKED) > 0 ";
        if (ppName != null) {
            sql = sql + " AND PP.NAME =:ppName ";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        if (ppName != null) {
            query.setParameter("ppName", ppName);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return groupingBy(dtos);
    }

    /* TotalPicking明细 = ReadyToPick 明细 + PickingNotYetPicked 明细 + pending 明细 */
    public List<WorkFlowDetailDTO> getByTotalPickings(String ppName, String exsdTime) {
        List<WorkFlowDetailDTO> dtos = getByReadyToPicks(ppName, exsdTime);
        dtos.addAll(getByPickingNotYetPickeds(ppName, exsdTime));
        dtos.addAll(getByPendng(ppName, exsdTime));
        return dtos;
    }

    /*6. PickingPicked 明细*/
    public List<WorkFlowDetailDTO> getByPickingPickeds(String ppName, String exsdTime) {
//        String sql = "SELECT    " +
//                "   CS.SHIPMENT_NO                   AS shipmentID,    " +
//                "   CS.CUSTOMER_NAME                 AS orderID,    " +
//                "   BT.NAME                          AS boxType,    " +
//                "   ITD.SKU_NO                       AS SKUNO,    " +
//                "   ITD.ITEM_NO                      AS SKUID,    " +
//                "   coalesce((POP.AMOUNT_PICKED), 0) AS quality,    " +
//                "   CS.DELIVERY_DATE                 AS planDepartTime,    " +
//                "   SL.NAME                          AS stockPosition1,    " +
//                "   ''                               AS stockPosition2,    " +
//                "   'PickingPicked'                  AS workFlowStatus,    " +
//                "   PP.NAME                          AS ppName,    " +
//                "  PO.PICKINGORDER_NO               AS batchNo      " +
//                "FROM OB_PICKINGORDERPOSITION POP      " +
//                "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID      " +
//                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID      " +
//                "  LEFT JOIN OB_PICKINGUNITLOAD PUL ON POP.PICKTOUNITLOAD_ID = PUL.ID      " +
//                "  LEFT JOIN INV_UNITLOAD UL ON PUL.UNITLOAD_ID = UL.ID      " +
//                "  LEFT JOIN MD_STORAGELOCATION SL ON UL.STORAGELOCATION_ID = SL.ID      " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID      " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID      " +
//                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID      " +
//                "  LEFT JOIN MD_ITEMDATA ITD ON POP.ITEMDATA_ID = ITD.ID      " +
//                "WHERE POP.CUSTOMERSHIPMENTPOSITION_ID      " +
//                "       IN (SELECT CSP.ID    " +
//                "          FROM OB_CUSTOMERSHIPMENTPOSITION CSP      " +
//                "            LEFT JOIN OB_CUSTOMERSHIPMENT CS      " +
//                "              ON CSP.SHIPMENT_ID = CS.ID    " +
//                "          WHERE CS.DELIVERY_DATE = :exsdTime )    " +
//                "      AND POP.AMOUNT_PICKED > 0  ";

//        String sql = "SELECT DISTINCT     " +
//                "  CS.SHIPMENT_NO                   AS shipmentID,     " +
//                "  CS.CUSTOMER_NAME                 AS orderID,     " +
//                "  BT.NAME                          AS boxType,     " +
//                "  ITD.SKU_NO                       AS SKUNO,     " +
//                "  ITD.ITEM_NO                      AS SKUID,     " +
//                "  coalesce((CSP.AMOUNT_PICKED), 0) AS quality,     " +
//                "  CS.DELIVERY_DATE                 AS planDepartTime,     " +
//                "  SL.NAME                          AS stockPosition1,     " +
//                "  ''                               AS stockPosition2,     " +
//                "  'PickingPicked'                  AS workFlowStatus,     " +
//                "  PP.NAME                          AS ppName,     " +
//                "  PO.PICKINGORDER_NO               AS batchNo     " +
//                "FROM     " +
//                "  OB_CUSTOMERSHIPMENT CS     " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID     " +
//                "  LEFT JOIN OB_PICKINGORDERPOSITION POP ON CSP.ID = POP.CUSTOMERSHIPMENTPOSITION_ID     " +
//
//                "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID     " +
//                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID     " +
//                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID     " +
//                "  LEFT JOIN MD_ITEMDATA ITD ON POP.ITEMDATA_ID = ITD.ID     " +
//                "  LEFT JOIN OB_PICKINGUNITLOAD PUL ON POP.PICKTOUNITLOAD_ID = PUL.ID     " +
//                "  LEFT JOIN INV_UNITLOAD UL ON PUL.UNITLOAD_ID = UL.ID     " +
//                "  LEFT JOIN MD_STORAGELOCATION SL ON UL.STORAGELOCATION_ID = SL.ID     " +
//                "WHERE     " +
//                "  CS.DELIVERY_DATE = :exsdTime    " +
//                "  AND CSP.STATE <= '600'     " +
//                "  AND CSP.AMOUNT_PICKED > 0 ";
//        String sql = " SELECT DISTINCT       " +
//                "  CS.SHIPMENT_NO                   AS shipmentID,       " +
//                "  CS.CUSTOMER_NAME                 AS orderID,       " +
//                "  BT.NAME                          AS boxType,       " +
//                "  ITD.SKU_NO                       AS SKUNO,       " +
//                "  ITD.ITEM_NO                      AS SKUID,       " +
//                "  coalesce((POP.AMOUNT_PICKED), 0) AS quality,       " +
//                "  CS.DELIVERY_DATE                 AS planDepartTime,       " +
//                "  S.NAME                          AS stockPosition1,       " +
//                "  ''                               AS stockPosition2,       " +
//                "  'PickingPicked'                  AS workFlowStatus,       " +
//                "  PP.NAME                          AS ppName,       " +
//                "  PO.PICKINGORDER_NO               AS batchNo       " +
//                "FROM OB_PICKINGORDERPOSITION POP       " +
//                "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID       " +
//                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID       " +
////                "  LEFT JOIN OB_PICKINGUNITLOAD PUL ON POP.PICKTOUNITLOAD_ID = PUL.ID       " +
////                "  LEFT JOIN INV_UNITLOAD UL ON PUL.UNITLOAD_ID = UL.ID       " +
////                "  LEFT JOIN MD_STORAGELOCATION SL ON UL.STORAGELOCATION_ID = SL.ID       " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID       " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID       " +
//                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID       " +
//                "  LEFT JOIN MD_ITEMDATA ITD ON POP.ITEMDATA_ID = ITD.ID       " +
//
//                " LEFT JOIN INV_UNITLOAD_SHIPMENT INV ON CS.ID = INV.SHIPMENT_ID " +
//                " LEFT JOIN INV_UNITLOAD UN ON INV.UNITLOAD_ID = UN.ID " +
//                " LEFT JOIN MD_STORAGELOCATION S ON UN.STORAGELOCATION_ID = S.ID " +
//
//                " WHERE       " +
//                " CS.DELIVERY_DATE = :exsdTime     " +
//                " AND CSP.STATE <= '605'       " +
//                " AND CS.STATE <> '1000'  " +
//                " AND POP.AMOUNT_PICKED > 0 ";
        String sql = "SELECT   " +
                "         CS.SHIPMENT_NO                   AS shipmentID, " +
                "         CS.CUSTOMER_NAME                 AS orderID, " +
                "         BT.NAME                          AS boxType, " +
                "         ITD.SKU_NO                       AS SKUNO, " +
                "         ITD.ITEM_NO                      AS SKUID, " +
                "         coalesce((POP.AMOUNT_PICKED), 0) AS quality, " +
                "         CS.DELIVERY_DATE                 AS planDepartTime, " +
                "         S.NAME                          AS stockPosition1, " +
                "         ''                               AS stockPosition2, " +
                "         'PickingPicked'                  AS workFlowStatus, " +
                "         PP.NAME                          AS ppName, " +
                "         PO.PICKINGORDER_NO               AS batchNo,  " +
                "         CSP.LOT_DATE  AS lotDate " +
                " FROM " +
                "  OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "  LEFT JOIN OB_PICKINGORDERPOSITION POP ON CSP.ID = POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "  LEFT JOIN OB_PICKINGUNITLOAD PUL ON POP.PICKTOUNITLOAD_ID = PUL.ID " +
                " " +
                "    LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID " +
                "    LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
                "    LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
                "    LEFT JOIN MD_ITEMDATA ITD ON POP.ITEMDATA_ID = ITD.ID " +
                "    LEFT JOIN INV_UNITLOAD UN ON PUL.UNITLOAD_ID = UN.ID " +
                "    LEFT JOIN MD_STORAGELOCATION S ON UN.STORAGELOCATION_ID = S.ID " +
                " WHERE " +
                " " +
                "  CS.DELIVERY_DATE = :exsdTime AND POP.AMOUNT_PICKED > 0 AND POP.STATE NOT IN ('800','1000') " +
          //      "  AND CSP.STATE < 600 " ;
                "  AND (CSP.STATE <= '600' OR (CSP.STATE = '605' AND PUL.STATE = '600')) ";

        if (ppName != null) {
            sql = sql + " AND PP.NAME =:ppName ";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        if (ppName != null) {
            query.setParameter("ppName", ppName);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return groupingBy(dtos);
    }

    /*7. Rebatched 明细*/
    public List<WorkFlowDetailDTO> getByRebatcheds(String ppName, String exsdTime) {
        String sql = "SELECT " +
                "  CS.SHIPMENT_NO                   AS shipmentID, " +
                "  CS.CUSTOMER_NAME                 AS orderID, " +
                "  BT.NAME                          AS boxType, " +
                "  ITD.SKU_NO                       AS SKUNO, " +
                "  ITD.ITEM_NO                      AS SKUID, " +
                "  coalesce((POP.AMOUNT_PICKED), 0) AS quality, " +
                "  CS.DELIVERY_DATE                 AS planDepartTime, " +
                "  SL.NAME                          AS stockPosition1, " +
                "  RSL.NAME                         AS stockPosition2, " +
                "  'Rebatched'                      AS workFlowStatus, " +
                "  PP.NAME                          AS ppName, " +
                "  PO.PICKINGORDER_NO               AS batchNo " +
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
                " WHERE POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "      IN (SELECT CSP.ID " +
                "          FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
                "            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
                "          WHERE CS.DELIVERY_DATE =:exsdTime " +
                "               AND (CS.STATE = '610' OR CS.STATE= '605') ) " +
                " AND PUL.STATE = '610' ";

        if (ppName != null) {
            sql = sql + " AND PP.NAME =:ppName ";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        if (ppName != null) {
            query.setParameter("ppName", ppName);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return groupingBy(dtos);
    }

    /*8. RebinBuffer 明细*/
    public List<WorkFlowDetailDTO> getByRebinBuffers(String ppName, String exsdTime) {
        String sql = "SELECT " +
                "  CS.SHIPMENT_NO               AS shipmentID, " +
                "  CS.CUSTOMER_NAME             AS orderID, " +
                "  BT.NAME                      AS boxType, " +
                "  ITD.SKU_NO                   AS SKUNO, " +
                "  ITD.ITEM_NO                  AS SKUID, " +
                "  coalesce(SUM(RBP.AMOUNT), 0) AS quality, " +
                "  CS.DELIVERY_DATE             AS planDepartTime, " +
                "  RBP.REBINFROMCONTAINER_NAME  AS stockPosition1, " +
                "  ''                           AS stockPosition2, " +
                "  'Rebin Buffer'               AS workFlowStatus, " +
                "  PP.NAME                      AS ppName, " +
                "  PO.PICKINGORDER_NO           AS batchNo " +
                "FROM OB_REBINREQUESTPOSITION RBP " +
                "  LEFT JOIN OB_REBINREQUEST RQ ON RBP.REBINREQUEST_ID = RQ.ID " +
                "  LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON RBP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
                "  LEFT JOIN MD_ITEMDATA ITD ON RBP.ITEMDATA_ID = ITD.ID " +
                "  LEFT JOIN OB_REBINWALL RW ON RBP.REBINWALL_ID = RW.ID " +
                "WHERE RBP.AMOUNT_REBINED = 0  " +
                "      AND RBP.CUSTOMERSHIPMENTPOSITION_ID  " +
                "          IN (SELECT CSP.ID    " +
                "              FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
                "                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID    " +
                "              WHERE CSP.STATE = '620'    " +
                "                    AND CS.DELIVERY_DATE = :exsdTime) " +
                "      AND CSP.ID NOT IN       " +
                " ( SELECT RBP.CUSTOMERSHIPMENTPOSITION_ID FROM OB_REBINREQUESTPOSITION RBP WHERE RBP.STATE = 'LOSE') ";

        if (ppName != null) {
            sql = sql + " AND PP.NAME =:ppName ";
        }
        sql = sql + " GROUP BY CS.SHIPMENT_NO, CS.CUSTOMER_NAME, BT.NAME, ITD.SKU_NO, ITD.ITEM_NO, CS.DELIVERY_DATE, " +
                "  RBP.REBINFROMCONTAINER_NAME, '', 'Rebin Buffer', PP.NAME, PO.PICKINGORDER_NO ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        if (ppName != null) {
            query.setParameter("ppName", ppName);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return groupingBy(dtos);
    }

    /*9. Rebined 明细*/
//    public List<WorkFlowDetailDTO> getByRebineds(String ppName, String exsdTime) {
//        String sql = "SELECT " +
//                "  C.shipmentID     AS shipmentID, " +
//                "  C.orderID        AS orderID, " +
//                "  C.boxType        AS boxType, " +
//                "  C.SKUNO          AS SKUNO, " +
//                "  C.SKUID          AS SKUID, " +
//                "  C.quality        AS quality, " +
//                "  C.planDepartTime AS planDepartTime, " +
//                "  C.stockPosition1 AS stockPosition1, " +
//                "  ''               AS stockPosition2, " +
//                "  C.workFlowStatus AS workFlowStatus, " +
//                "  C.ppName         AS ppName, " +
//                "  C.batchNo        AS batchNo " +
//                "FROM ( " +
//                "       SELECT " +
//                "         A.shipmentID, " +
//                "         A.orderID, " +
//                "         A.boxType, " +
//                "         A.SKUNO, " +
//                "         A.SKUID, " +
//                "         coalesce((CASE WHEN (A.quality1 - B.AMOUNT) IS NULL " +
//                "           THEN A.quality1 " +
//                "                   ELSE (A.quality1 - B.AMOUNT) END), 0) AS quality, " +
//                "         A.planDepartTime, " +
//                "         A.stockPosition1, " +
//                "         A.workFlowStatus, " +
//                "         A.ppName, " +
//                "         A.batchNo " +
//                "       FROM " +
//                "         (SELECT " +
//                "            CS.SHIPMENT_NO                        AS shipmentID, " +
//                "            CS.CUSTOMER_NAME                      AS orderID, " +
//                "            BT.NAME                               AS boxType, " +
//                "            ITD.SKU_NO                            AS SKUNO, " +
//                "            ITD.ITEM_NO                           AS SKUID, " +
//                "            coalesce((SUM(RBP.AMOUNT)), 0)        AS quality1, " +
//                "            CS.DELIVERY_DATE                      AS planDepartTime, " +
//                "            concat(RW.NAME, RBP.REBINTOCELL_NAME) AS stockPosition1, " +
//                "            'Rebined'                             AS workFlowStatus, " +
//                "            PP.NAME                               AS ppName, " +
//                "            PO.PICKINGORDER_NO                    AS batchNo " +
//                "          FROM OB_REBINREQUESTPOSITION RBP " +
//                "            LEFT JOIN OB_REBINREQUEST RQ ON RBP.REBINREQUEST_ID = RQ.ID " +
//                "            LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID " +
//                "            LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
//                "            LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON RBP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
//                "            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
//                "            LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
//                "            LEFT JOIN MD_ITEMDATA ITD ON RBP.ITEMDATA_ID = ITD.ID " +
//                "            LEFT JOIN OB_REBINWALL RW ON RBP.REBINWALL_ID = RW.ID " +
//                "          WHERE RBP.AMOUNT > 0 " +
//                "                AND RBP.CUSTOMERSHIPMENTPOSITION_ID " +
//                "                    IN (SELECT CSP.ID " +
//                "                        FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
//                "                          LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
//                "                        WHERE CS.DELIVERY_DATE =:exsdTime) " +
//                "          GROUP BY shipmentID, orderID, boxType, SKUNO, SKUID, planDepartTime, stockPosition1, " +
//                "            workFlowStatus, PP.NAME, PO.PICKINGORDER_NO) A " +
//                "         LEFT JOIN " +
//                "         (SELECT " +
//                "            CS.SHIPMENT_NO, " +
//                "            ITD.SKU_NO, " +
//                "            coalesce((SUM(PRP.AMOUNT)), 0) AS AMOUNT " +
//                "          FROM OB_PACKINGREQUESTPOSITION PRP " +
//                "            LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON PRP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
//                "            LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
//                "            LEFT JOIN MD_ITEMDATA ITD ON PRP.ITEMDATA_ID = ITD.ID " +
//                "          WHERE PRP.CUSTOMERSHIPMENTPOSITION_ID " +
//                "                IN (SELECT CSP.ID " +
//                "                    FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
//                "                      LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
//                "                    WHERE CS.DELIVERY_DATE =:exsdTime) " +
//                "          GROUP BY SHIPMENT_NO, SKU_NO) B " +
//                "           ON A.shipmentID = B.SHIPMENT_NO " +
//                "              AND A.SKUNO = B.SKU_NO) C " +
//                "WHERE C.quality != 0 ";
//
//        if (ppName != null) {
//            sql = sql + " AND C.ppName =:ppName ";
//        }
//        Query query = entityManager.createNativeQuery(sql);
//        query.setParameter("exsdTime", exsdTime);
//        if (ppName != null) {
//            query.setParameter("ppName", ppName);
//        }
//        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
//        List<WorkFlowDetailDTO> dtos = query.getResultList();
//        if (dtos.isEmpty()) {
//            return new ArrayList<>();
//        }
//        return dtos;
//    }
    /*9. Rebined 明细*/
    public List<WorkFlowDetailDTO> getByRebineds(String ppName, String exsdTime) {
//        String sql = " SELECT  DISTINCT " +
//                "    CS.SHIPMENT_NO                        AS shipmentID,   " +
//                "    CS.CUSTOMER_NAME                      AS orderID,   " +
//                "    BT.NAME                               AS boxType,   " +
//                "    ITD.SKU_NO                            AS SKUNO,   " +
//                "    ITD.ITEM_NO                           AS SKUID,   " +
//                "    coalesce((SUM(RBP.AMOUNT)), 0)        AS quality,   " +
//                "    CS.DELIVERY_DATE                      AS planDepartTime,   " +
//                "    concat(RW.NAME, RBP.REBINTOCELL_NAME) AS stockPosition1,   " +
//                "    ''                                    AS stockPosition2,   " +
//                "    'Rebined'                            AS workFlowStatus,   " +
//                "    PP.NAME                               AS ppName,   " +
//                "    PO.PICKINGORDER_NO                    AS batchNo   " +
//                "  FROM OB_REBINREQUESTPOSITION RBP   " +
//                "    LEFT JOIN OB_REBINREQUEST RQ ON RBP.REBINREQUEST_ID = RQ.ID   " +
//                "    LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID   " +
//                "    LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID   " +
//                "    LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON RBP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID   " +
//                "    LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID   " +
//                "    LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID   " +
//                "    LEFT JOIN MD_ITEMDATA ITD ON RBP.ITEMDATA_ID = ITD.ID   " +
//                "    LEFT JOIN OB_REBINWALL RW ON RBP.REBINWALL_ID = RW.ID   " +
//                "  WHERE " +
//                "   CS.ID NOT IN (SELECT CS.ID FROM OB_CUSTOMERSHIPMENT CS WHERE CS.STATE = '1100') " +
//                "   AND (CSP.STATE = '620' OR CS.STATE = '630') " +
//                "                         AND CS.DELIVERY_DATE = :exsdTime " +
//                "                         AND RBP.STATE = 'FINISHED' ";
//                "  GROUP BY shipmentID, orderID, boxType, SKUNO, SKUID, planDepartTime, stockPosition1,   " +
//                "    workFlowStatus, PP.NAME, PO.PICKINGORDER_NO ";
        String sql = "   SELECT  DISTINCT " +
                "    CS.SHIPMENT_NO                        AS shipmentID, " +
                "    CS.CUSTOMER_NAME                      AS orderID, " +
                "    BT.NAME                               AS boxType, " +
                "    ITD.SKU_NO                            AS SKUNO, " +
                "    ITD.ITEM_NO                           AS SKUID, " +
                "    coalesce(sum(RBP.AMOUNT), 0)        AS quality, " +
                "    CS.DELIVERY_DATE                      AS planDepartTime, " +
                "    concat(RW.NAME, RBP.REBINTOCELL_NAME) AS stockPosition1, " +
                "    ''                                    AS stockPosition2, " +
                "    'Rebined'                            AS workFlowStatus, " +
                "    PP.NAME                               AS ppName, " +
                "    PO.PICKINGORDER_NO                    AS batchNo  " +
                "  FROM OB_REBINREQUESTPOSITION RBP " +
                "    LEFT JOIN OB_REBINREQUEST RQ ON RBP.REBINREQUEST_ID = RQ.ID " +
                "    LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID " +
                "    LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
                "    LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON RBP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
                "    LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
                "    LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
                "    LEFT JOIN MD_ITEMDATA ITD ON RBP.ITEMDATA_ID = ITD.ID " +
                "    LEFT JOIN OB_REBINWALL RW ON RBP.REBINWALL_ID = RW.ID " +
                "  WHERE   " +
                "         ((PO.STATE <> '700' ) or (PO.STATE = '700' AND CS.STATE = '630' AND CS.ID NOT IN (SELECT CS.ID FROM OB_CUSTOMERSHIPMENT CS WHERE CS.STATE = '1100'))) " +
                "        AND CS.DELIVERY_DATE = :exsdTime " +
                "        AND RBP.STATE <> 'RAW' " +
                "        AND RBP.STATE <> 'LOSE' " +
                "        AND CSP.SHIPMENT_ID NOT IN (  " +
                "           SELECT HOT.SHIPMENT_ID FROM OB_CUSTOMERSHIPMENT_HOTPICK HOT  ) ";
        if (ppName != null) {
            sql = sql + " AND PP.Name =:ppName ";
        }
        sql = sql + " GROUP BY shipmentID, orderID, boxType, SKUNO, SKUID, planDepartTime, stockPosition1,   " +
                "    workFlowStatus, PP.NAME, PO.PICKINGORDER_NO ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        if (ppName != null) {
            query.setParameter("ppName", ppName);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return groupingBy(dtos);
    }

    /*10. ScanVerify 明细*/
    public List<WorkFlowDetailDTO> getByScanVerifys(String ppName, String exsdTime) {
//        String sql = "SELECT " +
//                "  CS.SHIPMENT_NO     AS shipmentID, " +
//                "  CS.CUSTOMER_NAME   AS orderID, " +
//                "  BT.NAME            AS boxType, " +
//                "  ITD.SKU_NO         AS SKUNO, " +
//                "  ITD.ITEM_NO        AS SKUID, " +
//                "  PRP.AMOUNT         AS quality, " +
//                "  CS.DELIVERY_DATE   AS planDepartTime, " +
//                "  (CASE WHEN RCS.REBINWALL_INDEX = 1 " +
//                "    THEN concat(RW1.NAME, RCS.REBINCELL_NAME) " +
//                "   WHEN RCS.REBINWALL_INDEX = 2 " +
//                "     THEN concat(RW2.NAME, RCS.REBINCELL_NAME) " +
//                "   ELSE '' END)      AS stockPosition1, " +
//                "  ''                 AS stockPosition2, " +
//                "  'Scan Verify'      AS workFlowStatus, " +
//                "  PP.NAME            AS ppName, " +
//                "  PO.PICKINGORDER_NO AS batchNo " +
//                "FROM OB_PACKINGREQUEST PR " +
//                "  LEFT JOIN OB_PACKINGREQUESTPOSITION PRP ON PR.ID = PRP.PACKINGREQUEST_ID " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON PRP.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
//                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
//                "  LEFT JOIN MD_ITEMDATA ITD ON PRP.ITEMDATA_ID = ITD.ID " +
//                "  LEFT JOIN OB_REBINCUSTOMERSHIPMENT RCS ON CS.ID = RCS.CUSTOMERSHIPMENT_ID " +
//                "  LEFT JOIN OB_REBINREQUEST RQ ON RCS.REBINREQUEST_ID = RQ.ID " +
//                "  LEFT JOIN OB_PICKINGORDER PO ON RQ.PICKINGORDER_ID = PO.ID " +
//                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
//                "  LEFT JOIN OB_REBINWALL RW1 ON RQ.REBINWALL1_ID = RW1.ID " +
//                "  LEFT JOIN OB_REBINWALL RW2 ON RQ.REBINWALL2_ID = RW2.ID " +
//                "WHERE PRP.CUSTOMERSHIPMENTPOSITION_ID " +
//                "      IN (SELECT CSP.ID " +
//                "          FROM OB_CUSTOMERSHIPMENT CS " +
//                "            LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
//                "          WHERE CSP.STATE = '640' " +
//                "                AND CS.DELIVERY_DATE =:exsdTime) ";

        String sql = "SELECT DISTINCT " +
                "  CS.SHIPMENT_NO     AS shipmentID, " +
                "  CS.CUSTOMER_NAME   AS orderID, " +
                "  BT.NAME            AS boxType, " +
                "  ITD.SKU_NO         AS SKUNO, " +
                "  ITD.ITEM_NO        AS SKUID, " +
                "  CSP.AMOUNT         AS quality, " +
                "  CS.DELIVERY_DATE   AS planDepartTime, " +
                "  S.NAME            AS stockPosition1, " +
                "  PA.NAME            AS stockPosition2, " +
                "  'Scan Verify'      AS workFlowStatus, " +
                "  PP.NAME            AS ppName, " +
                "  PO.PICKINGORDER_NO AS batchNo " +
                "FROM OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_PACKINGREQUEST PR ON CS.ID = PR.CUSTOMERSHIPMENT_ID " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
                "  LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID " +
                "  LEFT JOIN OB_PACKINGSTATION PA ON PR.PACKINGSTATION_ID = PA.ID " +

                " LEFT JOIN INV_UNITLOAD_SHIPMENT INV ON CS.ID = INV.SHIPMENT_ID " +
                " LEFT JOIN INV_UNITLOAD UN ON INV.UNITLOAD_ID = UN.ID " +
                " LEFT JOIN MD_STORAGELOCATION S ON UN.STORAGELOCATION_ID = S.ID " +

                "  LEFT JOIN OB_PICKINGORDERPOSITION POP ON CSP.ID = POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "  LEFT JOIN OB_PICKINGORDER PO on POP.PICKINGORDER_ID = PO.ID " +
//
//                "  LEFT JOIN OB_PICKINGORDER PO ON CS.SHIPMENT_NO = PO.CUSTOMERSHIPMENT_NO " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
//                "  LEFT JOIN INV_UNITLOAD UL ON PR.FROMUNITLOAD_ID = UL.ID " +
//                "  LEFT JOIN MD_STORAGELOCATION SL ON UL.STORAGELOCATION_ID = SL.ID " +
                "WHERE " +
                "  CS.DELIVERY_DATE = :exsdTime " +
//                "  AND CS.STATE > 600 " +
                "  AND CS.STATE = 640 ";

        if (ppName != null) {
            sql = sql + " AND PP.NAME =:ppName ";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        if (ppName != null) {
            query.setParameter("ppName", ppName);
        }

        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return dtos;
    }

    /*11. Packed 明细*/
    public List<WorkFlowDetailDTO> getByPackeds(String ppName, String exsdTime) {
        String sql = "SELECT DISTINCT " +
                "  A.SHIPMENT_NO   AS shipmentID, " +
                "  A.ORDER_NO      AS orderID, " +
                "  A.NAME          AS boxType, " +
                "  A.SKU_NO        AS SKUNO, " +
                "  A.ITEM_NO       AS SKUID, " +
                "  A.AMOUNT        AS quality, " +
                "  A.DELIVERY_DATE AS planDepartTime, " +
                "  A.packedCell   AS stockPosition1, " +
                "  ''              AS stockPosition2, " +
                "  'Packed'        AS workFlowStatus, " +
                "  A.ppName        AS ppName, " +
                "  A.batchNo       AS batchNo " +
                "FROM (SELECT " +
                "        CSP.ID, " +
                "        CS.SHIPMENT_NO, " +
                "        CO.ORDER_NO, " +
                "        BT.NAME, " +
                "        ITD.SKU_NO, " +
                "        ITD.ITEM_NO, " +
                "        CSP.AMOUNT, " +
                "        CS.DELIVERY_DATE, " +
                "        B.NAME            AS ppName, " +
                "        B.PICKINGORDER_NO AS batchNo, " +
                "        S.NAME            AS packedCell " +
                "      FROM OB_CUSTOMERSHIPMENT CS " +
                "        LEFT JOIN OB_PICKINGCATEGORY PC ON CS.PICKINGCATEGORY_ID = PC.ID " +
                "        LEFT JOIN OB_PROCESSPATH PP ON PC.PROCESSPATH_ID = PP.ID " +
                "        LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "        LEFT JOIN INV_UNITLOAD_SHIPMENT INV ON CS.ID = INV.SHIPMENT_ID   " +
                "        LEFT JOIN INV_UNITLOAD UN ON INV.UNITLOAD_ID = UN.ID   " +
                "        LEFT JOIN MD_STORAGELOCATION S ON UN.STORAGELOCATION_ID = S.ID   " +
                "        LEFT JOIN (SELECT DISTINCT " +
                "                     POP.CUSTOMERSHIPMENTPOSITION_ID, " +
                "                     PO.PICKINGORDER_NO, " +
                "                     PP.NAME ," +
                "                     POP.STATE     " +
                "                   FROM OB_PICKINGORDER PO " +
                "                     LEFT JOIN OB_PICKINGORDERPOSITION POP ON PO.ID = POP.PICKINGORDER_ID " +
                "                     LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID) B " +
                "          ON CSP.ID = B.CUSTOMERSHIPMENTPOSITION_ID " +
                "        LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID " +
                "        LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
                "        LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID " +
                "      WHERE CS.DELIVERY_DATE =:exsdTime " +
                "            AND CS.STATE = '650' AND B.STATE NOT IN ('800' ,'1000') ) A ";

        if (ppName != null) {
            sql = sql + " WHERE A.ppName =:ppName ";
        }
        Query query = entityManager.createNativeQuery(sql);

        query.setParameter("exsdTime", exsdTime);
        if (ppName != null) {
            query.setParameter("ppName", ppName);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return dtos;
    }

    /*TotalWorkInProces 清单 = PickingPicked 清单 + Rebatched 清单 + RebinBuffer 清单 +
                               Rebined 清单 + ScanVerify 清单 + Packed 清单 */

    public List<WorkFlowDetailDTO> getByTotalWorkInProcess(String ppName, String exsdTime) {
        List<WorkFlowDetailDTO> dtos = getByPickingPickeds(ppName, exsdTime);
        dtos.addAll(getByRebatcheds(ppName, exsdTime));
        dtos.addAll(getByRebinBuffers(ppName, exsdTime));
        dtos.addAll(getByRebineds(ppName, exsdTime));
        dtos.addAll(getByScanVerifys(ppName, exsdTime));
        dtos.addAll(getByPackeds(ppName, exsdTime));
        return dtos;
    }


    /*12. Problem 明细*/
    public List<WorkFlowDetailDTO> getByProblems(String ppName, String exsdTime) {

        String sql = " SELECT DISTINCT   " +
                "  A.SHIPMENT_NO     AS shipmentID,   " +
                "  A.ORDER_NO        AS orderID,   " +
                "  A.NAME            AS boxType,   " +
                "  A.SKU_NO          AS SKUNO,   " +
                "  A.ITEM_NO         AS SKUID,   " +
                "  A.AMOUNT          AS quality,   " +
                "  A.DELIVERY_DATE   AS planDepartTime,   " +
                "  A.cellName     AS stockPosition1,   " +
                "  A.SORT_CODE       AS stockPosition2,   " +
                "  'Problem'         AS workFlowStatus,   " +
                "  A.PPNAME          AS ppName,   " +
                "  A.PICKINGORDER_NO AS batchNo   " +
                "   " +
                "FROM   " +
                "  (SELECT DISTINCT   " +
                "     CS.ID,   " +
                "     CS.SHIPMENT_NO,   " +
                "     CO.ORDER_NO,   " +
                "     BT.NAME,   " +
                "     ITD.SKU_NO,   " +
                "     ITD.ITEM_NO,   " +
                "     CSP.AMOUNT,   " +
                "     CS.DELIVERY_DATE,   " +
                "     CS.SORT_CODE,   " +
                "     B.NAME AS PPNAME,   " +
                "     B.PICKINGORDER_NO,   " +
                "     S.NAME AS cellName   " +
                "   FROM OB_CUSTOMERSHIPMENT CS   " +
                "     LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID   " +
                "     LEFT JOIN INV_UNITLOAD_SHIPMENT INV ON CS.ID = INV.SHIPMENT_ID   " +
                "     LEFT JOIN INV_UNITLOAD UN ON INV.UNITLOAD_ID = UN.ID   " +
                "     LEFT JOIN MD_STORAGELOCATION S ON UN.STORAGELOCATION_ID = S.ID   " +
                "     LEFT JOIN (SELECT DISTINCT   " +
                "                  POP.CUSTOMERSHIPMENTPOSITION_ID,   " +
                "                  PO.PICKINGORDER_NO,   " +
                "                  PP.NAME   " +
                "                FROM OB_PICKINGORDER PO   " +
                "                  LEFT JOIN OB_PICKINGORDERPOSITION POP ON PO.ID = POP.PICKINGORDER_ID   " +
                "                  LEFT JOIN OB_PICKINGORDER PR ON POP.PICKINGORDER_ID = PR.ID   " +
                "                  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID   " +
                "                WHERE PR.STATE = '700' ) B   " +
                "       ON CSP.ID = B.CUSTOMERSHIPMENTPOSITION_ID   " +
                "     LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID   " +
                "     LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID   " +
                "     LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID   " +
                "     LEFT JOIN OB_REBINREQUESTPOSITION RBP ON CSP.ID = RBP.CUSTOMERSHIPMENTPOSITION_ID   " +
                "     LEFT JOIN OB_REBINWALL RW ON RBP.REBINWALL_ID = RW.ID   " +
                "     LEFT JOIN OBP_OBPCELL ce ON RW.ID = ce.WALL_ID   " +
                "   WHERE CS.DELIVERY_DATE = :exsdTime   " +
                "         AND CS.STATE = '1100')A";

//        String sql = " SELECT DISTINCT " +
//                "  CS.SHIPMENT_NO             AS shipmentID, " +
//                "  CS.CUSTOMER_NAME           AS orderID, " +
//                "  BT.NAME                    AS boxType, " +
//                "  ITD.SKU_NO                 AS SKUNO, " +
//                "  ITD.ITEM_NO                AS SKUID, " +
//                "  coalesce((CSP.AMOUNT), 0)  AS quality, " +
//                "  CS.DELIVERY_DATE           AS planDepartTime, " +
//                "  OB.CONTAINER               AS stockPosition1, " +
//                "  CS.SORT_CODE               AS stockPosition2, " +
//                "  'Problem'                  AS workFlowStatus, " +
//                "  PP.NAME                    AS ppName, " +
//                "  PO.PICKINGORDER_NO         AS batchNo " +
//                "FROM OBP_OBPROBLEM OB " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON OB.SHIPMENT_ID = CS.ID " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
//                "  LEFT JOIN OB_PICKINGORDERPOSITION POP ON CSP.ID = POP.CUSTOMERSHIPMENTPOSITION_ID " +
//                "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID " +
//                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
//                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
//                "  LEFT JOIN MD_ITEMDATA ITD ON POP.ITEMDATA_ID = ITD.ID " +
//                "  LEFT JOIN OB_PICKINGUNITLOAD PUL ON POP.PICKTOUNITLOAD_ID = PUL.ID " +
//                "WHERE OB.STATE = 'unsolved' " +
//                " AND PO.STATE = '700' " +
//                "      AND CS.DELIVERY_DATE =:exsdTime ";
        if (ppName != null) {
            sql = sql + " AND PP.NAME =:ppName ";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        if (ppName != null) {
            query.setParameter("ppName", ppName);
        }

        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return groupingBy(dtos);
    }

    /*13. Sorted 明细*/
    public List<WorkFlowDetailDTO> getBySorteds(String ppName, String exsdTime) {
        String sql = "  SELECT " +
                "  A.SHIPMENT_NO     AS shipmentID, " +
                "  A.ORDER_NO        AS orderID, " +
                "  A.NAME            AS boxType, " +
                "  A.SKU_NO          AS SKUNO, " +
                "  A.ITEM_NO         AS SKUID, " +
                "  A.AMOUNT          AS quality, " +
                "  A.DELIVERY_DATE   AS planDepartTime, " +
                "  A.cell     AS stockPosition1, " +
                "  A.SORT_CODE       AS stockPosition2, " +
                "  'Sorted'          AS workFlowStatus, " +
                "  A.PPNAME          AS ppName, " +
                "  A.PICKINGORDER_NO AS batchNo " +
                "  FROM (SELECT " +
                "  CSP.ID, " +
                "  CS.SHIPMENT_NO, " +
                "  CO.ORDER_NO, " +
                "  BT.`NAME`, " +
                "  ITD.SKU_NO, " +
                "  ITD.ITEM_NO, " +
                "  CSP.AMOUNT, " +
                "  CS.DELIVERY_DATE, " +
                "  CS.SORT_CODE, " +
                "  B.NAME AS PPNAME, " +
                "  B.PICKINGORDER_NO, " +
                "  S.NAME AS cell " +
                "  FROM OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "    LEFT JOIN INV_UNITLOAD_SHIPMENT INV ON CS.ID = INV.SHIPMENT_ID " +
                "    LEFT JOIN INV_UNITLOAD UN ON INV.UNITLOAD_ID = UN.ID " +
                "    LEFT JOIN MD_STORAGELOCATION S ON UN.STORAGELOCATION_ID = S.ID " +
                "  LEFT JOIN (SELECT DISTINCT " +
                "  POP.CUSTOMERSHIPMENTPOSITION_ID, " +
                "  PO.PICKINGORDER_NO, " +
                "  PP.NAME, " +
                "  POP.STATE     " +
                "  FROM OB_PICKINGORDER PO " +
                "  LEFT JOIN OB_PICKINGORDERPOSITION POP ON PO.ID = POP.PICKINGORDER_ID " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID) B " +
                "  ON CSP.ID = B.CUSTOMERSHIPMENTPOSITION_ID " +
                "  LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID " +
                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID " +
                "  LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID " +
                "  WHERE " +
                "  CS.DELIVERY_DATE = :exsdTime " +
                "  AND CS.STATE = '660' AND B.STATE NOT IN ('800' , '1000')  ) A  ";

        if (ppName != null) {
            sql = sql + " WHERE A.PPNAME =:ppName ";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        if (ppName != null) {
            query.setParameter("ppName", ppName);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return dtos;
    }

    /*14. Loaded 明细*/
    public List<WorkFlowDetailDTO> getByLoadeds(String ppName, String exsdTime) {
        String sql = "                                         " +
                "SELECT      " +
                "                    CS.SHIPMENT_NO    AS shipmentID,      " +
                "                    CO.ORDER_NO       AS orderID,      " +
                "                    BT.`NAME`         AS boxType,      " +
                "                    ITD.SKU_NO        AS SKUNO,      " +
                "                    ITD.ITEM_NO       AS SKUID,      " +
                "                    CSP.AMOUNT        AS quality,      " +
                "                    CS.DELIVERY_DATE  AS planDepartTime,      " +
                "                    S.NAME           AS stockPosition1,      " +
                "                    CS.SORT_CODE       AS stockPosition2,      " +
                "                    'Loaded'          AS workFlowStatus,      " +
                "                    B.NAME            AS ppName,      " +
                "                    B.PICKINGORDER_NO    AS batchNo      " +
                "                  FROM OB_CUSTOMERSHIPMENT CS      " +
                "                    LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID      " +
                "                    LEFT JOIN INV_UNITLOAD_SHIPMENT INV ON CS.ID = INV.SHIPMENT_ID      " +
                "                    LEFT JOIN INV_UNITLOAD UN ON INV.UNITLOAD_ID = UN.ID      " +
                "                    LEFT JOIN MD_STORAGELOCATION S ON UN.STORAGELOCATION_ID = S.ID      " +
                "                    LEFT JOIN (SELECT DISTINCT      " +
                "                                 POP.CUSTOMERSHIPMENTPOSITION_ID,      " +
                "                                 PO.PICKINGORDER_NO,      " +
                "                                 PP.NAME,  " +
                "                                 POP.STATE     " +
                "                               FROM OB_PICKINGORDER PO      " +
                "                                 LEFT JOIN OB_PICKINGORDERPOSITION POP ON PO.ID = POP.PICKINGORDER_ID      " +
                "                                 LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID ) B      " +
                "                      ON CSP.ID = B.CUSTOMERSHIPMENTPOSITION_ID      " +
                "                    LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID      " +
                "                    LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID      " +
                "                    LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID      " +
                "                  WHERE      " +
                "                    CS.DELIVERY_DATE = :exsdTime      " +
                "                    AND CS.STATE = '670' AND B.STATE NOT IN ('800','1000') ";
        if (ppName != null) {
            sql = sql + " WHERE B.NAME =:ppName ";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        if (ppName != null) {
            query.setParameter("ppName", ppName);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return dtos;
    }


    /*15. Manifested 明细*/
    public List<WorkFlowDetailDTO> getByManifested(String ppName, String exsdTime) {
        String sql = " SELECT  " +
                "  CS.SHIPMENT_NO    AS shipmentID,  " +
                "  CO.ORDER_NO       AS orderID,  " +
                "  BT.`NAME`         AS boxType,  " +
                "  ITD.SKU_NO        AS SKUNO,  " +
                "  ITD.ITEM_NO       AS SKUID,  " +
                "  CSP.AMOUNT        AS quality,  " +
                "  CS.DELIVERY_DATE  AS planDepartTime,  " +
                "  S.NAME           AS stockPosition1,  " +
                "  CS.SORT_CODE       AS stockPosition2,  " +
                "  'Manifested'          AS workFlowStatus,  " +
                "  B.NAME            AS ppName,  " +
                "  B.PICKINGORDER_NO    AS batchNo  " +
                "FROM OB_CUSTOMERSHIPMENT CS  " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID  " +
                "  LEFT JOIN INV_UNITLOAD_SHIPMENT INV ON CS.ID = INV.SHIPMENT_ID  " +
                "  LEFT JOIN INV_UNITLOAD UN ON INV.UNITLOAD_ID = UN.ID  " +
                "  LEFT JOIN MD_STORAGELOCATION S ON UN.STORAGELOCATION_ID = S.ID  " +
                "  LEFT JOIN (SELECT DISTINCT  " +
                "               POP.CUSTOMERSHIPMENTPOSITION_ID,  " +
                "               PO.PICKINGORDER_NO,  " +
                "               PP.NAME,  " +
                "               POP.STATE     " +
                "             FROM OB_PICKINGORDER PO  " +
                "               LEFT JOIN OB_PICKINGORDERPOSITION POP ON PO.ID = POP.PICKINGORDER_ID  " +
                "               LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID ) B  " +
                "    ON CSP.ID = B.CUSTOMERSHIPMENTPOSITION_ID  " +
                "  LEFT JOIN OB_CUSTOMERORDER CO ON CS.ORDER_ID = CO.ID  " +
                "  LEFT JOIN OB_BOXTYPE BT ON CS.BOXTYPE_ID = BT.ID  " +
                "  LEFT JOIN MD_ITEMDATA ITD ON CSP.ITEMDATA_ID = ITD.ID  " +
                "WHERE  " +
                "  CS.DELIVERY_DATE = :exsdTime  " +
                "  AND CS.STATE = '680' AND B.STATE NOT IN ('800','1000') ";
        if (ppName != null) {
            sql = sql + " WHERE B.NAME =:ppName ";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        if (ppName != null) {
            query.setParameter("ppName", ppName);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(WorkFlowDetailDTO.class));
        List<WorkFlowDetailDTO> dtos = query.getResultList();
        if (dtos.isEmpty()) {
            return new ArrayList<>();
        }
        return dtos;
    }


//    /*14. Manifested 明细*/
//    public List<DetailDTO> getByManifesteds(String exsdTime) {
//        String ReportSQL = "";
//        Query query = entityManager.createNativeQuery(ReportSQL);
//        query.setParameter("exsdTime", exsdTime);
//        List<DetailDTO> dtos = query.getResultList();
//        if (dtos.isEmpty()) {
//            return new ArrayList<>();
//        }
//        return dtos;
//    }

    /*TotalShipping 明细 = Sorted 明细+ Loaded 明细 + Manifested 明细 */
    public List<WorkFlowDetailDTO> getByTotalShippings(String ppName, String exsdTime) {
        List<WorkFlowDetailDTO> dtos = getBySorteds(ppName, exsdTime);
        dtos.addAll(getByLoadeds(ppName, exsdTime));
        dtos.addAll(getByManifested(ppName, exsdTime));
        return dtos;
    }

    /* Total 明细 =  */
    public List<WorkFlowDetailDTO> getByTotal(String ppName, String exsdTime) {
//        List<WorkFlowDetailDTO> dtos = getByTotalReplenishments(ppName, exsdTime);
//        dtos.addAll(getByTotalPickings(ppName, exsdTime));
//        dtos.addAll(getByTotalWorkInProcess(ppName, exsdTime));
//        dtos.addAll(getByProblems(ppName, exsdTime));
//        dtos.addAll(getByTotalShippings(ppName, exsdTime));
        List<WorkFlowDetailDTO> dtos = getByTotalPickings(ppName, exsdTime);
//        dtos.addAll(getByTotalPickings(ppName, exsdTime));
        dtos.addAll(getByTotalWorkInProcess(ppName, exsdTime));
        dtos.addAll(getByProblems(ppName, exsdTime));
        dtos.addAll(getByTotalShippings(ppName, exsdTime));
        return dtos;
    }

    public String checkNull(String obj) {
        if (obj == null) {
            return "";
        }
        return obj;
    }
    //明细 数据相同的合并
    public List<WorkFlowDetailDTO> groupingBy(List<WorkFlowDetailDTO> workFlowDetailDTOS) {
        if (workFlowDetailDTOS.isEmpty()) {
            return new ArrayList<>();
        }
        List<WorkFlowDetailDTO> dtos = new ArrayList<>();
        workFlowDetailDTOS.stream().collect(
                Collectors.groupingBy(w -> w.getShipmentID(),
                        Collectors.groupingBy(w -> checkNull(w.getSKUNO()),
                                Collectors.groupingBy(w -> checkNull(w.getStockPosition1()))))).forEach((k, v) -> {
            v.forEach((k1, v1) -> {
                v1.forEach((k2, v2) -> {
                    BigDecimal amount = BigDecimal.ZERO;
                    WorkFlowDetailDTO dto = new WorkFlowDetailDTO();
                    for (WorkFlowDetailDTO d : v2) {
                        amount = amount.add(d.getQuality());
                        dto.setShipmentID(d.getShipmentID());
                        dto.setOrderID(d.getOrderID());
                        dto.setBoxType(d.getBoxType());
                        dto.setSKUNO(d.getSKUNO());
                        dto.setSKUID(d.getSKUID());
                        dto.setPlanDepartTime(d.getPlanDepartTime());
                        dto.setStockPosition1(d.getStockPosition1());
                        dto.setStockPosition2(d.getStockPosition2());
                        dto.setWorkFlowStatus(d.getWorkFlowStatus());
                        dto.setPpName(d.getPpName());
                        dto.setBatchNo(d.getBatchNo());
                    }
                    dto.setQuality(amount);
                    dtos.add(dto);
                });
            });
        });
        return dtos;
    }


}
