package com.mushiny.wms.outboundproblem.web;

import com.mushiny.wms.outboundproblem.business.dto.SolveShipmentPositionDTO;
import com.mushiny.wms.outboundproblem.business.enums.SolveResoult;
import com.mushiny.wms.outboundproblem.crud.dto.*;
import com.mushiny.wms.outboundproblem.service.OBPCellService;
import com.mushiny.wms.outboundproblem.service.OBPSolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
    @RequestMapping("/outboundproblem/obp-solve")
public class OBPSolveController {

    private final OBPSolveService obpSolveService;
    private final OBPCellService obpCellService;

    @Autowired
    public OBPSolveController(OBPSolveService obpSolveService,
                              OBPCellService obpCellService) {
        this.obpSolveService = obpSolveService;
        this.obpCellService = obpCellService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPSolveDTO> create(@RequestBody OBPSolveDTO dto) {
        return ResponseEntity.ok(obpSolveService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        obpSolveService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPSolveDTO> update(@RequestBody OBPSolveDTO dto) {
        return ResponseEntity.ok(obpSolveService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPSolveDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(obpSolveService.retrieve(id));
    }

    @RequestMapping(value = "/universalSearch",
            method = RequestMethod.GET,
            params = {"state", "seek"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBPCheckStateDTO>> getBySeek(
            @RequestParam String state,
            @RequestParam(required = false) String userName,
            @RequestParam String seek,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(obpSolveService.getSolveBySeek(state, userName, seek, startDate, endDate));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBPSolveDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(obpSolveService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<OBPSolveDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(obpSolveService.getBySearchTerm(search, pageable));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBPSolveDTO>> getAll() {
        return ResponseEntity.ok(obpSolveService.getAll());
    }

    @RequestMapping(value = "/shipment-position",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SolveShipmentPositionDTO>> getSolveShipmentPositionByShipmentNo(@RequestParam String shipmentNo) {
        return ResponseEntity.ok(obpSolveService.getSolveShipmentPositionByShipmentNo(shipmentNo));
    }

    @RequestMapping(value = "/scan-goods",
            method = RequestMethod.GET,
            params = {"cellName", "shipmentNo", "itemNo"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolveShipmentPositionDTO> scanItemByShipmentAndItem(@RequestParam String cellName,
                                                                 @RequestParam String shipmentNo,
                                                                 @RequestParam String itemNo) {
        return ResponseEntity.ok(obpSolveService.scanItemByShipmentAndItem(cellName, shipmentNo, itemNo));
    }

    @RequestMapping(value = "/scan-sn",
            method = RequestMethod.GET,
            params = {"cellName", "shipmentNo", "itemNo", "serialNo"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> scanSerialNo(@RequestParam String cellName,
                                             @RequestParam String shipmentNo,
                                             @RequestParam String itemNo,
                                             @RequestParam String serialNo) {

        obpSolveService.scanSerialNo(cellName, shipmentNo, itemNo, serialNo);
        return ResponseEntity.ok().build();
    }
    //生成hot pick扫描拣货车车牌
    @RequestMapping(value = "/get-cell-container",
            method = RequestMethod.GET,
            params = {"containerName", "obpWallId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPCellDTO> getCellByContainer(@RequestParam String containerName,
                                                         @RequestParam String obpWallId) {

        return ResponseEntity.ok(obpSolveService.getCellByContainer(containerName, obpWallId));
    }
    //生成hot pick扫描商品
    @RequestMapping(value = "/get-cell-itemNo",
            method = RequestMethod.GET,
            params = {"containerName", "obpWallId", "itemNo"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPCellDTO> getCellByContainerAndItemNo(@RequestParam String containerName,
                                                         @RequestParam String obpWallId,
                                                         @RequestParam String itemNo) {

        return ResponseEntity.ok(obpSolveService.getCellByContainerAndItemNo(containerName, obpWallId, itemNo));
    }

    //生成hot pick扫描商品序列号
    @RequestMapping(value = "/scan-hotPick-goods-sn",
            method = RequestMethod.GET,
            params = {"containerName", "obpWallId", "itemNo","serialNo"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPCellDTO> scanHotPickSn(@RequestParam String containerName,
                                                     @RequestParam String obpWallId,
                                                     @RequestParam String itemNo,
                                                     @RequestParam String serialNo) {

        return ResponseEntity.ok(obpSolveService.scanHotPickSn(containerName, obpWallId, itemNo,serialNo));
    }

   //点击货货位放商品
   @RequestMapping(value = "/assign-location",
           method = RequestMethod.GET,
           params = {"name", "solveId","obpLocationId","obpWallId"},
           produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Void> scanGoodsStorageLocation(@RequestParam String name,
                                                         @RequestParam String solveId,
                                                         @RequestParam String obpLocationId,
                                                         @RequestParam String obpWallId) {
          obpSolveService.assignLocation(name,solveId, obpLocationId,obpWallId);
       return ResponseEntity.ok().build();
   }
    @RequestMapping(value = "/scan-picking-goods",
            method = RequestMethod.GET,
            params = {"location", "itemNo"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolveShipmentPositionDTO> scanPickingGoods(@RequestParam String location,
                                                         @RequestParam String itemNo) {
        return ResponseEntity.ok(obpSolveService.scanPickingGoods(location,itemNo));
    }

    @RequestMapping(value = "/scan-goods-sn",
            method = RequestMethod.GET,
            params = {"serialNo","location", "itemNo"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> scanGoodsSn(@RequestParam String serialNo,@RequestParam String location, @RequestParam String itemNo) {
        obpSolveService.scanGoodsSn(serialNo,location,itemNo);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/getPodPosition",
            method = RequestMethod.GET,
            params = {"cellNames","workStationId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map>> getLocations(@RequestParam List<String> cellNames,@RequestParam String workStationId) {
        return ResponseEntity.ok(obpSolveService.getLocations(cellNames,workStationId));
    }

    @RequestMapping(value = "/get-cell-problem",
            method = RequestMethod.GET,
            params = {"obpWallId", "cellName"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBPSolveDTO>> bindCell(@RequestParam String obpWallId, @RequestParam String cellName) {
        return ResponseEntity.ok(obpSolveService.getOBProblemByCellName(obpWallId, cellName));
    }

    @RequestMapping(value = "/sign-out-station",
            method = RequestMethod.GET,
            params = {"obpStationId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signOutOBPStation(@RequestParam String obpStationId) {
        obpSolveService.signOutOBPStation(obpStationId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/verify-sign-out-station",
            method = RequestMethod.GET,
            params = {"obpStationId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> verifySignOutOBPStation(@RequestParam String obpStationId) {
        return ResponseEntity.ok(obpSolveService.verifySignOutOBPStation(obpStationId));
    }

    @RequestMapping(value = "/goto-packing",
            method = RequestMethod.GET,
            params = {"shipmentNo","obpStationId","cellName"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gotoPacking(@RequestParam String shipmentNo,
                                            @RequestParam String obpStationId,
                                            @RequestParam String cellName) {
        obpCellService.unbindCell(shipmentNo, obpStationId, cellName, SolveResoult.RELEASE_CELL.toString());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/batch-to-packing",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> batchToPacking(@RequestParam String datas){
        obpSolveService.batchToPacking(datas);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/print-shipment-position",
            method=RequestMethod.GET,
            params={"shipmentNo","solveKey"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PrintShipmentPositionDTO> printOrder(@RequestParam String shipmentNo,
                                                                @RequestParam String solveKey){
        return ResponseEntity.ok(obpSolveService.printOrderByShipmentNo(shipmentNo,solveKey));
    }
}
