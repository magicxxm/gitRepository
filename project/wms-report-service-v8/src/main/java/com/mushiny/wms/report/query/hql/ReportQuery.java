package com.mushiny.wms.report.query.hql;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReportQuery implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

//    //获取发货时间列表
//    public List<LocalDateTime> getByStartOrEndTime(String startTime, String endTime) {
//
//        String sql = "SELECT d.deliveryTime FROM DeliveryTime d  where 1=1 ";
//        if (startTime != null && !startTime.equals("")) {
//            sql = sql + " and d.deliveryTime >=:startTime ";
//        }
//        if (endTime != null && !endTime.equals("")) {
//            sql = sql + " and  d.deliveryTime <=:endTime ";
//        }
//        sql = sql + " order by d.deliveryTime ";
//
//        Query query = entityManager.createQuery(sql);
//
//        if (startTime != null && !startTime.equals("")) {
//            query.setParameter("startTime", LocalDateTime.parse(startTime));
//        }
//        if (endTime != null && !endTime.equals("")) {
//            query.setParameter("endTime", LocalDateTime.parse(endTime));
//        }
//        List<LocalDateTime> deliveryTimes = query.getResultList();
//        return deliveryTimes;
//    }

    //获取发货时间列表
    public List<LocalDateTime> getByStartOrEndTime(String startTime, String endTime) {

//        String sql = "SELECT d.deliveryTime FROM CustomerShipment d  where 1=1 ";
        String sql = "SELECT distinct  d.deliveryTime FROM CustomerShipment d  where 1=1 ";

        if (("null").equals(startTime)){
            startTime = "";
        }
        if (("null").equals(endTime)){
            endTime = "";
        }

        if (!"".equals(startTime) && startTime != null) {
            sql = sql + " and d.deliveryTime >=:startTime ";
        }
        if (!"".equals(endTime) && endTime != null) {
            sql = sql + " and  d.deliveryTime <=:endTime ";
        }
        sql = sql + " order by d.deliveryTime ";

        Query query = entityManager.createQuery(sql);

        if (!"".equals(startTime) && startTime != null) {
            query.setParameter("startTime", LocalDateTime.parse(startTime));
        }
        if (!"".equals(endTime) && endTime != null) {
            query.setParameter("endTime", LocalDateTime.parse(endTime));
        }
        List<LocalDateTime> deliveryTimes = query.getResultList();
        return deliveryTimes;
    }


    //获取PPName列表
    public List<String> getProcessPath() {
        String sql = " SELECT pp.NAME FROM  OB_PROCESSPATH AS pp  ";
        Query query = entityManager.createNativeQuery(sql);
        List<String> ppName = query.getResultList();
        return ppName;
    }
}
