package com.mushiny.wms.outboundproblem.query.hql;


import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class UnitloadShipmentUpdateHQL {

    @PersistenceContext
    private EntityManager entityManager;

    public void updateUnitloadByShipment(String unitLoadId, String shipmentId) {
        String sql = " update INV_UNITLOAD_SHIPMENT set UNITLOAD_ID = :unitLoadId where SHIPMENT_ID = :shipmentId ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("unitLoadId", unitLoadId);
        query.setParameter("shipmentId", shipmentId);
        query.executeUpdate();
    }

    public void insertUnitloadShipment(String unitLoadId, String shipmentId) {
        String sql = " insert into INV_UNITLOAD_SHIPMENT (UNITLOAD_ID, SHIPMENT_ID) values (:unitLoadId, :shipmentId) ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("unitLoadId", unitLoadId);
        query.setParameter("shipmentId", shipmentId);
        query.executeUpdate();
    }

    public void deleteUnitloadShipment(String shipmentId) {
        String sql = " delete from INV_UNITLOAD_SHIPMENT where  SHIPMENT_ID=:shipmentId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("shipmentId", shipmentId);
        query.executeUpdate();
    }

    public List<Object> getShipmentNo(String unitLoadId) {
        String sql = "select SHIPMENT_ID from INV_UNITLOAD_SHIPMENT where UNITLOAD_ID=:unitLoadId ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("unitLoadId", unitLoadId);
        return query.getResultList();
    }

    public List<Object[]> selectUnitloadShipment(String unitLoadId,String shipmentId) {
        String sql = "select UNITLOAD_ID,SHIPMENT_ID from INV_UNITLOAD_SHIPMENT where UNITLOAD_ID=:unitLoadId and SHIPMENT_ID=:shipmentId ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("unitLoadId", unitLoadId);
        query.setParameter("shipmentId", shipmentId);
        List<Object[]> entities=query.getResultList();
        return entities;
    }
}
