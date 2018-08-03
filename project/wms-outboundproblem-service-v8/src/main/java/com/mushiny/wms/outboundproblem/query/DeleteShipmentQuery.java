package com.mushiny.wms.outboundproblem.query;


import com.mushiny.wms.outboundproblem.business.dto.ForceDeleteShipmentDTO;
import com.mushiny.wms.outboundproblem.query.hql.DeleteShipmentQueryHQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DeleteShipmentQuery {
    private final DeleteShipmentQueryHQL deleteShipmentQueryHQL;

    @Autowired
    public DeleteShipmentQuery(DeleteShipmentQueryHQL deleteShipmentQueryHQL) {
        this.deleteShipmentQueryHQL = deleteShipmentQueryHQL;
    }

    public List<Object[]> queryForceDeleteShipment(String startDate, String endDate, String shipmentNo, String state,String obpStationId) {
        return deleteShipmentQueryHQL.queryForceDeleteShipment(startDate, endDate, shipmentNo, state,obpStationId);
    }

}
