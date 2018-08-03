package com.mushiny.wms.masterdata.mdbasics.web;


import com.mushiny.wms.masterdata.mdbasics.crud.dto.NodeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationDTO;
import com.mushiny.wms.masterdata.mdbasics.service.WorkStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/workstations")
public class WorkStationController {

    private final WorkStationService workStationService;

    @Autowired
    public WorkStationController(WorkStationService workStationService) {
        this.workStationService = workStationService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkStationDTO> create(@RequestBody WorkStationDTO dto) {
        return ResponseEntity.ok(workStationService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        workStationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/exit/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> exitWorkStation(@PathVariable String id) {
        workStationService.exitWorkStation(id);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkStationDTO> update(@RequestBody WorkStationDTO dto) {
        return ResponseEntity.ok(workStationService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkStationDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(workStationService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WorkStationDTO>> getAll() {
        return ResponseEntity.ok(workStationService.getAll());
    }

    @RequestMapping(value = "/sectionId",
            method = RequestMethod.GET,
            params = {"sectionId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NodeDTO>> getBySectionId(@RequestParam String sectionId) {
        return ResponseEntity.ok(workStationService.getBySectionId(sectionId));
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WorkStationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(workStationService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<WorkStationDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(workStationService.getBySearchTerm(search, pageable));
    }
}
