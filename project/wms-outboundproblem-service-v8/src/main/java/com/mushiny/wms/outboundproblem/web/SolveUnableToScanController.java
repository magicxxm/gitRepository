package com.mushiny.wms.outboundproblem.web;

import com.mushiny.wms.outboundproblem.crud.dto.OBPSolvePositionDTO;
import com.mushiny.wms.outboundproblem.domain.common.ItemDataGlobal;
import com.mushiny.wms.outboundproblem.service.SolveUnableToScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outboundproblem/unable-to-scan")
public class SolveUnableToScanController {

    private final SolveUnableToScanService solveUnableToScanService;

    @Autowired
    public SolveUnableToScanController(SolveUnableToScanService solveUnableToScanService) {
        this.solveUnableToScanService = solveUnableToScanService;
    }

    @RequestMapping(value = "/print-sku",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataGlobal> printSKUNo(@RequestBody OBPSolvePositionDTO obpSolvePositionDTO) {

        return ResponseEntity.ok(solveUnableToScanService.printSKUNo(obpSolvePositionDTO));
    }

    @RequestMapping(value = "/to-investigated",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> pendingInvestigation(@RequestBody OBPSolvePositionDTO obpSolvePositionDTO) {
        solveUnableToScanService.pendingInvestigation(obpSolvePositionDTO);
        return ResponseEntity.ok().build();
    }

}
