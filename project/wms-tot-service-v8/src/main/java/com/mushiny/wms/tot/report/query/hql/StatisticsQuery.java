package com.mushiny.wms.tot.report.query.hql;
import com.mushiny.wms.tot.report.query.dto.JobTypeDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Component
public class StatisticsQuery implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getStatisticsTotal(String employeeCode,String startDate,String endDate,String warehouseId) {
        String sql = "SELECT   " +
                "  a.CLOCK_TIME ,a.EMPLOYEE_CODE,a.EMPLOYEE_NAME," +
                " IF (a.CLOCK_TYPE = 'CLOCK_IN','上班','下班') ,a.CLIENT_ID,a.WAREHOUSE_ID "+
                "FROM TOT_ATTENDANCE a   " +
                "where 1=1  ";
        if (!StringUtils.isEmpty(startDate)) {
            sql = sql + " and a.CLOCK_TIME >=:startDate ";
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql = sql + " and a.CLOCK_TIME <=:endDate ";
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND  a.EMPLOYEE_CODE =:employeeCode  ";
        }
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " and a.WAREHOUSE_ID =:warehouseId ";
        }
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
        List<Object[]> entities = query.getResultList();
        return entities;
    }

    public List<Object[]> getMaxClockTime(String employeeCode,String startDate,String endDate,String warehouseId,String dayDate ) {
        String sql = "SELECT   " +
                "  a.CLOCK_TIME ,a.EMPLOYEE_CODE,a.EMPLOYEE_NAME," +
                " IF (a.CLOCK_TYPE = 'CLOCK_IN','上班','下班') ,a.CLIENT_ID,a.WAREHOUSE_ID  "+
                "FROM TOT_ATTENDANCE a   " +
                "where 1=1  ";
        if (!StringUtils.isEmpty(startDate)) {
            sql = sql + " and a.CLOCK_TIME >=:startDate ";
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql = sql + " and a.CLOCK_TIME <=:endDate ";
        }
        if (!StringUtils.isEmpty(dayDate)) {
            sql = sql + " and a.CLOCK_TIME  >= '"+dayDate+" 00:00:00'"+" and a.CLOCK_TIME  <= '"+dayDate+" 23:59:59'";
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND  a.EMPLOYEE_CODE =:employeeCode  ";
        }
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " and a.WAREHOUSE_ID =:warehouseId ";
        }
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

    public List<Object[]> getMaxJobAction(String warehouseId, String employeeCode,String startTime,String endTime,String dayDate) {
        //j.RECORD_TIME,j.JOB_TYPE,j.JOB_ACTION,j.INDIRECT_TYPE,j.JOB_NAME,j.EMPLOYEE_CODE,j.CLIENT_ID,j.WAREHOUSE_ID
        String sql = " SELECT j.JOB_TYPE,j.RECORD_TIME,j.JOB_ACTION,j.INDIRECT_TYPE,j.JOB_NAME,j.EMPLOYEE_CODE,j.CLIENT_ID,j.WAREHOUSE_ID   " +
                " FROM TOT_JOBRECORD  j  " +
                " where 1=1  ";
        if (!StringUtils.isEmpty(startTime)) {
            sql = sql + " AND   j.RECORD_TIME >=:startTime   ";
        }
        if (!StringUtils.isEmpty(endTime)) {
            sql = sql + " AND   j.RECORD_TIME <=:endTime   ";
        }
        if (!StringUtils.isEmpty(dayDate)) {
            sql = sql + " AND   j.RECORD_TIME  >= '"+dayDate+" 00:00:00'"+" and j.RECORD_TIME  <= '"+dayDate+" 23:59:59'";
        }
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " AND  j.WAREHOUSE_ID =:warehouseId  ";
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND   j.EMPLOYEE_CODE =:employeeCode  ";
        }

        sql = sql +" ORDER BY j.RECORD_TIME DESC  ";
        Query query = entityManager.createNativeQuery(sql);
        if (!StringUtils.isEmpty(warehouseId)) {
            query.setParameter("warehouseId", warehouseId);
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

    public List<Object[]> getRecords(String warehouseId, String employeeCode,String startDate,String endDate,String dayDate) {
        String sql =    "SELECT  j.RECORD_TIME,j.JOB_TYPE,j.JOB_ACTION,j.INDIRECT_TYPE,j.JOB_NAME,j.EMPLOYEE_CODE,j.CLIENT_ID,j.WAREHOUSE_ID   " +
                "FROM TOT_JOBRECORD j  " +
                "where 1=1  ";
        if (!StringUtils.isEmpty(startDate)) {
            sql = sql + " AND   j.RECORD_TIME >=:startDate   ";
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql = sql + " AND   j.RECORD_TIME <=:endDate   ";
        }
        if (!StringUtils.isEmpty(dayDate)) {
            sql = sql + " AND   j.RECORD_TIME  >= '"+dayDate+" 00:00:00'"+" and j.RECORD_TIME  <= '"+dayDate+" 23:59:59'";
        }
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " AND  j.WAREHOUSE_ID =:warehouseId  ";
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND  j.EMPLOYEE_CODE =:employeeCode  ";
        }
      /*  if (!StringUtils.isEmpty(startDate)) {
            sql = sql + " AND   j.RECORD_TIME >=:str_to_date(startDate,'%Y-%m-%d %H:%i:%s')   ";
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql = sql + " AND   j.RECORD_TIME <=:str_to_date(endDate,'%Y-%m-%d %H:%i:%s')   ";
        }*/

        sql = sql + " ORDER BY j.RECORD_TIME ";

        Query query = entityManager.createNativeQuery(sql);
        if (!StringUtils.isEmpty(warehouseId)) {
            query.setParameter("warehouseId", warehouseId);
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

    public List<Object[]> getDJobType(String typeTable){

        String sql = "SELECT NAME,DESCRIPTION FROM "+typeTable;

        Query query = entityManager.createNativeQuery(sql);
//        query.setParameter("typeTable", typeTable);
        List<Object[]> obj = query.getResultList();
        return obj;
    }
}
