package com.mushiny.wms.outboundproblem.web;

import com.mushiny.wms.outboundproblem.crud.common.dto.StorageLocationDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolveCheckDTO;
import com.mushiny.wms.outboundproblem.service.OBPSolveCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outboundproblem/obp-solve-check")
public class OBPSolveCheckController {

    private final OBPSolveCheckService obpSolveCheckService;

    @Autowired
    public OBPSolveCheckController(OBPSolveCheckService obpSolveCheckService) {
        this.obpSolveCheckService = obpSolveCheckService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody OBPSolveCheckDTO dto) {
        obpSolveCheckService.create(dto);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        obpSolveCheckService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPSolveCheckDTO> update(@RequestBody OBPSolveCheckDTO dto) {
        return ResponseEntity.ok(obpSolveCheckService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPSolveCheckDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(obpSolveCheckService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBPSolveCheckDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(obpSolveCheckService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<OBPSolveCheckDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(obpSolveCheckService.getBySearchTerm(search, pageable));
    }

    @RequestMapping(value = "/get-storageLocation-id",
            method = RequestMethod.GET,
            params = {"storageLocationName"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageLocationDTO> getStorageLocationIdByName(@RequestParam String storageLocationName) {
        return ResponseEntity.ok(obpSolveCheckService.getStorageLocationIdByName(storageLocationName));
    }



}
