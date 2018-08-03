package com.mushiny.wms.outboundproblem.web;

import com.mushiny.wms.outboundproblem.business.dto.DeleteShipmentDTO;
import com.mushiny.wms.outboundproblem.business.dto.ForceDeleteShipmentDTO;
import com.mushiny.wms.outboundproblem.business.enums.SolveResoult;
import com.mushiny.wms.outboundproblem.service.DeleteShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/outboundproblem/obp-delete-shipment")
public class DeleteShipmentController {

    private final DeleteShipmentService deleteShipmentService;

    @Autowired
    public DeleteShipmentController(DeleteShipmentService deleteShipmentService) {
        this.deleteShipmentService = deleteShipmentService;
    }

    @RequestMapping(value = "/query-force-delete",
            method = RequestMethod.GET,
            params = {"startDate","endDate","shipmentNo","state","obpStationId","obpWallId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ForceDeleteShipmentDTO>> queryForceDeleteShipment(@RequestParam String startDate,
                                                                                 @RequestParam String endDate,
                                                                                 @RequestParam String shipmentNo,
                                                                                 @RequestParam String state,
                                                                                 @RequestParam String obpStationId,
                                                                                 @RequestParam String obpWallId) {
        return ResponseEntity.ok(deleteShipmentService.queryForceDeleteShipment(startDate, endDate, shipmentNo, state, obpStationId, obpWallId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            params = {"shipmentNo","deleteReason"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addDeleteReason(@RequestParam String shipmentNo,
                                                @RequestParam String deleteReason) {
        deleteShipmentService.addDeleteReason(shipmentNo, deleteReason);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/force-delete",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putForceDeleteGoodsToContainer(@RequestBody DeleteShipmentDTO deleteShipmentDTO) {
        deleteShipmentService.putForceDeleteGoodsToContainer(deleteShipmentDTO);
        return ResponseEntity.ok().build();
    }
}
