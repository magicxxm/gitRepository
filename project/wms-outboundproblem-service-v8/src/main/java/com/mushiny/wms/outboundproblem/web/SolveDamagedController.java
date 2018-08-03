package com.mushiny.wms.outboundproblem.web;

import com.mushiny.wms.outboundproblem.business.dto.DeleteShipmentDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolvePositionDTO;
import com.mushiny.wms.outboundproblem.crud.dto.PrintShipmentPositionDTO;
import com.mushiny.wms.outboundproblem.service.OBPSolveService;
import com.mushiny.wms.outboundproblem.service.SolveDamagedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/outboundproblem/obp-damaged")
public class    SolveDamagedController {

    private final SolveDamagedService solveDamagedService;
    private final OBPSolveService obpSolveService;

    @Autowired
    public SolveDamagedController(SolveDamagedService solveDamagedService,
                                  OBPSolveService obpSolveService) {
        this.solveDamagedService = solveDamagedService;
        this.obpSolveService = obpSolveService;
    }

    @RequestMapping(value = "/to-normal",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> damagedToNormal(@RequestBody OBPSolvePositionDTO obpSolvePositionDTO) {
        solveDamagedService.damagedToNormal(obpSolvePositionDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/confirm-damage",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmDamaged(@RequestBody OBPSolvePositionDTO obpSolvePositionDTO) {
        solveDamagedService.confirmDamaged(obpSolvePositionDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/generate-hot-pick",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> generateHotPick(@RequestBody OBPSolvePositionDTO obpSolvePositionDTO) {
        solveDamagedService.generateHotPick(obpSolvePositionDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/get-location",
            method = RequestMethod.GET,
            params = {"itemDataNo","sectionId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map>> searchStorageLocationByItemNo(@RequestParam  String itemDataNo,@RequestParam String sectionId) {
        return ResponseEntity.ok(solveDamagedService.searchStorageLocationByItemNo(itemDataNo,sectionId));
    }

    @RequestMapping(value = "/allocation-location",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPSolvePositionDTO> allocationStorageLocation(@RequestBody OBPSolvePositionDTO obpSolvePositionDTO) {
        return ResponseEntity.ok(solveDamagedService.allocationStorageLocation(obpSolvePositionDTO));
    }

    @RequestMapping(value = "/damaged-to-container",
            method = RequestMethod.GET,
            params = {"containerName", "shipmentNo", "itemNo", "useNotAfter","serialNo"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putDamagedToContainer( @RequestParam  String containerName,
                                                      @RequestParam  String shipmentNo,
                                                      @RequestParam  String itemNo,
                                                      @RequestParam String useNotAfter,
                                                      @RequestParam  String serialNo) {
        solveDamagedService.putDamagedToContainer(containerName, shipmentNo, itemNo, useNotAfter,serialNo);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/delete-shipment",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteShipment(@RequestBody OBPSolvePositionDTO obpSolvePositionDTO) {
        solveDamagedService.deleteShipment(obpSolvePositionDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/delete-shipment-scan-goods",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteShipmentScanGoods(@RequestBody DeleteShipmentDTO deleteShipmentDTO) {
        solveDamagedService.deleteShipmentScanGoods(deleteShipmentDTO);
        return ResponseEntity.ok().build();
    }
    //拆单发货确认
    @RequestMapping(value = "/dismantle-shipment",
            method = RequestMethod.GET,
            params = {"shipmentNo","solveKey"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PrintShipmentPositionDTO> dismantleShipment(@RequestParam String shipmentNo, @RequestParam String solveKey) {
        solveDamagedService.dismantleShipment(shipmentNo,solveKey);
        return ResponseEntity.ok(obpSolveService.printOrderByShipmentNo(shipmentNo,solveKey));
    }

    //获取pod信息
    @RequestMapping(value = "/getPod",
            method = RequestMethod.GET,
            params = {"names","obpStationId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map>> getPodFace(@RequestParam List<String> names,@RequestParam String obpStationId){
        return ResponseEntity.ok(solveDamagedService.getPodFace(names,obpStationId));
    }
}
