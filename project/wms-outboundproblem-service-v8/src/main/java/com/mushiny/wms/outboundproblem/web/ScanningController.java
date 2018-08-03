package com.mushiny.wms.outboundproblem.web;

import com.mushiny.wms.outboundproblem.crud.common.dto.ScanningOBPStationDTO;
import com.mushiny.wms.outboundproblem.crud.common.dto.StorageLocationDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolveDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPWallDTO;
import com.mushiny.wms.outboundproblem.service.OBPSolveService;
import com.mushiny.wms.outboundproblem.service.OBPVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/outboundproblem/scanning")
public class ScanningController {

    private final OBPVerificationService obpVerificationService;
    private final OBPSolveService obpSolveService;

    @Autowired
    public ScanningController(OBPVerificationService obpVerificationService,
                              OBPSolveService obpSolveService) {
        this.obpVerificationService = obpVerificationService;
        this.obpSolveService = obpSolveService;
    }

    @RequestMapping(value = "/obp-station",
            method = RequestMethod.GET,
            params = {"name"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScanningOBPStationDTO> scanOBPStation(@RequestParam String name) {
        return ResponseEntity.ok(obpVerificationService.scanOBPStation(name));
    }

    @RequestMapping(value = "/obp-wall",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPWallDTO> scanOBPWall(@RequestParam String name) {
        return ResponseEntity.ok(obpVerificationService.scanOBPWall(name));
    }

    @RequestMapping(value = "/shipment",
            method = RequestMethod.GET,
            params = {"obpStationId","obpWallId","shipmentNo","state"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBPSolveDTO>> getProblemsByShipmentNo(@RequestParam String obpStationId,
                                                                     @RequestParam String obpWallId,
                                                                     @RequestParam String shipmentNo,
                                                                     @RequestParam String state) {
        return ResponseEntity.ok(obpSolveService.getProblemsByShipmentNo(obpStationId, obpWallId, shipmentNo, state));
    }

    @RequestMapping(value = "/obp-container",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageLocationDTO> scanProcessContainer(@RequestParam String name) {
        return ResponseEntity.ok(obpSolveService.scanProcessContainer(name));
    }
}
