package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.ProcessPathDTO;
import com.mushiny.wms.masterdata.obbasics.service.ProcessPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/process-paths")
public class ProcessPathController {

    private final ProcessPathService processPathService;

    @Autowired
    public ProcessPathController(ProcessPathService processPathService) {
        this.processPathService = processPathService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessPathDTO> create(@RequestBody ProcessPathDTO dto) {
        return ResponseEntity.ok(processPathService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        processPathService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessPathDTO> update(@RequestBody ProcessPathDTO dto) {
        return ResponseEntity.ok(processPathService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessPathDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(processPathService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProcessPathDTO>> getAll() {
        return ResponseEntity.ok(processPathService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProcessPathDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(processPathService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ProcessPathDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(processPathService.getBySearchTerm(search, pageable));
    }
}
