package com.mushiny.wms.report.query.hql;


import com.mushiny.wms.report.query.dto.FudDTO;
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

@Component
public class LegacyDataAndFudQuery implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;


    /* 获取 FUD 数据明细  */
    public List<FudDTO> getFud() {

        List<FudDTO> fudDTOS = new ArrayList<>();
        // FUD 订单明细
        List<Object[]> orders = getFudOrder();  // (itemdataID , overTime, amount)
        if (orders != null) {
            for (Object o : orders) {
                Object[] order = (Object[]) o;
                //获取商品bin位库存
                BigDecimal binAmount = getFudStoreBin((String) order[0]);

                //订单数量
                BigDecimal amount = (BigDecimal) order[2];

                if (binAmount.compareTo(amount) >= 0) {
//                    break;
                } else {
                    // 在货框中未及时上架的总数量 = 订单数量-bin数量
                    BigDecimal containerAmount = amount.subtract(binAmount);
                    //获取商品货筐库存
                    List<FudDTO> stores = containerStockerUnit((String) order[0]);
                    //订单数量 - 库存数量 = 初始值 0
                    BigDecimal difference = BigDecimal.ZERO;

                    if (stores != null) {
//                        FudDTO fudDTO = null;
                        //遍历库存
                        for (FudDTO fd : stores) {
                            // 如果容器相同则需取容器的最后一次操作
                            List<Object[]> stockUnitRecord = getRecordMaxTime(fd.getContainerName(), (String) order[0]);
                            String time = String.valueOf(stockUnitRecord.get(0)[3]);
                            List<String> recordType = getRecordType((String) order[0], time);
                            fd.setActivityCode(recordType.get(0));

                            //订单数量 - 库存数量
                            difference = containerAmount.subtract(fd.getAmount());
                            // 如果订单数量大于库存数量，返回库存数量
                            if (containerAmount.compareTo(BigDecimal.ZERO) > 0 && difference.compareTo(BigDecimal.ZERO) >= 0) {
                                fd.setOverTime((String) order[1]);
                                fd.setAmount(fd.getAmount());
                                fudDTOS.add(fd);
                                containerAmount = containerAmount.subtract(fd.getAmount());
//                            //
//                            amount = amount.subtract(fd.getAmount());
                                // 如果订单数量小于库存数量，返回订单数量
                            } else if (containerAmount.compareTo(BigDecimal.ZERO) > 0 && difference.compareTo(BigDecimal.ZERO) < 0) {
                                fd.setOverTime((String) order[1]);
                                fd.setAmount(containerAmount);
                                fudDTOS.add(fd);
                                containerAmount = containerAmount.subtract(fd.getAmount());
//                                break;
                            }
                        }
                    }
                }
//                    else {
//                                fd.setAmount(amount);
//                                fd.setOverTime((String) order[1]);
//                                fudDTOS.add(fd);
//                       break;

//                    }

//                    //遍历库存
//                    for (FudDTO fd : stores) {
//                        //订单数量 - 库存数量
////                        difference = amount.subtract(fd.getAmount());
//                        // difference 大于等于 0
//                        if (amount.compareTo(fd.getAmount()) > 0) {
//                            fd.setOverTime((String) order[1]);
//                            fudDTOS.add(fd);
//                            //
//                            amount = amount.subtract(fd.getAmount());
//                            //如果订单数量小于库存数量，返回订单数量
//                        } else {
//                            fd.setAmount(amount);
//                            fd.setOverTime((String) order[1]);
//                            fudDTOS.add(fd);
//                            break;
//                        }
//                    }

            }
        }
        /*如果 影响客户订单时间 大于 累计时间， 影响订单时间 =  累计时间 */
        List<FudDTO> fudDTOList = new ArrayList<>();
        for (FudDTO fudDTO : fudDTOS) {
            String overTime = totalByOverTime(fudDTO.getTotalTime(), fudDTO.getOverTime());
            fudDTO.setOverTime(overTime);
            fudDTOList.add(fudDTO);
        }

        return fudDTOList;
    }

    /* FUD 订单明细*/
    public List<Object[]> getFudOrder() {
//        String OrderSQL = "SELECT " +
//                "  A.ITEMDATA_ID AS itemDataID, " +
//                "  B.overTime    AS overTime, " +
//                "  A.AMOUNT      AS amount " +
//                "FROM " +
//                "  (SELECT " +
//                "     CSP.ITEMDATA_ID, " +
//                "     coalesce(SUM(CSP.AMOUNT), 0) AS AMOUNT " +
//                "   FROM OB_CUSTOMERSHIPMENT CS " +
//                "     LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP " +
//                "       ON CS.ID = CSP.SHIPMENT_ID " +
//                "   WHERE CS.STATE = 550 " +
//                "   GROUP BY CSP.ITEMDATA_ID) A, " +
//                "  (SELECT " +
//                "     ITEMDATA_ID, " +
//                "     MAX(overTime) AS overTime " +
//                "   FROM (SELECT " +
//                "           CSP.ITEMDATA_ID, " +
//                "           FORMAT((TIMESTAMPDIFF(MINUTE, CS.CREATED_DATE,' " + LocalDateTime.now() +
//                "                                ')) / 1440, 2) AS overTime " +
//                "         FROM OB_CUSTOMERSHIPMENT CS " +
//                "           LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP " +
//                "             ON CS.ID = CSP.SHIPMENT_ID " +
//                "         WHERE CS.STATE = 550) AS d " +
//                "   GROUP BY d.ITEMDATA_ID) B " +
//                "WHERE A.ITEMDATA_ID = B.ITEMDATA_ID ";
        String OrderSQL = " SELECT " +
                "                  A.ITEMDATA_ID AS itemDataID,  " +
                "                  B.overTime    AS overTime,  " +
                "                  A.AMOUNT      AS amount  " +
                "                FROM  " +
                "                  ( " +
                "                    SELECT COALESCE(SUM(CSP.AMOUNT),0) AS AMOUNT ,CSP.ITEMDATA_ID " +
                "                    FROM OB_CUSTOMERSHIPMENT CS " +
                "                      LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "                    WHERE CSP.ID NOT IN " +
                "                          (SELECT DISTINCT POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "                           FROM OB_PICKINGORDERPOSITION POP " +
                "                             LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID) AND CSP.STATE <= '300' GROUP BY CSP.ITEMDATA_ID " +
                "                  ) A,  " +
                "                  (SELECT  " +
                "                     ITEMDATA_ID,  " +
                "                     MAX(overTime) AS overTime  " +
                "                   FROM (  " +
                "                     SELECT " +
                "                       CSP.ITEMDATA_ID, " +
                "                            format((TIMESTAMPDIFF(MINUTE, CS.CREATED_DATE, ' " + LocalDateTime.now() +
                "                                                ')) / 1440, 2) AS overTime " +
                "                     FROM OB_CUSTOMERSHIPMENT CS " +
                "                       LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "                     WHERE CSP.ID NOT IN " +
                "                           (SELECT DISTINCT POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "                            FROM OB_PICKINGORDERPOSITION POP " +
                "                              LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID) AND CSP.STATE <= '300' " +
                "                        ) AS d  " +
                "                   GROUP BY d.ITEMDATA_ID) B  " +
                "                WHERE A.ITEMDATA_ID = B.ITEMDATA_ID;";

        Query query = entityManager.createNativeQuery(OrderSQL);
        List<Object[]> entities = query.getResultList();
        return entities;

    }

    public String getOverTime(String id) {
        String sql = "  SELECT " +
                "  format((TIMESTAMPDIFF(MINUTE, CS.CREATED_DATE, ' " + LocalDateTime.now() +
                "  ')) / 1440, 2) AS overTime " +
                "  FROM OB_CUSTOMERSHIPMENT CS WHERE CS.ID = :id";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("id", id);
        String overTime = query.getResultList().get(0).toString();
        return overTime;
    }


    /* FUD  bin 位明细*/
    public BigDecimal getFudStoreBin(String itemDataID) {
        String storeSQL = " SELECT COALESCE(sum(S.AMOUNT-S.RESERVED_AMOUNT),0) " +
                "  FROM INV_STOCKUNIT S " +
                "LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID " +
                "LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID " +
                "LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID " +
                "WHERE U.STORAGELOCATION_ID IS NOT NULL " +
                "AND SLT.STORAGETYPE = 'BIN' AND U.ENTITY_LOCK < 2 " +
                "      AND S.STATE  = 'Inventory' " +
                "      AND S.ITEMDATA_ID = :itemDataID ";

        Query query = entityManager.createNativeQuery(storeSQL);
        query.setParameter("itemDataID", itemDataID);
        BigDecimal binAmount = (BigDecimal) query.getResultList().get(0);
        return binAmount;
    }

    /* FUD  库存明细*/
    public List<FudDTO> getFudStoreContainer(String itemDataID) {
        String storeSQL = " SELECT DISTINCT " +
                "  I.SKU_NO                                                                         AS skuNo, " +
                "  I.ITEM_NO                                                                        AS skuId, " +
                "  I.NAME                                                                           AS skuName, " +
                "  C.NAME                                                                           AS clientName, " +
                "  SL.NAME                                                                          AS containerName, " +
                "  S.AMOUNT                                                                         AS amount, " +
                "  SR.RECORD_TYPE                                                                   AS activityCode, " +
                "  (CASE " +
                "   WHEN SR.TO_STATE = 'Inventory' " +
                "     THEN '正品' " +
                "   WHEN SR.TO_STATE = 'Pending' " +
                "     THEN '待调查' " +
                "   WHEN SR.TO_STATE = 'Damage' " +
                "     THEN '残品' " +
                "   WHEN SR.TO_STATE = 'Measure' " +
                "     THEN '测量' " +
                "   ELSE ' ' " +
                "   END)                                                                            AS state, " +
                "  S.CREATED_DATE                                                                   AS modifiedDate, " +
                "  format( " +
                "      (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE, ' " + LocalDateTime.now() +
                "                       ')) / 1440, 2) AS totalTime " +
                "FROM INV_STOCKUNIT S " +
                "  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID " +
                "  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID " +
                "  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID " +
                "  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID " +
                "  LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID " +
                "  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT " +
//                "  LEFT JOIN INV_UNITLOAD_SHIPMENT LO ON S.UNITLOAD_ID = LO.UNITLOAD_ID " +
                "WHERE U.STORAGELOCATION_ID IS NOT NULL AND U.ENTITY_LOCK < 2 " +
                " AND S.STATE IN ('Inventory','Measure') " +
//                AND S.STATE NOT IN ('Damage','Pending','Adjust')
                "      AND SLT.STORAGETYPE <> 'BIN' " +
                "      AND S.AMOUNT > 0 " +
                "      AND (SR.RECORD_TYPE NOT IN " +
                "           ('SORT', 'PACK LOSS', 'PACK OVERAGE', 'REBIN LOSS', 'REBIN OVERAGE', 'REBATCH', 'INVENTORY LOSS','REBIN-PACK') " +
                "           OR ((SR.RECORD_TYPE LIKE 'PICK PICK%' AND SR.TO_STATE <> 'INVENTORY') " +
                "               OR (SR.RECORD_TYPE LIKE 'REBIN REBIN%' AND SR.TO_STATE <> 'INVENTORY') " +
                "               OR (SR.RECORD_TYPE IN ('PACK VERIFY', 'PACK DIRECT') AND SR.TO_STATE <> 'INVENTORY') " +
                "               OR ((S.STATE  = 'Inventory' AND SL.NAME <> 'Problem Solved') " +
                "                     OR (S.STATE  = 'Inventory' AND SR.RECORD_TYPE = 'OB PROBLEM SOLVE' AND SL.NAME NOT LIKE 'OBPSW%')) " +
                "           )) " +
                "      AND S.ITEMDATA_ID = :itemDataID  " +
                " ORDER BY SL.NAME ";
        Query query = entityManager.createNativeQuery(storeSQL);
        query.setParameter("itemDataID", itemDataID);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(FudDTO.class));
        List<FudDTO> fudDTOS = query.getResultList();
        return fudDTOS;
    }


    /*如果 影响客户订单时间 大于 累计时间， 影响订单时间 =  累计时间 */
    public String totalByOverTime(String totalTime, String OverTime) {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal over = BigDecimal.ZERO;
        total = new BigDecimal(totalTime);
        over = new BigDecimal(OverTime);
        if (over.compareTo(total) > 0) {
            over = total;
        }
        return over.toString();
    }


    /*车牌遗留数据*/
    public List<FudDTO> getByContainerLegacyData() {
        String sql = "SELECT DISTINCT " +
                "  I.SKU_NO                                                                         AS skuNo, " +
                "  I.ITEM_NO                                                                        AS skuId, " +
                "  I.NAME                                                                           AS skuName, " +
                "  C.NAME                                                                           AS clientName, " +
                "  SL.NAME                                                                          AS containerName, " +
                "  S.AMOUNT                                                                         AS amount, " +
                "  SR.RECORD_TYPE                                                                   AS activityCode, " +
                "  (CASE " +
                "   WHEN SR.TO_STATE = 'Inventory' " +
                "     THEN '正品' " +
                "   WHEN SR.TO_STATE = 'Pending' " +
                "     THEN '待调查' " +
                "   WHEN SR.TO_STATE = 'Damage' " +
                "     THEN '残品' " +
                "   WHEN SR.TO_STATE = 'Measure' " +
                "     THEN '测量' " +
                "   ELSE ' ' " +
                "   END)                                                                            AS state, " +
                "  S.CREATED_DATE                                                                   AS modifiedDate, " +
                "  format( " +
                "      (TIMESTAMPDIFF(MINUTE, S.CREATED_DATE, ' " + LocalDateTime.now() +
                "                       ')) / 1440, 2) AS totalTime " +
                "FROM INV_STOCKUNIT S " +
                "  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID " +
                "  LEFT JOIN MD_ITEMDATA I ON S.ITEMDATA_ID = I.ID " +
                "  LEFT JOIN SYS_CLIENT C ON S.CLIENT_ID = C.ID " +
                "  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID " +
                "  LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID " +
                "  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT " +
                "WHERE U.STORAGELOCATION_ID IS NOT NULL " +
                "      AND S.AMOUNT > 0  AND SR.TO_STATE <> 'Adjust' " +
                "      AND SLT.STORAGETYPE <> 'BIN' " +
                "      AND (SR.RECORD_TYPE NOT IN " +
                "           ('SORT', 'PACK LOSS', 'PACK OVERAGE', 'REBIN LOSS', 'REBIN OVERAGE', 'REBATCH', 'INVENTORY LOSS','REBIN-PACK') " +
                "           OR ((SR.RECORD_TYPE LIKE 'PICK PICK%' AND SR.TO_STATE <> 'INVENTORY') " +
                "               OR (SR.RECORD_TYPE LIKE 'REBIN REBIN%' AND SR.TO_STATE <> 'INVENTORY') " +
                "               OR (SR.RECORD_TYPE IN ('PACK VERIFY', 'PACK DIRECT') AND SR.TO_STATE <> 'INVENTORY') " +
                "               OR ((S.STATE  = 'Inventory' AND SL.NAME <> 'Problem Solved') " +
                "                     OR (S.STATE  = 'Inventory' AND SR.RECORD_TYPE = 'OB PROBLEM SOLVE' AND SL.NAME NOT LIKE 'OBPSW%')) " +
                "           )) " +
                "ORDER BY S.CREATED_DATE ";
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(FudDTO.class));
//        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<FudDTO> fudDTOs = query.getResultList();
        return fudDTOs;
    }

    public List<Object[]> getRecordMaxTime(String container, String itemDataID) {
        String sql = " SELECT SR.ID AS id , SR.MODIFIED_DATE AS times " +
                " FROM INV_STOCKUNIT S " +
                "  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT " +
                "  LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID " +
                "  LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID " +
                " WHERE S.ITEMDATA_ID = :itemDataID " +
                " AND SL.NAME = :container ORDER BY SR.MODIFIED_DATE DESC  ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("container", container);
        query.setParameter("itemDataID", itemDataID);
//        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Object[]> stockUnitRecord = query.getResultList();
        return stockUnitRecord;
    }

    public List<String> getRecordType(String itemDataID, String time) {
        String sql = " SELECT SR.RECORD_TYPE AS activityCode FROM INV_STOCKUNIT S  " +
                "  LEFT JOIN INV_STOCKUNITRECORD SR ON S.ID = SR.TO_STOCKUNIT  " +
                " WHERE S.ITEMDATA_ID = :itemDataID AND SR.MODIFIED_DATE = :time ORDER BY SR.MODIFIED_DATE  ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("itemDataID", itemDataID);
        query.setParameter("time", time);
        List<String> recordType = query.getResultList();
        return recordType;

    }

    public List<FudDTO> getShipmentNoNotNull(String containerName, String itemDataID) {
        String sql = " SELECT st.`NAME` AS containerName,d.`Id` AS itemDataID FROM INV_STOCKUNIT s ,INV_UNITLOAD u,MD_STORAGELOCATION st,MD_ITEMDATA d  " +
                "WHERE s.`UNITLOAD_ID` = u.`ID` AND u.`STORAGELOCATION_ID` = st.`ID` AND s.`ITEMDATA_ID` = d.`ID`  " +
                "      AND st.`NAME` = :containerName AND d.`Id` = :itemDataID  " +
                "      AND u.`ID` IN (SELECT us.UNITLOAD_ID FROM INV_UNITLOAD_SHIPMENT us) ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("containerName", containerName);
        query.setParameter("itemDataID", itemDataID);
        List<FudDTO> shipmentNo = query.getResultList();
        return shipmentNo;

    }

    // 货筐库存
    public List<FudDTO> containerStockerUnit(String itemDateId) {
        // 获取商品货筐库存
        List<FudDTO> stores = getFudStoreContainer(itemDateId);
        List<FudDTO> legacys = new ArrayList<>();
        for (FudDTO legacyDate : stores) {
            if (legacyDate.getContainerName().contains("OBPS") || legacyDate.getContainerName().contentEquals("Problem Solved")
                    || legacyDate.getContainerName().contentEquals("Problem Solving") || legacyDate.getContainerName().contains("Problem")) {
//                        if (legacyDate.getActivityCode().contentEquals("OB PROBLEM SOLVE")
//                                && legacyDate.getState().contentEquals("正品")) {
                legacys.add(legacyDate);
//              }
            }
            List<FudDTO> getShipmentNoNotNull = getShipmentNoNotNull(legacyDate.getContainerName(), itemDateId);
            if (getShipmentNoNotNull != null && getShipmentNoNotNull.size() > 0) {
                legacys.add(legacyDate);
            }
        }
        for (FudDTO legacy : legacys) {
            stores.remove(legacy);
        }
        return stores;
    }

}
