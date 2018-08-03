package com.mushiny.wms.tot.ppr.query.hql;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Component
public class PprStatisticsQuery implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getStatisticsTotal(String employeeCode,String startDate,String endDate,String warehouseId, String clientId) {
//        if("ALL".equals(clientId)) clientId="";
        String sql = "SELECT   " +
                "  a.CLOCK_TIME ,a.EMPLOYEE_CODE,a.EMPLOYEE_NAME," +
                " IF (a.CLOCK_TYPE = 'CLOCK_IN','上班','下班')  "+
                "FROM TOT_ATTENDANCE a   " +
                "where 1=1  ";
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND  a.EMPLOYEE_CODE =:employeeCode  ";
        }
        if (!StringUtils.isEmpty(startDate)) {
            sql = sql + " and a.CLOCK_TIME >=:startDate ";
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql = sql + " and a.CLOCK_TIME <=:endDate ";
        }
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " and a.WAREHOUSE_ID =:warehouseId ";
        }
//        if (!StringUtils.isEmpty(clientId)) {
//            sql = sql + " and a.CLIENT_ID =:clientId ";
//        }
            sql = sql + " order by a.CLOCK_TIME ASC ";
        Query query = entityManager.createNativeQuery(sql);
        if (!StringUtils.isEmpty(employeeCode)) {
            query.setParameter("employeeCode", employeeCode);
        }
        if (!StringUtils.isEmpty(startDate)) {
            query.setParameter("startDate", startDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            query.setParameter("endDate", endDate);
        }
        if (!StringUtils.isEmpty(warehouseId)) {
            query.setParameter("warehouseId", warehouseId);
        }
//        if (!StringUtils.isEmpty(clientId)) {
//            query.setParameter("clientId", clientId);
//        }
      //  query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Statistics.class));
        List<Object[]> entities = query.getResultList();
        return entities;
    }

    public List<Object[]> getMaxClockTime(String employeeCode,String startDate,String endDate,String warehouseId, String clientId) {
//        if("ALL".equals(clientId)) clientId="";
        String sql = "SELECT   " +
                "  a.CLOCK_TIME ,a.EMPLOYEE_CODE,a.EMPLOYEE_NAME," +
                " IF (a.CLOCK_TYPE = 'CLOCK_IN','上班','下班')  "+
                "FROM TOT_ATTENDANCE a   " +
                "where 1=1  ";
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND  a.EMPLOYEE_CODE =:employeeCode  ";
        }
        if (!StringUtils.isEmpty(startDate)) {
            sql = sql + " and a.CLOCK_TIME >=:startDate ";
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql = sql + " and a.CLOCK_TIME <=:endDate ";
        }
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " and a.WAREHOUSE_ID =:warehouseId ";
        }
//        if (!StringUtils.isEmpty(clientId)) {
//            sql = sql + " and a.CLIENT_ID =:clientId ";
//        }
        sql = sql + " order by a.CLOCK_TIME DESC ";
        Query query = entityManager.createNativeQuery(sql);
        if (!StringUtils.isEmpty(employeeCode)) {
            query.setParameter("employeeCode", employeeCode);
        }
        if (!StringUtils.isEmpty(startDate)) {
            query.setParameter("startDate", startDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            query.setParameter("endDate", endDate);
        }
        if (!StringUtils.isEmpty(warehouseId)) {
            query.setParameter("warehouseId", warehouseId);
        }
//        if (!StringUtils.isEmpty(clientId)) {
//            query.setParameter("clientId", clientId);
//        }
        //  query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Statistics.class));
        List<Object[]> entities = query.getResultList();
        return entities;
    }

    public List<Object[]> getMaxJobAction(String warehouseId, String clientId, String employeeCode,String startTime,String endTime) {
        if("ALL".equals(clientId)) clientId="";
        String sql = " SELECT j.RECORD_TIME,j.JOB_TYPE,j.JOB_ACTION,j.INDIRECT_TYPE,j.CATEGORY_NAME,j.SIZE," +
                "j.JOB_CODE,j.EMPLOYEE_CODE,j.EMPLOYEE_NAME,j.CLIENT_ID,j.WAREHOUSE_ID,j.JOB_NAME " +
                " FROM TOT_JOBRECORD  j  " +
                " where 1=1  ";
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " AND  j.WAREHOUSE_ID =:warehouseId  ";
        }
        if (!StringUtils.isEmpty(clientId)) {
            sql = sql + " AND  j.CLIENT_ID =:clientId  ";
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND   j.EMPLOYEE_CODE =:employeeCode  ";
        }
        if (!StringUtils.isEmpty(startTime)) {
            sql = sql + " AND   j.RECORD_TIME >=:startTime   ";
        }
        if (!StringUtils.isEmpty(endTime)) {
            sql = sql + " AND   j.RECORD_TIME <=:endTime   ";
        }
        sql = sql +" ORDER BY j.RECORD_TIME DESC  ";
        Query query = entityManager.createNativeQuery(sql);
        if (!StringUtils.isEmpty(warehouseId)) {
            query.setParameter("warehouseId", warehouseId);
        }
        if (!StringUtils.isEmpty(clientId)) {
            query.setParameter("clientId", clientId);
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            query.setParameter("employeeCode", employeeCode);
        }

        if (!StringUtils.isEmpty(startTime)) {
            query.setParameter("startTime",startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            query.setParameter("endTime", endTime);
        }
        List<Object[]> clockTime = query.getResultList();
        return clockTime;
    }

    public List<Object[]> getRecords(String warehouseId, String clientId, String employeeCode,String startDate,String endDate) {
        if("ALL".equals(clientId)) clientId="";
        String sql =    "SELECT  j.RECORD_TIME,j.JOB_TYPE,j.JOB_ACTION,j.INDIRECT_TYPE,j.CATEGORY_NAME,j.SIZE," +
                "j.JOB_CODE,j.EMPLOYEE_CODE,j.EMPLOYEE_NAME,j.CLIENT_ID,j.WAREHOUSE_ID,j.JOB_NAME" +
                " FROM TOT_JOBRECORD j  " +
                " where 1=1  ";
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " AND  j.WAREHOUSE_ID =:warehouseId  ";
        }
        if (!StringUtils.isEmpty(clientId)) {
            sql = sql + " AND  j.CLIENT_ID =:clientId  ";
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND  j.EMPLOYEE_CODE =:employeeCode  ";
        }
        if (!StringUtils.isEmpty(startDate)) {
            sql = sql + " AND   j.RECORD_TIME >=:startDate   ";
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql = sql + " AND   j.RECORD_TIME <=:endDate   ";
        }

        sql = sql + " ORDER BY j.RECORD_TIME ";

        Query query = entityManager.createNativeQuery(sql);
        if (!StringUtils.isEmpty(warehouseId)) {
            query.setParameter("warehouseId", warehouseId);
        }
        if (!StringUtils.isEmpty(clientId)) {
            query.setParameter("clientId", clientId);
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            query.setParameter("employeeCode", employeeCode);
        }

        if (!StringUtils.isEmpty(startDate)) {
            query.setParameter("startDate", startDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            query.setParameter("endDate", endDate);
        }

        // query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(User.class));
        List<Object[]> obj = query.getResultList();
        return obj;
    }

    // 查询工作的项目尺寸 多列合并
    public List<Object[]> getRecordByPpr(String warehouseId, String clientId,String startDate,String endDate, String employeeCode, boolean flag) {
        if("ALL".equals(clientId)) clientId="";
        String sql =    "SELECT  r.CATEGORY_NAME,r.SIZE,r.UNIT_TYPE, SUM(r.QUANTITY)," +
                "IF (r.JOB_TYPE = 'DIRECT','直接工作','间接工作'),r.WAREHOUSE_ID,r.CLIENT_ID " +
                "FROM TOT_JOBRECORD r  " +
                "WHERE (" + flag  + " or r.ENTITY_LOCK = 0)";
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " AND  r.WAREHOUSE_ID =:warehouseId  ";
        }
        if (!StringUtils.isEmpty(clientId)) {
            sql = sql + " AND  r.CLIENT_ID =:clientId  ";
        }
        if (!StringUtils.isEmpty(startDate)) {
            sql = sql + " AND   r.RECORD_TIME >=:startDate   ";
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql = sql + " AND   r.RECORD_TIME <=:endDate   ";
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND  r.EMPLOYEE_CODE =:employeeCode  ";
        }

        sql = sql + " GROUP BY r.CATEGORY_NAME, r.SIZE,r.UNIT_TYPE,r.JOB_TYPE,r.WAREHOUSE_ID,r.CLIENT_ID ";

        Query query = entityManager.createNativeQuery(sql);
        if (!StringUtils.isEmpty(warehouseId)) {
            query.setParameter("warehouseId", warehouseId);
        }
        if (!StringUtils.isEmpty(clientId)) {
            query.setParameter("clientId", clientId);
        }
        if (!StringUtils.isEmpty(startDate)) {
            query.setParameter("startDate", startDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            query.setParameter("endDate", endDate);
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            query.setParameter("employeeCode", employeeCode);
        }
        List<Object[]> obj = query.getResultList();
        return obj;
    }

    public List<Object[]> getSpecialByPallet(String warehouseId, String clientId,String startDate,String endDate, String employeeCode) {
        if("ALL".equals(clientId)) clientId="";
        String sql =    "SELECT  r.CATEGORY_NAME,r.UNIT_TYPE,r.TO_STORAGELOCATION,r.WAREHOUSE_ID,r.CLIENT_ID,r.JOB_CODE,r.EMPLOYEE_CODE FROM TOT_JOBRECORD r  " +
                " WHERE r.CATEGORY_NAME = 'Pallet Receive To Stow' ";
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " AND  r.WAREHOUSE_ID =:warehouseId  ";
        }
        if (!StringUtils.isEmpty(clientId)) {
            sql = sql + " AND  r.CLIENT_ID =:clientId  ";
        }
        if (!StringUtils.isEmpty(startDate)) {
            sql = sql + " AND   r.RECORD_TIME >=:startDate   ";
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql = sql + " AND   r.RECORD_TIME <=:endDate   ";
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND  r.EMPLOYEE_CODE =:employeeCode  ";
        }

        sql = sql + " GROUP BY r.CATEGORY_NAME, r.UNIT_TYPE,r.TO_STORAGELOCATION,r.WAREHOUSE_ID,r.CLIENT_ID,r.JOB_CODE,r.EMPLOYEE_CODE ";

        Query query = entityManager.createNativeQuery(sql);
        if (!StringUtils.isEmpty(warehouseId)) {
            query.setParameter("warehouseId", warehouseId);
        }
        if (!StringUtils.isEmpty(clientId)) {
            query.setParameter("clientId", clientId);
        }
        if (!StringUtils.isEmpty(startDate)) {
            query.setParameter("startDate", startDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            query.setParameter("endDate", endDate);
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            query.setParameter("employeeCode", employeeCode);
        }
        List<Object[]> obj = query.getResultList();
        return obj;
    }

    public List<Object[]> getSpecialBySD(String warehouseId, String clientId,String startDate,String endDate, String employeeCode) {
        if("ALL".equals(clientId)) clientId="";
        String sql =    "SELECT  r.CATEGORY_NAME,r.UNIT_TYPE,r.FROM_STORAGELOCATION,r.WAREHOUSE_ID,r.CLIENT_ID,r.JOB_CODE,r.EMPLOYEE_CODE FROM TOT_JOBRECORD r  " +
                " WHERE (r.CATEGORY_NAME = 'Sort' OR r.CATEGORY_NAME = 'Dock') ";
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " AND  r.WAREHOUSE_ID =:warehouseId  ";
        }
        if (!StringUtils.isEmpty(clientId)) {
            sql = sql + " AND  r.CLIENT_ID =:clientId  ";
        }
        if (!StringUtils.isEmpty(startDate)) {
            sql = sql + " AND   r.RECORD_TIME >=:startDate   ";
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql = sql + " AND   r.RECORD_TIME <=:endDate   ";
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND  r.EMPLOYEE_CODE =:employeeCode  ";
        }

        sql = sql + " GROUP BY r.CATEGORY_NAME, r.UNIT_TYPE,r.FROM_STORAGELOCATION,r.WAREHOUSE_ID,r.CLIENT_ID,r.JOB_CODE,r.EMPLOYEE_CODE ";

        Query query = entityManager.createNativeQuery(sql);
        if (!StringUtils.isEmpty(warehouseId)) {
            query.setParameter("warehouseId", warehouseId);
        }
        if (!StringUtils.isEmpty(clientId)) {
            query.setParameter("clientId", clientId);
        }
        if (!StringUtils.isEmpty(startDate)) {
            query.setParameter("startDate", startDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            query.setParameter("endDate", endDate);
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            query.setParameter("employeeCode", employeeCode);
        }
        List<Object[]> obj = query.getResultList();
        return obj;
    }

    // 查询工作的 多列合并
    public List<Object[]> getRecordByPprJob(String warehouseId, String clientId,String category,
                                            String startDate,String endDate, String employeeCode,boolean flag) {
        if("ALL".equals(clientId)) clientId="";
        String sql =    "SELECT  r.JOB_CODE,r.SIZE,COUNT(r.JOB_CODE), SUM(r.QUANTITY)," +
                "IF (r.JOB_TYPE = 'DIRECT','直接工作','间接工作'),r.EMPLOYEE_CODE,r.EMPLOYEE_NAME,r.CATEGORY_NAME,r.JOB_NAME " +
                "FROM TOT_JOBRECORD r  " +
                "WHERE (" + flag + " or r.ENTITY_LOCK != 2) ";
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " AND  r.WAREHOUSE_ID =:warehouseId  ";
        }
        if (!StringUtils.isEmpty(clientId)) {
            sql = sql + " AND  r.CLIENT_ID =:clientId  ";
        }
        if (!StringUtils.isEmpty(category)) {
            sql = sql + " AND  r.CATEGORY_NAME =:category  ";
        }
        if (!StringUtils.isEmpty(startDate)) {
            sql = sql + " AND   r.RECORD_TIME >=:startDate   ";
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql = sql + " AND   r.RECORD_TIME <=:endDate   ";
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND  r.EMPLOYEE_CODE =:employeeCode  ";
        }
        sql = sql + " GROUP BY r.JOB_CODE, r.SIZE,r.JOB_TYPE,r.EMPLOYEE_CODE,r.EMPLOYEE_NAME,r.CATEGORY_NAME,r.JOB_NAME ";

        Query query = entityManager.createNativeQuery(sql);
        if (!StringUtils.isEmpty(warehouseId)) {
            query.setParameter("warehouseId", warehouseId);
        }
        if (!StringUtils.isEmpty(clientId)) {
            query.setParameter("clientId", clientId);
        }
        if (!StringUtils.isEmpty(category)) {
            query.setParameter("category", category);
        }
        if (!StringUtils.isEmpty(startDate)) {
            query.setParameter("startDate", startDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            query.setParameter("endDate", endDate);
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            query.setParameter("employeeCode", employeeCode);
        }
        List<Object[]> obj = query.getResultList();
        return obj;
    }
}
