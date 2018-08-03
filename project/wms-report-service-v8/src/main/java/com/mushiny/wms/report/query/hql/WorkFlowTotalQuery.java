package com.mushiny.wms.report.query.hql;

import com.mushiny.wms.report.query.dto.WorkFlowCutlineDTO;
import org.hibernate.boot.cfgxml.spi.LoadedConfig;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class WorkFlowTotalQuery implements Serializable {
    @PersistenceContext
    private EntityManager entityManager;


    /* 获取 WorkFlow 属性数据 */
    public WorkFlowCutlineDTO getWorkFlowCutline(LocalDateTime exsdTime) {
        WorkFlowCutlineDTO workFlowCutlineDTO = new WorkFlowCutlineDTO();
        BigDecimal sum = BigDecimal.ZERO;

        sum = getByStockEnough(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setStockEnough(sum);
        }

        sum = getByReplenishingAndNeedToReplenish(exsdTime)[1];
        if (sum != null) {
            workFlowCutlineDTO.setReplenishing(sum);
        }

        sum = getByTotalReplenishment(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setTotalReplenishment(sum);
        }

        sum = getByPending(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setPending(sum);
        }

        sum = getByReadyToPick(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setReadyToPick(sum);
        }

        sum = getByPickingNotYetPicked(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setPickingNotYetPicked(sum);
        }

        sum = getByPickingPicked(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setPickingPicked(sum);
        }

        sum = getByRebatched(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setRebatched(sum);
        }

        sum = getByRebined(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setRebined(sum);
        }

        sum = getByRebinBuffer(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setRebinBuffer(sum);
        }

        sum = getByScanVerify(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setScanVerify(sum);
        }

        sum = getByPacked(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setPacked(sum);
        }

        sum = getByProblem(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setProblem(sum);
        }

        sum = getBySorted(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setSorted(sum);
        }

        sum = getByLoaded(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setLoaded(sum);
        }

        sum = getByManifested(exsdTime);
        if (sum != null) {
            workFlowCutlineDTO.setManifested(sum);
        }

        /*初始化运算*/
        workFlowCutlineDTO.setNeedToReplenish();

        workFlowCutlineDTO.setTotalPicking();
        workFlowCutlineDTO.setTotalWorkInProcess();
        workFlowCutlineDTO.setTotalShipping();
        workFlowCutlineDTO.setTotal();
        return workFlowCutlineDTO;
    }


    public BigDecimal getByStockEnough(LocalDateTime exsdTime) {
        BigDecimal stockEnough = BigDecimal.ZERO;

        // 未处理记录
        List<Object[]> objects = getByUntreatedRecord(exsdTime);
        if (objects != null) {
            for (Object o : objects) {
                Object[] row = (Object[]) o;
                stockEnough = stockEnough.add(getByStockEnough((String) row[0]));
            }
        }
        return stockEnough;
    }


    // 获取 needToReplenish 和 replenishing 的值
    public BigDecimal[] getByReplenishingAndNeedToReplenish(LocalDateTime exsdTime) {
        BigDecimal needToReplenish = BigDecimal.ZERO;
        BigDecimal replenishing = BigDecimal.ZERO;

        // 未处理记录
        List<Object[]> objects = getByUntreatedRecord(exsdTime);
        if (objects != null) {
            for (Object o : objects) {
                Object[] row = (Object[]) o;
                BigDecimal s = (BigDecimal) row[1];
                BigDecimal b = getByStockEnough((String) row[0]);
                BigDecimal a = getByReplenishing((String) row[0]);
                if (s.subtract(b).compareTo(BigDecimal.ZERO) > 0) {
                    if (s.subtract(b).compareTo(a) == -1) {
                        replenishing = replenishing.add(s.subtract(b));
                    } else {
                        replenishing = replenishing.add(a);
                    }
                } else {
                    replenishing = replenishing.add(BigDecimal.ZERO);
                }
            }
        }
        BigDecimal[] bigDecimals = {needToReplenish, replenishing};
        return bigDecimals;
    }

    /* 未处理记录 StockEnough + Replenishing 通用 */
    public List<Object[]> getByUntreatedRecord(LocalDateTime exsdTime) {
        String sql = "SELECT " +
                "  CSP.ITEMDATA_ID              AS itemDataId,  " +
                "  coalesce(SUM(CSP.AMOUNT), 0) AS amount   " +
                "FROM OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP " +
                "    ON CS.ID = CSP.SHIPMENT_ID  " +
                "WHERE CS.STATE = 550  " +
                "      AND CS.DELIVERY_DATE =:exsdTime " +
                "GROUP BY CSP.ITEMDATA_ID";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        List<Object[]> objects = query.getResultList();
        return objects;
    }

    /*StockEnough*/
    public BigDecimal getByStockEnough(String itemDataId) {
        String sql = "SELECT coalesce(SUM(STU.AMOUNT - STU.RESERVED_AMOUNT), 0) AS StockEnough " +
                "FROM INV_STOCKUNIT STU " +
                "  LEFT JOIN INV_UNITLOAD AS UL ON STU.UNITLOAD_ID = UL.ID " +
                "WHERE UL.STORAGELOCATION_ID " +
                "      IN (SELECT STL.ID " +
                "          FROM MD_STORAGELOCATION STL " +
                "            LEFT JOIN MD_AREA A ON STL.AREA_ID = A.ID " +
                "          WHERE A.NAME = 'Picking_Zone') " +
                "      AND STU.ITEMDATA_ID =:itemDataId ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("itemDataId", itemDataId);
        BigDecimal stockEnough = (BigDecimal) query.getResultList().get(0);
        return stockEnough;
    }

    /* Replenishing */
    public BigDecimal getByReplenishing(String itemDataId) {
        String sql = "SELECT coalesce(SUM(STU.AMOUNT), 0) AS Replenishing " +
                "FROM INV_STOCKUNIT STU " +
                "  LEFT JOIN INV_STOCKUNITRECORD SUR ON STU.ID = SUR.TO_STOCKUNIT " +
                "WHERE SUR.FROM_STORAGELOCATION " +
                "      IN (SELECT STL.NAME " +
                "          FROM MD_STORAGELOCATION STL " +
                "            LEFT JOIN MD_AREA A  ON STL.AREA_ID = A.ID " +
                "          WHERE A.NAME = 'Buffer_Zone') " +
                "      AND STU.ITEMDATA_ID =:itemDataId ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("itemDataId", itemDataId);
        BigDecimal replenishing = (BigDecimal) query.getResultList().get(0);
        return replenishing;
    }

    /*TotalReplenishment*/
    public BigDecimal getByTotalReplenishment(LocalDateTime exsdTime) {
        String sql = "SELECT coalesce(SUM(CSP.AMOUNT), 0) AS TotalReplenishment " +
                "FROM OB_CUSTOMERSHIPMENT AS CS, " +
                "  OB_CUSTOMERSHIPMENTPOSITION AS CSP, " +
                "  OB_CUSTOMERORDER AS CO " +
                "WHERE CS.ID = CSP.SHIPMENT_ID " +
                "      AND CS.ORDER_ID = CO.ID " +
                "      AND CS.STATE = 550 " +
                "      AND CO.DELIVERY_DATE =:exsdTime  " +
                "GROUP BY CO.DELIVERY_DATE ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal totalReplenishment = BigDecimal.ZERO;
        try {
            totalReplenishment = (BigDecimal) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return totalReplenishment;
    }

    /*Pending*/
    public BigDecimal getByPending(LocalDateTime exsdTime) {
        String sql = " SELECT COALESCE((AMOUNT1-AMOUNT2),0) AS pending FROM " +
                "  ((SELECT COALESCE(SUM(CSP.AMOUNT),0) AS AMOUNT1 " +
                "FROM OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID WHERE CS.DELIVERY_DATE =:exsdTime AND CSP.STATE <= '300') A " +
                "   LEFT JOIN " +
                "  (SELECT COALESCE(SUM(CSP.AMOUNT),0) AS AMOUNT2 " +
                "FROM OB_CUSTOMERSHIPMENT CS " +
                "LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "  WHERE CSP.ID IN " +
                "        (SELECT DISTINCT POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "         FROM OB_PICKINGORDERPOSITION POP " +
                "           LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID) AND CS.DELIVERY_DATE =:exsdTime AND CSP.STATE <= '300')A1 ON 1=1 " +
                ")   ";

//        String sql = " SELECT COALESCE(sum(P.amount+D.amount4),0) amount0 " +
//                "FROM " +
//                "  (SELECT COALESCE(sum(A.amount1-B.amount2-C.amount3),0) amount " +
//                "FROM( " +
////                "    #  customSfmentPosition 状态小于600 " +
//                "    (SELECT COALESCE(CSP.AMOUNT,0) AS amount1, " +
//                "            CSP.SHIPMENT_ID AS shipmentId " +
//                "     FROM OB_CUSTOMERSHIPMENT CS " +
//                "       LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
//                "     WHERE CSP.STATE < 600 AND CSP.AMOUNT_PICKED > 0) A " +
//                "    LEFT JOIN " +
////                "    #   customSfmentPosition 状态小于600 sfment 对应的 pickOrderPosition 中状态 > 600的已拣数量 " +
//                "    (SELECT COALESCE(PC.AMOUNT_PICKED,0) AS amount2, " +
//                "            PC.CUSTOMERSHIPMENTPOSITION_ID AS shipmentId " +
//                "     FROM OB_CUSTOMERSHIPMENT CS " +
//                "       LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
//                "       LEFT JOIN OB_PICKINGORDERPOSITION PC ON CSP.ID = PC.CUSTOMERSHIPMENTPOSITION_ID " +
//                "       LEFT JOIN OB_PICKINGORDER PR ON PC.PICKINGORDER_ID = PR.ID " +
//                "     WHERE PC.STATE >= 600 AND PC.STATE <> 800 AND PC.AMOUNT_PICKED >0 ) B " +
//                "      ON A.shipmentId = B.shipmentId " +
//                "    LEFT JOIN " +
////                "    #   customSfmentPosition 状态小于600 sfment 对应的 pickOrderPosition 中状态 < 600 且 >= 300 " +
//                "    (SELECT COALESCE(PC.AMOUNT,0) AS amount3, " +
//                "            PC.CUSTOMERSHIPMENTPOSITION_ID AS shipmentId " +
//                "     FROM OB_CUSTOMERSHIPMENT CS " +
//                "       LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
//                "       LEFT JOIN OB_PICKINGORDERPOSITION PC ON CSP.ID = PC.CUSTOMERSHIPMENTPOSITION_ID " +
//                "       LEFT JOIN OB_PICKINGORDER PR ON PC.PICKINGORDER_ID = PR.ID " +
//                "     WHERE PC.STATE >= 300 AND PC.STATE < 600 ) C " +
//                "      ON A.shipmentId = C.shipmentId " +
//                "  )) P LEFT JOIN " +
////                "  #   存在于customSfmentPosition 但不存在pickorderposition中 " +
//                "  (SELECT COALESCE(SUM(CSP.AMOUNT),0) AS amount4 " +
//                "   FROM OB_CUSTOMERSHIPMENT CS " +
//                "     LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
//                "   WHERE CSP.ID NOT IN " +
//                "         (SELECT DISTINCT POP.CUSTOMERSHIPMENTPOSITION_ID " +
//                "          FROM OB_PICKINGORDERPOSITION POP " +
//                "            LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID) AND CSP.STATE <= '300' ) D " +
//                "  ON 1 = 1 ";
//
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal pending = (BigDecimal) query.getResultList().get(0);
        return pending;
    }

    /*ReadyToPick  未生成批次在 OB_PICKINGORDERPOSITION 中无数据 */
    public BigDecimal getByReadyToPick(LocalDateTime exsdTime) {
        String sql = "SELECT coalesce(SUM(POP.AMOUNT), 0) AS ReadyToPick " +
                "FROM OB_PICKINGORDERPOSITION POP " +
                "WHERE POP.PICKINGORDER_ID IS NULL " +
                "      AND POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "          IN (SELECT CSP.ID " +
                "              FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
                "                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
                "              WHERE CS.DELIVERY_DATE =:exsdTime )";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal readyToPick = (BigDecimal) query.getResultList().get(0);
        return readyToPick;
    }


    /*PickingNotYetPicked */
    public BigDecimal getByPickingNotYetPicked(LocalDateTime exsdTime) {
        String sql = "SELECT coalesce(SUM(POP.AMOUNT - POP.AMOUNT_PICKED), 0) AS PickingNotYetPicked " +
                "FROM OB_PICKINGORDERPOSITION POP " +
                "  LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID " +
                "WHERE PO.STATE < 600 AND POP.STATE < 600 " +
                "      AND POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "          IN (SELECT CSP.ID " +
                "              FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
                "                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
                "              WHERE CS.DELIVERY_DATE =:exsdTime ) " +
                "      AND POP.PICKINGORDER_ID IS NOT NULL ";
         /* 这种计算方法会包含未生成批次的数据 */
//        String sql = "SELECT coalesce(SUM(CSP.AMOUNT - CSP.AMOUNT_PICKED), 0) AS PickingNotYetPicked    " +
//                "FROM    " +
//                "  OB_CUSTOMERSHIPMENT CS    " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID    " +
//                "WHERE CS.DELIVERY_DATE = :exsdTime    " +
//                "AND CSP.STATE <= '600'";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal pickingNotYetPicked = (BigDecimal) query.getSingleResult();
        return pickingNotYetPicked;
    }

    /*PickingPicked*/
    public BigDecimal getByPickingPicked(LocalDateTime exsdTime) {
//        String sql = "SELECT coalesce(SUM(POP.AMOUNT_PICKED), 0) AS PickingPicked " +
//                "FROM OB_PICKINGORDERPOSITION POP " +
//                "WHERE POP.CUSTOMERSHIPMENTPOSITION_ID " +
//                "      IN (SELECT CSP.ID " +
//                "          FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
//                "            LEFT JOIN OB_CUSTOMERSHIPMENT CS  ON CSP.SHIPMENT_ID = CS.ID " +
//                "          WHERE CS.DELIVERY_DATE =:exsdTime )";

//        String sql = " SELECT coalesce(SUM(POP.AMOUNT_PICKED), 0) AS PickingPicked " +
//                "FROM " +
//                "  OB_CUSTOMERSHIPMENT CS " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
//                "  LEFT JOIN OB_PICKINGORDERPOSITION POP ON CSP.ID = POP.CUSTOMERSHIPMENTPOSITION_ID " +
//                "WHERE " +
//                "  CS.DELIVERY_DATE = :exsdTime  " +
//                "  AND POP.AMOUNT_PICKED > 0 " +
//                "  AND CSP.ID NOT IN " +
//                "      (SELECT P.CUSTOMERSHIPMENTPOSITION_ID " +
//                "       FROM OB_PICKINGORDERPOSITION P) ";

//        String sql = "SELECT coalesce(SUM(CSP.AMOUNT_PICKED), 0) AS PickingPicked   " +
//                " FROM   " +
//                "  OB_CUSTOMERSHIPMENT CS   " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID   " +
//                " WHERE   " +
//                "  CS.DELIVERY_DATE = :exsdTime   " +
//                " AND CSP.STATE <= '605' AND CS.STATE <> '1000' ";

		String sql = " SELECT coalesce(SUM(POP.AMOUNT_PICKED), 0) AS PickingPicked    " +
				"                   FROM          " +
				"                    OB_CUSTOMERSHIPMENT CS          " +
				"                    LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID    " +
				"                    LEFT JOIN OB_PICKINGORDERPOSITION POP ON CSP.ID = POP.CUSTOMERSHIPMENTPOSITION_ID    " +
				"                      LEFT JOIN OB_PICKINGUNITLOAD PUL ON POP.PICKTOUNITLOAD_ID = PUL.ID    " +
				"                   WHERE    " +
				"                    CS.DELIVERY_DATE = :exsdTime   " +
				"                   AND (CSP.STATE <= '600' OR (CSP.STATE = '605' AND PUL.STATE = '600')) ";
   //      String sql = " SELECT coalesce(SUM(CSP.AMOUNT_PICKED), 0) AS PickingPicked " +
    //            "                                   FROM " +
   //             "                                    OB_CUSTOMERSHIPMENT CS " +
    //            "                                    LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
    //            "                                   WHERE " +
   //             "                                    CS.DELIVERY_DATE = :exsdTime " +
   //             "                                   AND CSP.STATE < '600' "; -->
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal pickingPicked = (BigDecimal) query.getResultList().get(0);
        return pickingPicked;
    }

    /*Rebatched*/
    public BigDecimal getByRebatched(LocalDateTime exsdTime) {
        String sql = "SELECT coalesce(SUM(POP.AMOUNT_PICKED), 0) AS Rebatched   " +
                "    FROM OB_PICKINGORDERPOSITION POP   " +
                "    LEFT JOIN OB_PICKINGUNITLOAD PUL ON POP.PICKTOUNITLOAD_ID = PUL.ID   " +
                "    WHERE POP.CUSTOMERSHIPMENTPOSITION_ID   " +
                "    IN (SELECT CSP.ID   " +
                "    FROM OB_CUSTOMERSHIPMENTPOSITION CSP   " +
                "    LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID   " +
                "    WHERE CS.DELIVERY_DATE = :exsdTime  " +
                "     AND (CS.STATE = '610' OR CS.STATE= '605') )   " +
                " AND PUL.STATE = '610'  ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal rebatched = (BigDecimal) query.getResultList().get(0);
        return rebatched;
    }


    /*RebinBuffer*/
    public BigDecimal getByRebinBuffer(LocalDateTime exsdTime) {
//        String sql = "SELECT coalesce(SUM(RBP.AMOUNT), 0) AS RebinBuffer " +
//                "FROM OB_REBINREQUESTPOSITION RBP " +
//                "WHERE RBP.AMOUNT_REBINED = 0 " +
//                "      AND RBP.CUSTOMERSHIPMENTPOSITION_ID " +
//                "          IN (SELECT CSP.ID " +
//                "              FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
//                "                LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
//                "              WHERE CS.STATE = 620 " +
//                "                    AND CS.DELIVERY_DATE =:exsdTime)";

        String sql = " SELECT COALESCE(SUM(POP.AMOUNT - POP.AMOUNT_REBINED), 0) AS amount       " +
                "FROM OB_CUSTOMERSHIPMENTPOSITION POP       " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID       " +
                " WHERE CS.DELIVERY_DATE = :exsdTime       " +
                " AND POP.STATE = '620'  " +
                "        AND POP.ID NOT IN       " +
                "       ( SELECT RBP.CUSTOMERSHIPMENTPOSITION_ID FROM OB_REBINREQUESTPOSITION RBP WHERE RBP.STATE = 'LOSE') ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal rebinBuffer = (BigDecimal) query.getResultList().get(0);
        return rebinBuffer;
    }

    //    /*Rebined*/
//    public BigDecimal getByRebined(LocalDateTime exsdTime) {
//        String sql = "SELECT (CASE " +
//                "        WHEN (a.Rebined1 - b.Rebined2) < 0 " +
//                "          THEN 0 " +
//                "        ELSE (a.Rebined1 - b.Rebined2) " +
//                "        END) AS Rebined " +
//                "FROM (SELECT coalesce(SUM(RP.AMOUNT), 0) AS Rebined1 " +
//                "      FROM OB_REBINREQUESTPOSITION RP " +
//                "      WHERE RP.CUSTOMERSHIPMENTPOSITION_ID " +
//                "            IN (SELECT CSP.ID " +
//                "                FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
//                "                  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
//                "                WHERE CS.DELIVERY_DATE =:exsdTime)) a, " +
//                "  (SELECT coalesce(SUM(AMOUNT), 0) AS Rebined2 " +
//                "   FROM OB_PACKINGREQUESTPOSITION " +
//                "   WHERE CUSTOMERSHIPMENTPOSITION_ID " +
//                "         IN (SELECT CSP.ID " +
//                "             FROM OB_CUSTOMERSHIPMENTPOSITION CSP " +
//                "               LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
//                "             WHERE CS.DELIVERY_DATE =:exsdTime)) b ";
//        Query query = entityManager.createNativeQuery(sql);
//        query.setParameter("exsdTime", exsdTime);
//        BigDecimal scanVerify = (BigDecimal) query.getResultList().get(0);
//        return scanVerify;
//    }
       /*Rebined*/
    public BigDecimal getByRebined(LocalDateTime exsdTime) {
//        String sql = "SELECT COALESCE(SUM(RBP.AMOUNT_REBINED), 0) AS Rebined  " +
//                "FROM OB_CUSTOMERSHIPMENTPOSITION POP  " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID  " +
//                "  LEFT JOIN OB_REBINREQUESTPOSITION RBP ON POP.ID = RBP.CUSTOMERSHIPMENTPOSITION_ID  " +
//                "WHERE  " +
//                "  CS.ID NOT IN (SELECT CS.ID FROM OB_CUSTOMERSHIPMENT CS WHERE CS.STATE = '1100')  " +
//                "  AND (POP.STATE = '620' OR CS.STATE = '630')  " +
//                "  AND CS.DELIVERY_DATE = :exsdTime  " +
//                "  and RBP.STATE = 'FINISHED'  " ;
//        String sql = "SELECT COALESCE(SUM(POP.AMOUNT_REBINED), 0) AS Rebined     " +
//                "FROM OB_CUSTOMERSHIPMENTPOSITION POP     " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID     " +
//                "WHERE CS.DELIVERY_DATE = :exsdTime     " +
//                "AND CS.ID NOT IN (SELECT CS.ID FROM OB_CUSTOMERSHIPMENT CS WHERE CS.STATE = '1100') ";

        String sql = "SELECT  COALESCE(SUM(amount), 0) AS Rebined FROM(      " +
                "      " +
                "  (SELECT COALESCE(SUM(POP.AMOUNT_REBINED), 0) AS amount      " +
                "     FROM OB_CUSTOMERSHIPMENTPOSITION POP      " +
                "       LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID      " +
                "     WHERE      " +
                "       CS.DELIVERY_DATE = :exsdTime      " +
                "       AND POP.ID IN (      " +
                "         SELECT DISTINCT POP.ID      " +
                "         FROM OB_CUSTOMERSHIPMENTPOSITION POP      " +
                "           LEFT JOIN OB_PICKINGORDERPOSITION P ON POP.ID = P.CUSTOMERSHIPMENTPOSITION_ID      " +
                "           LEFT JOIN OB_PICKINGORDER PO ON P.PICKINGORDER_ID = PO.ID      " +
                "           WHERE PO.STATE = '700'      " +
                "           AND CS.STATE = '630'  " +
                "       )      " +
                "        AND CS.ID NOT IN (SELECT CS.ID FROM OB_CUSTOMERSHIPMENT CS WHERE CS.STATE = '1100'))      " +
                "UNION ALL      " +
                "      (SELECT COALESCE(SUM(POP.AMOUNT_REBINED), 0) AS amount      " +
                "    FROM OB_CUSTOMERSHIPMENTPOSITION POP      " +
                "    LEFT JOIN OB_CUSTOMERSHIPMENT CS ON POP.SHIPMENT_ID = CS.ID      " +
                "    WHERE      " +
                "      CS.DELIVERY_DATE = :exsdTime      " +
                "      AND POP.ID IN (      " +
                "        SELECT DISTINCT POP.ID      " +
                "        FROM OB_CUSTOMERSHIPMENTPOSITION POP      " +
                "          LEFT JOIN OB_PICKINGORDERPOSITION P ON POP.ID = P.CUSTOMERSHIPMENTPOSITION_ID      " +
                "          LEFT JOIN OB_PICKINGORDER PO ON P.PICKINGORDER_ID = PO.ID      " +
                "        WHERE PO.STATE <> '700'      " +
                "      )   " +
                "       AND POP.SHIPMENT_ID NOT IN ( " +
                "             SELECT HOT.SHIPMENT_ID FROM OB_CUSTOMERSHIPMENT_HOTPICK HOT " +
                "                        )     " +
                "      )) A ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal scanVerify = (BigDecimal) query.getResultList().get(0);
        return scanVerify;
    }


    /*ScanVerify*/
    public BigDecimal getByScanVerify(LocalDateTime exsdTime) {
//        String sql = "SELECT coalesce(SUM(PRP.AMOUNT), 0) AS ScanVerify " +
//                "FROM OB_PACKINGREQUEST PR " +
//                "  LEFT JOIN OB_PACKINGREQUESTPOSITION PRP ON PR.ID = PRP.PACKINGREQUEST_ID " +
//                "WHERE PRP.CUSTOMERSHIPMENTPOSITION_ID " +
//                "      IN (SELECT CSP.ID " +
//                "          FROM OB_CUSTOMERSHIPMENT CS " +
//                "            LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
//                "          WHERE CSP.STATE = '640' " +
//                "                AND CS.DELIVERY_DATE =:exsdTime) ";
        String sql = " SELECT coalesce(SUM(CSP.AMOUNT), 0) AS ScanVerify " +
                "FROM OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_PACKINGREQUEST PR ON CS.ID = PR.CUSTOMERSHIPMENT_ID " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "WHERE " +
                //               "  PR.WEIGHT IS NOT NULL " +
                "   CS.DELIVERY_DATE =:exsdTime " +
//                "  AND CS.STATE > 600 " +
                "  AND CS.STATE = 640 ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal scanVerify = (BigDecimal) query.getResultList().get(0);
        return scanVerify;
    }

    /*Packed*/
    public BigDecimal getByPacked(LocalDateTime exsdTime) {
        String sql = "SELECT coalesce(SUM(CSP.AMOUNT), 0) AS Packed  " +
                "FROM OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP  ON CS.ID = CSP.SHIPMENT_ID " +
                "WHERE CS.STATE = '650' " +
                "      AND CS.DELIVERY_DATE =:exsdTime ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal packed = (BigDecimal) query.getResultList().get(0);
        return packed;
    }

    /*Problem*/
    public BigDecimal getByProblem(LocalDateTime exsdTime) {
        String sql = " SELECT DISTINCT coalesce(SUM(CSP.AMOUNT), 0) AS Problem  " +
                " FROM OB_CUSTOMERSHIPMENT CS  " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID  " +
                " WHERE  " +
                "  CS.DELIVERY_DATE = :exsdTime " +
                "  AND CS.state= '1100' " +
                "  AND CSP.ID  " +
                "      IN (SELECT DISTINCT CSP.ID  " +
                "          FROM OB_CUSTOMERSHIPMENTPOSITION CSP  " +
                "            LEFT JOIN OB_PICKINGORDERPOSITION PC ON CSP.ID = PC.CUSTOMERSHIPMENTPOSITION_ID  " +
                "            LEFT JOIN OB_PICKINGORDER PR ON PC.PICKINGORDER_ID = PR.ID  " +
                "          WHERE 1 = 1   " +
                "          AND PR.STATE = '700'  " +
                "      )  ";
//        String sql = "SELECT coalesce(SUM(CSP.AMOUNT), 0) AS Problem " +
//                "FROM OBP_OBPROBLEM OB " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON OB.SHIPMENT_ID = CS.ID " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
//                "WHERE OB.STATE = 'unsolved' " +
//                "      AND CS.DELIVERY_DATE =:exsdTime ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal problem = (BigDecimal) query.getResultList().get(0);
        return problem;
    }

    /*Sorted*/
    public BigDecimal getBySorted(LocalDateTime exsdTime) {
        String sql = "SELECT coalesce(SUM(CSP.AMOUNT), 0) AS Sorted " +
                "FROM OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "WHERE CS.STATE = '660' " +
                "      AND CS.DELIVERY_DATE =:exsdTime  ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal packed = (BigDecimal) query.getResultList().get(0);
        return packed;
    }

    /*Loaded*/
    public BigDecimal getByLoaded(LocalDateTime exsdTime) {
        String sql = " SELECT coalesce(SUM(CSP.AMOUNT), 0) AS Loaded    " +
                "                FROM OB_CUSTOMERSHIPMENT CS    " +
                "                  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID    " +
                "                WHERE CS.STATE = '670'    " +
                "                     AND CS.DELIVERY_DATE = :exsdTime ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal loaded = (BigDecimal) query.getResultList().get(0);
        return loaded;
    }

    /*Manifested*/
    public BigDecimal getByManifested(LocalDateTime exsdTime) {
        String sql = " SELECT coalesce(SUM(CSP.AMOUNT), 0) AS Manifested    " +
                "                FROM OB_CUSTOMERSHIPMENT CS    " +
                "                  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID    " +
                "                WHERE CS.STATE = '680'    " +
                "                     AND CS.DELIVERY_DATE = :exsdTime ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exsdTime", exsdTime);
        BigDecimal manifested = (BigDecimal) query.getResultList().get(0);
        return manifested;

    }

}
