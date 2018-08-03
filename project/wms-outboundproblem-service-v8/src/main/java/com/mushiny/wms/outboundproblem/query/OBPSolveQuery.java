package com.mushiny.wms.outboundproblem.query;


import com.mushiny.wms.outboundproblem.domain.OBPSolve;
import com.mushiny.wms.outboundproblem.domain.common.ItemDataSerialNumber;
import com.mushiny.wms.outboundproblem.query.hql.OBPSolveQueryHQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OBPSolveQuery {
    private final OBPSolveQueryHQL obpSolveQueryHQL;

    @Autowired
    public OBPSolveQuery(OBPSolveQueryHQL obpSolveQueryHQL) {
        this.obpSolveQueryHQL = obpSolveQueryHQL;
    }

    public List<OBPSolve> queryProblem(String warehouseId, String obpStationId, String obpWallId, String shipmentNo, String state) {
        return obpSolveQueryHQL.queryProblem(warehouseId, obpStationId, obpWallId, shipmentNo, state);
    }

    public List<OBPSolve> queryProblemByShipment(String warehouseId, String obpStationId, String obpWallId, String shipmentNo, String state) {
        return obpSolveQueryHQL.queryProblemByShipment(warehouseId, obpStationId, obpWallId, shipmentNo, state);
    }

    public List<Object[]> queryShipment(String shipmentNo) {
        return obpSolveQueryHQL.queryShipment(shipmentNo);
    }

    public ItemDataSerialNumber checkSerialNo(String serialNo) {
        return obpSolveQueryHQL.checkSerialNo(serialNo);
    }

    public List<String> getCellByWallId(String warehouseId,String obpWallId){
        return obpSolveQueryHQL.getCellByWallId(warehouseId,obpWallId);
    }
    public List<Object[]> getStorageLocation(String warehouseId,String obpWallId,String cellId){
        return obpSolveQueryHQL.getStorageLocationBySolveKey(warehouseId,obpWallId,cellId);
    }
}
