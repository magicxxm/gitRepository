package com.mushiny.wms.outboundproblem.query;


import com.mushiny.wms.outboundproblem.query.hql.UnitloadShipmentUpdateHQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnitLoadShipmentQuery {
    private final UnitloadShipmentUpdateHQL unitloadShipmentUpdateHQL;

    @Autowired
    public UnitLoadShipmentQuery(UnitloadShipmentUpdateHQL unitloadShipmentUpdateHQL) {
        this.unitloadShipmentUpdateHQL = unitloadShipmentUpdateHQL;
    }

    public void updateUnitloadByShipment(String unitLoadId, String shipmentId) {
        unitloadShipmentUpdateHQL.updateUnitloadByShipment(unitLoadId, shipmentId);
    }

    public void insertUnitloadShipment(String unitLoadId, String shipmentId) {
        unitloadShipmentUpdateHQL.insertUnitloadShipment(unitLoadId, shipmentId);
    }

    public void deleteUnitloadShipment(String shipmentId) {
        unitloadShipmentUpdateHQL.deleteUnitloadShipment(shipmentId);
    }

    public List<Object[]> selectUnitloadShipment(String unitLoadId,String shipmentId) {
       return unitloadShipmentUpdateHQL.selectUnitloadShipment(unitLoadId,shipmentId);
    }

    public List<Object> getShipmentNo(String unitLoadId) {
       return unitloadShipmentUpdateHQL.getShipmentNo(unitLoadId);
    }
}
