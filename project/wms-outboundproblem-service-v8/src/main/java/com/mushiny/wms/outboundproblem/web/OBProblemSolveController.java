package com.mushiny.wms.outboundproblem.web;

import com.mushiny.wms.outboundproblem.crud.dto.*;
import com.mushiny.wms.outboundproblem.service.OBProblemSolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/outboundproblem/solve")
public class OBProblemSolveController {

    private final OBProblemSolveService obProblemSolveService;

    @Autowired
    public OBProblemSolveController(OBProblemSolveService obProblemSolveService ) {
        this.obProblemSolveService = obProblemSolveService;
    }



    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody OBPCheckStateDTO dto) {
        obProblemSolveService.create(dto);
        return ResponseEntity.ok().build();
    }











}
