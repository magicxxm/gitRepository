package com.mushiny.wms.report.query.hql;

import com.mushiny.wms.report.query.dto.PickDTO;
import com.mushiny.wms.report.query.dto.picked.Picked;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Component
public class PickedQuery implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;


    //    # 所有ppName 发货时间，区域，拣货总数量，已拣数量，未拣数量
    public List<Picked> getPicked(String ppName, String zoneName, String deliveryDate) {

//        String sql = "SELECT " +
//                "  PP.NAME                             AS ppName, " +
//                "  CS.DELIVERY_DATE                    AS deliveryDate, " +
//                "  Z.NAME                              AS zoneName, " +
//                "  coalesce(sum(POR.AMOUNT),0)         AS total, " +
//                "  coalesce(sum(POR.AMOUNT_PICKED),0)  AS picked, " +
//                "  coalesce(sum(POR.AMOUNT),0)-coalesce(sum(POR.AMOUNT_PICKED),0) AS notPicked " +
//                "FROM OB_PICKINGORDERPOSITION POR " +
////                "  #   PpName " +
//                "  LEFT JOIN OB_PICKINGORDER PO ON POR.PICKINGORDER_ID = PO.ID " +
//                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
//                " " +
////                "  #   订单发货时间 " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POR.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
//                " " +
////                "  #  区域 " +
//                "  LEFT JOIN INV_STOCKUNIT ST ON POR.PICKFROMSTOCKUNIT_ID = ST.ID " +
//                "  LEFT JOIN INV_UNITLOAD UL ON ST.UNITLOAD_ID = UL.ID " +
//                "  LEFT JOIN MD_STORAGELOCATION STL ON UL.STORAGELOCATION_ID = STL.ID " +
//                "  LEFT JOIN MD_ZONE Z ON STL.ZONE_ID = Z.ID " +
//                " WHERE CS.DELIVERY_DATE IS NOT NULL " +
//                " AND Z.NAME IS NOT NULL ";

        String sql = " SELECT " +
                "        A.ppName                             AS ppName, " +
                "        A.deliveryDate                       AS deliveryDate, " +
                "        A.zoneName                           AS zoneName, " +
                "        A.total                              AS total, " +
                "        A.picked                             AS picked, " +
                "        A.notPicked                          AS notPicked , " +
                "        IFNULL(B.pending,0)                          AS pending " +
                " FROM " +
                "  ((SELECT" +
                "  PP.NAME                             AS ppName, " +
                "  CS.DELIVERY_DATE                    AS deliveryDate, " +
                "  Z.NAME                              AS zoneName, " +
                "  coalesce(sum(POR.AMOUNT),0)         AS total, " +
                "  coalesce(sum(POR.AMOUNT_PICKED),0)  AS picked, " +
                "  coalesce(sum(POR.AMOUNT),0)-coalesce(sum(POR.AMOUNT_PICKED),0) AS notPicked " +
                "FROM OB_PICKINGORDERPOSITION POR " +
//                "                  #   PpName" +
                "  LEFT JOIN OB_PICKINGORDER PO ON POR.PICKINGORDER_ID = PO.ID " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
//                "                  #   订单发货时间" +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POR.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
//                "                  #  区域" +
                "  LEFT JOIN INV_STOCKUNIT ST ON POR.PICKFROMSTOCKUNIT_ID = ST.ID " +
                "  LEFT JOIN INV_UNITLOAD UL ON ST.UNITLOAD_ID = UL.ID " +
                "  LEFT JOIN MD_STORAGELOCATION STL ON UL.STORAGELOCATION_ID = STL.ID " +
                "  LEFT JOIN MD_ZONE Z ON STL.ZONE_ID = Z.ID " +
                " WHERE 1=1 " +
                " AND  CS.DELIVERY_DATE IS NOT NULL " +
                " AND Z.NAME IS NOT NULL " +
                " AND PP.NAME = '" + ppName + "' " +
                " AND Z.NAME = ' " + zoneName + " ' " +
                " AND POR.STATE NOT IN ('800','1000') " +
//                " AND CS.DELIVERY_DATE LIKE '" + deliveryDate + " ' " +
                " GROUP BY ppName,deliveryDate, zoneName " +
                " ) A " +
                " LEFT JOIN" +
//                "                  #  pending " +
                "(SELECT coalesce(sum(CSP.AMOUNT),0) AS pending, " +
                " CS.DELIVERY_DATE                  AS deliveryDate" +
                " FROM OB_CUSTOMERSHIPMENT CS " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                " WHERE CSP.ID NOT IN " +
                "(SELECT DISTINCT POP.CUSTOMERSHIPMENTPOSITION_ID " +
                " FROM OB_PICKINGORDERPOSITION POP " +
                "   LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID) " +
//                " AND CS.DELIVERY_DATE LIKE :deliveryDate " +
                " AND CS.STATE <= '300' GROUP BY CS.DELIVERY_DATE)B " +
                " ON A.deliveryDate = B.deliveryDate) ";

//        if (ppName != null && !ppName.equals("")) {
//            sql = sql + " AND PP.NAME =:ppName ";
//        }
//        if (zoneName != null && !zoneName.equals("")) {
//            sql = sql + " AND Z.NAME =:zoneName ";
//        }
//        if (deliveryDate != null && !deliveryDate.equals("")) {
//            sql = sql + " AND CS.DELIVERY_DATE LIKE :deliveryDate ";
//        }
//        sql = sql + " GROUP BY ppName, deliveryDate, zoneName ";
        if (deliveryDate != null && !deliveryDate.equals("")) {
            sql = sql + " WHERE A.deliveryDate  LIKE :deliveryDate ";
        }
        Query query = entityManager.createNativeQuery(sql);
//        query.setParameter("ppName", ppName);
//        query.setParameter("zoneName", zoneName);
        if (deliveryDate != null && !deliveryDate.equals("")) {

            query.setParameter("deliveryDate", deliveryDate);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Picked.class));
        List<Picked> entities = query.getResultList();
        return entities;
    }

    //    # 所有ppName 发货时间，区域，拣货总数量，已拣数量，未拣数量
    public List<Picked> getPickedzone(String deliveryDate) {

//        String sql = "SELECT " +
//                "  PP.NAME                             AS ppName, " +
//                "  CS.DELIVERY_DATE                    AS deliveryDate, " +
//                "  Z.NAME                              AS zoneName, " +
//                "  coalesce(sum(POR.AMOUNT),0)         AS total, " +
//                "  coalesce(sum(POR.AMOUNT_PICKED),0)  AS picked, " +
//                "  coalesce(sum(POR.AMOUNT),0)-coalesce(sum(POR.AMOUNT_PICKED),0) AS notPicked " +
//                "FROM OB_PICKINGORDERPOSITION POR " +
////                "  #   PpName " +
//                "  LEFT JOIN OB_PICKINGORDER PO ON POR.PICKINGORDER_ID = PO.ID " +
//                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
//                " " +
////                "  #   订单发货时间 " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POR.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
//                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
//                " " +
////                "  #  区域 " +
//                "  LEFT JOIN INV_STOCKUNIT ST ON POR.PICKFROMSTOCKUNIT_ID = ST.ID " +
//                "  LEFT JOIN INV_UNITLOAD UL ON ST.UNITLOAD_ID = UL.ID " +
//                "  LEFT JOIN MD_STORAGELOCATION STL ON UL.STORAGELOCATION_ID = STL.ID " +
//                "  LEFT JOIN MD_ZONE Z ON STL.ZONE_ID = Z.ID " +
//                " WHERE CS.DELIVERY_DATE IS NOT NULL " +
//                " AND Z.NAME IS NOT NULL ";

        String sql = " SELECT " +
                "   A.ppName                             AS ppName, " +
                "        A.deliveryDate                       AS deliveryDate, " +
                "        A.zoneName                           AS zoneName, " +
                "        A.total                              AS total, " +
                "        A.picked                             AS picked, " +
                "        A.notPicked                          AS notPicked , " +
                "        IFNULL(B.pending ,0)                 AS pending " +
                " FROM" +
                "  ((SELECT" +
                "  PP.NAME                             AS ppName, " +
                "  CS.DELIVERY_DATE                    AS deliveryDate, " +
                "  Z.NAME                              AS zoneName, " +
                "  coalesce(sum(POR.AMOUNT),0)         AS total, " +
                "  coalesce(sum(POR.AMOUNT_PICKED),0)  AS picked, " +
                "  coalesce(sum(POR.AMOUNT),0)-coalesce(sum(POR.AMOUNT_PICKED),0) AS notPicked " +
                "FROM OB_PICKINGORDERPOSITION POR " +
//                "                  #   PpName" +
                "  LEFT JOIN OB_PICKINGORDER PO ON POR.PICKINGORDER_ID = PO.ID " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
//                "                  #   订单发货时间" +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POR.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
//                "                  #  区域" +
                "  LEFT JOIN INV_STOCKUNIT ST ON POR.PICKFROMSTOCKUNIT_ID = ST.ID " +
                "  LEFT JOIN INV_UNITLOAD UL ON ST.UNITLOAD_ID = UL.ID " +
                "  LEFT JOIN MD_STORAGELOCATION STL ON UL.STORAGELOCATION_ID = STL.ID " +
                "  LEFT JOIN MD_ZONE Z ON STL.ZONE_ID = Z.ID " +
                " WHERE" +
                "   CS.DELIVERY_DATE IS NOT NULL " +
                " AND Z.NAME IS NOT NULL " +
                " AND POR.STATE NOT IN ('800','1000') " +
//                " AND CS.DELIVERY_DATE LIKE :deliveryDate " +
                "   GROUP BY ppName, deliveryDate ,zoneName " +
                " ) A " +
                " LEFT JOIN" +
//                "                  #  pending " +
                "(SELECT coalesce(sum(CSP.AMOUNT),0) AS pending, " +
                " CS.DELIVERY_DATE                   AS deliveryDate " +
                " FROM OB_CUSTOMERSHIPMENT CS" +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                " WHERE CSP.ID NOT IN " +
                "(SELECT DISTINCT POP.CUSTOMERSHIPMENTPOSITION_ID " +
                " FROM OB_PICKINGORDERPOSITION POP " +
                "   LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID) " +
//                "   AND CS.DELIVERY_DATE LIKE :deliveryDate " +
                "   AND CS.STATE <= '300' GROUP BY CS.DELIVERY_DATE )B " +
                " ON A.deliveryDate = B.deliveryDate) ";

//        if (ppName != null && !ppName.equals("")) {
//            sql = sql + " AND PP.NAME =:ppName ";
//        }
//        if (zoneName != null && !zoneName.equals("")) {
//            sql = sql + " AND Z.NAME =:zoneName ";
//        }
        if (deliveryDate != null && !deliveryDate.equals("")) {
            sql = sql + " WHERE A.deliveryDate  LIKE :deliveryDate ";
        }
//        sql = sql + " GROUP BY ppName, deliveryDate, zoneName ";
        Query query = entityManager.createNativeQuery(sql);
//        if (ppName != null && !ppName.equals("")) {
//            query.setParameter("ppName", ppName);
//        }
//        if (zoneName != null && !zoneName.equals("")) {
//            query.setParameter("zoneName", zoneName);
//        }
//        if (deliveryDate != null && !deliveryDate.equals("")) {
//            query.setParameter("deliveryDate", deliveryDate + "%");
//        }
        if (deliveryDate != null && !deliveryDate.equals("")) {
            query.setParameter("deliveryDate", deliveryDate);
        }


        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Picked.class));
        List<Picked> entities = query.getResultList();
        return entities;
    }

    public List<Picked> getPickedZoneTotal(String zoneName) {
        //# 各 区域，拣货总数量，已拣数量，未拣数量
        String sql = "SELECT  " +
                "  Z.NAME                              AS zoneName,  " +
                " coalesce(sum(POR.AMOUNT),0)          AS total, " +
                "  coalesce(sum(POR.AMOUNT_PICKED),0)  AS picked, " +
                "  coalesce(sum(POR.AMOUNT),0)-coalesce(sum(POR.AMOUNT_PICKED),0) AS notPicked " +
                "FROM OB_PICKINGORDERPOSITION POR  " +
//                "  #  区域  " +
                "  LEFT JOIN INV_STOCKUNIT ST ON POR.PICKFROMSTOCKUNIT_ID = ST.ID  " +
                "  LEFT JOIN INV_UNITLOAD UL ON ST.UNITLOAD_ID = UL.ID  " +
                "  LEFT JOIN MD_STORAGELOCATION STL ON UL.STORAGELOCATION_ID = STL.ID  " +
                "  LEFT JOIN MD_ZONE Z ON STL.ZONE_ID = Z.ID " +
                " WHERE Z.NAME IS NOT NULL  ";

        if (zoneName != null && !zoneName.equals("")) {
            sql = sql + " AND Z.NAME =:zoneName ";
        }
        sql = sql + " GROUP BY zoneName ORDER BY zoneName ";
        Query query = entityManager.createNativeQuery(sql);
        if (zoneName != null && !zoneName.equals("")) {
            query.setParameter("zoneName", zoneName);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Picked.class));
        List<Picked> entities = query.getResultList();
        return entities;
    }

    public List<Picked> getPickedDateTotal(String deliveryDate) {
//        # 各 发货时间，拣货总数量，已拣数量，未拣数量
        String sql = "SELECT " +
                "  CS.DELIVERY_DATE                                                   AS deliveryDate, " +
                "  coalesce(sum(POR.AMOUNT), 0)                                       AS total, " +
                "  coalesce(sum(POR.AMOUNT_PICKED), 0)                                AS picked, " +
                "  coalesce(sum(POR.AMOUNT), 0) - coalesce(sum(POR.AMOUNT_PICKED), 0) AS notPicked " +
                "FROM OB_PICKINGORDERPOSITION POR " +
//                "  #   订单发货时间 " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POR.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
                "WHERE CS.DELIVERY_DATE IS NOT NULL ";

        if (deliveryDate != null && !deliveryDate.equals("")) {
            sql = sql + " AND CS.DELIVERY_DATE LIKE :deliveryDate ";
        }
        sql = sql + " GROUP BY deliveryDate ORDER BY deliveryDate ";
        Query query = entityManager.createNativeQuery(sql);
        if (deliveryDate != null && !deliveryDate.equals("")) {
            query.setParameter("deliveryDate", deliveryDate + "%");
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Picked.class));
        List<Picked> entities = query.getResultList();
        return entities;
    }


    public List<Picked> getPickedTest(String ppName, String zoneName, String deliveryDate) {

        String sql = "SELECT " +
                "  PP.NAME                             AS ppName, " +
                "  CS.DELIVERY_DATE                    AS deliveryDate, " +
                "  Z.NAME                              AS zoneName, " +
                "  coalesce(sum(POR.AMOUNT),0)         AS total, " +
                "  coalesce(sum(POR.AMOUNT_PICKED),0)  AS picked, " +
                "  coalesce(sum(POR.AMOUNT),0)-coalesce(sum(POR.AMOUNT_PICKED),0) AS notPicked " +
                "FROM OB_PICKINGORDERPOSITION POR " +
//                "  #   PpName " +
                "  LEFT JOIN OB_PICKINGORDER PO ON POR.PICKINGORDER_ID = PO.ID " +
                "  LEFT JOIN OB_PROCESSPATH PP ON PO.PROCESSPATH_ID = PP.ID " +
                " " +
//                "  #   订单发货时间 " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON POR.CUSTOMERSHIPMENTPOSITION_ID = CSP.ID " +
                "  LEFT JOIN OB_CUSTOMERSHIPMENT CS ON CSP.SHIPMENT_ID = CS.ID " +
                " " +
//                "  #  区域 " +
                "  LEFT JOIN INV_STOCKUNIT ST ON POR.PICKFROMSTOCKUNIT_ID = ST.ID " +
                "  LEFT JOIN INV_UNITLOAD UL ON ST.UNITLOAD_ID = UL.ID " +
                "  LEFT JOIN MD_STORAGELOCATION STL ON UL.STORAGELOCATION_ID = STL.ID " +
                "  LEFT JOIN MD_ZONE Z ON STL.ZONE_ID = Z.ID " +
                " WHERE CS.DELIVERY_DATE IS NOT NULL AND POR.STATE NOT IN ('800','1000') " +
                " AND Z.NAME IS NOT NULL ";

        if (ppName != null && !ppName.equals("")) {
            sql = sql + " AND PP.NAME =:ppName ";
        }
        if (zoneName != null && !zoneName.equals("")) {
            sql = sql + " AND Z.NAME =:zoneName ";
        }
        if (deliveryDate != null && !deliveryDate.equals("")) {
            sql = sql + " AND CS.DELIVERY_DATE LIKE :deliveryDate ";
        }
        sql = sql + " GROUP BY ppName, deliveryDate, zoneName";
        Query query = entityManager.createNativeQuery(sql);

        if (ppName != null && !ppName.equals("")) {
            query.setParameter("ppName", ppName);
        }
        if (zoneName != null && !zoneName.equals("")) {
            query.setParameter("zoneName", zoneName);
        }
        if (deliveryDate != null && !deliveryDate.equals("")) {
            query.setParameter("deliveryDate", deliveryDate + "%");
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Picked.class));
        List<Picked> entities = query.getResultList();
        return entities;

    }
    public List<Picked> getPendingArea(String deliveryDate) {

        String sql = "   SELECT coalesce(sum(CSP.AMOUNT),0) AS pending, " +
                "         'pendingZone'                     AS zoneName " +
                "  FROM OB_CUSTOMERSHIPMENT CS " +
                "    LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "  WHERE CSP.ID NOT IN " +
                "        (SELECT DISTINCT POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "         FROM OB_PICKINGORDERPOSITION POP " +
                "           LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID) " +
                "        AND CSP.STATE <= '300' ";

        if (deliveryDate != null && !deliveryDate.equals("")) {
            sql = sql + " AND CS.DELIVERY_DATE LIKE :deliveryDate ";
        }
        sql = sql + " GROUP BY zoneName";
        Query query = entityManager.createNativeQuery(sql);

        if (deliveryDate != null && !deliveryDate.equals("")) {
            query.setParameter("deliveryDate", deliveryDate + "%");
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Picked.class));
        List<Picked> entities = query.getResultList();
        return entities;
    }

    public List<Picked> getPendingExsd(String deliveryDate) {

        String sql = "   SELECT coalesce(sum(CSP.AMOUNT),0) AS pending, " +
                "          CS.DELIVERY_DATE  AS  deliveryDate, " +
                "         'pendingZone'                     AS zoneName " +
                "  FROM OB_CUSTOMERSHIPMENT CS " +
                "    LEFT JOIN OB_CUSTOMERSHIPMENTPOSITION CSP ON CS.ID = CSP.SHIPMENT_ID " +
                "  WHERE CSP.ID NOT IN " +
                "        (SELECT DISTINCT POP.CUSTOMERSHIPMENTPOSITION_ID " +
                "         FROM OB_PICKINGORDERPOSITION POP " +
                "           LEFT JOIN OB_PICKINGORDER PO ON POP.PICKINGORDER_ID = PO.ID) " +
                "        AND CSP.STATE <= '300' ";

        if (deliveryDate != null && !deliveryDate.equals("")) {
            sql = sql + " AND CS.DELIVERY_DATE = :deliveryDate ";
        }
        sql = sql + " GROUP BY deliveryDate, zoneName ";
        Query query = entityManager.createNativeQuery(sql);

        if (deliveryDate != null && !deliveryDate.equals("")) {
            query.setParameter("deliveryDate", deliveryDate + "%");
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Picked.class));
        List<Picked> entities = query.getResultList();
        return entities;
    }


}
