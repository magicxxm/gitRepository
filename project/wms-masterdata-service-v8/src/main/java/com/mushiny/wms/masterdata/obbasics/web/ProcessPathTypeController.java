package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.ProcessPathTypeDTO;
import com.mushiny.wms.masterdata.obbasics.service.ProcessPathTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/process-path-types")
public class ProcessPathTypeController {

    private final ProcessPathTypeService processPathTypeService;

    @Autowired
    public ProcessPathTypeController(ProcessPathTypeService processPathTypeService) {
        this.processPathTypeService = processPathTypeService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessPathTypeDTO> create(@RequestBody ProcessPathTypeDTO dto) {
        return ResponseEntity.ok(processPathTypeService.create(dto));
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessPathTypeDTO> update(@RequestBody ProcessPathTypeDTO dto) {
        return ResponseEntity.ok(processPathTypeService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessPathTypeDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(processPathTypeService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProcessPathTypeDTO>> getAll() {
        return ResponseEntity.ok(processPathTypeService.getAll());
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        processPathTypeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProcessPathTypeDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(processPathTypeService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ProcessPathTypeDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(processPathTypeService.getBySearchTerm(search, pageable));
    }
}
