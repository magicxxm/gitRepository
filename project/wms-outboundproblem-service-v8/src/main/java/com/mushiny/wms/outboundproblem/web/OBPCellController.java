package com.mushiny.wms.outboundproblem.web;

import com.mushiny.wms.outboundproblem.crud.dto.CellStorageLocationDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPCellDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolveDTO;
import com.mushiny.wms.outboundproblem.crud.dto.ReliveCellDTO;
import com.mushiny.wms.outboundproblem.service.OBPCellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outboundproblem")
public class OBPCellController {

    private final OBPCellService obpCellService;

    @Autowired
    public OBPCellController(OBPCellService obpCellService) {
        this.obpCellService = obpCellService;
    }


    @RequestMapping(value = "/obp-cell",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPCellDTO> getCellByWallName(@RequestParam String wallId) {
        return ResponseEntity.ok(obpCellService.getCellByWallId(wallId));
    }

    @RequestMapping(value = "/obp-bindCell",
            method = RequestMethod.GET,
            params = {"shipmentNo","cellName"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> bindCell(@RequestParam String shipmentNo, @RequestParam String cellName) {
       obpCellService.bindCell(shipmentNo,cellName);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/obp-unbindCell",
            method = RequestMethod.GET,
            params = {"shipmentNo","obpStationId","cellName","solveKey"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> unbindCell(@RequestParam String shipmentNo,@RequestParam String obpStationId,
                                           @RequestParam String cellName, @RequestParam String solveKey) {
        obpCellService.unbindCell(shipmentNo, obpStationId, cellName, solveKey);
        return ResponseEntity.ok().build();
    }

    //美的清除问题处理格和释放问题处理格接口
    @RequestMapping(value = "/obp-relieveCell",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> relieveCell(@RequestBody ReliveCellDTO relieverDTO) {
        obpCellService.relieveCell(relieverDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/cell-shipment",
            method = RequestMethod.GET,
            params = {"stationId","wallId","cellName"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBPSolveDTO>> getSolveShipmentPositionByShipmentNo(@RequestParam String stationId,
                                                                                  @RequestParam String wallId,
                                                                                  @RequestParam String cellName) {
        return ResponseEntity.ok(obpCellService.getProblemsByCell(stationId,wallId,cellName));
    }

    //查询所有未送至包装的问题格
    @RequestMapping(value = "/obp-wall-storagelocation",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CellStorageLocationDTO>> getStoragelocationByWallName(@RequestParam String wallId,@RequestParam String podNo,@RequestParam String location) {
        return ResponseEntity.ok(obpCellService.getStoragelocationByWallName(wallId,podNo,location));
    }
}
