package com.mushiny.wms.outboundproblem.web;

import com.mushiny.wms.outboundproblem.business.dto.SolveShipmentPositionDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolveDTO;
import com.mushiny.wms.outboundproblem.service.NormalToProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/outboundproblem/normal-to-problem")
public class NormalToProblemController {
    private final NormalToProblemService normalToProblemService;
    @Autowired
    public NormalToProblemController(NormalToProblemService normalToProblemService) {
        this.normalToProblemService = normalToProblemService;
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"obpStationId","obpWallId","shipmentNo"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<SolveShipmentPositionDTO>> normalToController(@RequestParam String obpStationId ,
                                                                @RequestParam String obpWallId,
                                                                @RequestParam String shipmentNo){
      return ResponseEntity.ok( normalToProblemService.normalToProblem(obpStationId,obpWallId,shipmentNo));
    }
}
