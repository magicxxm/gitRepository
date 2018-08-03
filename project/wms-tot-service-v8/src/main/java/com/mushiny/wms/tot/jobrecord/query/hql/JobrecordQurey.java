package com.mushiny.wms.tot.jobrecord.query.hql;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/25.
 */
@Component
public class JobrecordQurey implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getDetailByPage(int name, int size, String employeeCode, String warehouseId, String startDate, String endDate) {
        String sql = "SELECT j.RECORD_TIME,j.JOB_ACTION,j.SKU_NO,j.ITEM_NO,j.UNIT_TYPE,j.SIZE," +
                "j.QUANTITY,j.FROM_STORAGELOCATION,j.TO_STORAGELOCATION,j.CLIENT_ID,j.TOOL," +
                "j.SHIPMENTNO,j.ENTITY_LOCK FROM TOT_JOBRECORD j   " +
                "where j.JOB_TYPE='DIRECT' ";
        if (!StringUtils.isEmpty(startDate)) {
            sql = sql + " and j.RECORD_TIME >=:startDate ";
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql = sql + " and j.RECORD_TIME <=:endDate ";
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND j.EMPLOYEE_CODE =:employeeCode  ";
        }
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " and j.WAREHOUSE_ID =:warehouseId ";
        }
        sql = sql + " order by j.RECORD_TIME ASC ";
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
        query.setFirstResult(name*size);
        query.setMaxResults(size);
        List<Object[]> entities = query.getResultList();
        return entities;
    }

    public Object getCountByPage(String employeeCode, String warehouseId, String startDate, String endDate) {
        String sql = "SELECT COUNT(j.ID) FROM TOT_JOBRECORD j   " +
                "where j.JOB_TYPE='DIRECT' ";
        if (!StringUtils.isEmpty(startDate)) {
            sql = sql + " and j.RECORD_TIME >=:startDate ";
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql = sql + " and j.RECORD_TIME <=:endDate ";
        }
        if (!StringUtils.isEmpty(employeeCode)) {
            sql = sql + " AND j.EMPLOYEE_CODE =:employeeCode  ";
        }
        if (!StringUtils.isEmpty(warehouseId)) {
            sql = sql + " and j.WAREHOUSE_ID =:warehouseId ";
        }
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
        return query.getSingleResult();
    }
}
