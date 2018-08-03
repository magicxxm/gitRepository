package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowThresholdDTO;
import com.mushiny.wms.masterdata.ibbasics.service.StowThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/inbound/stow-thresholds")
public class StowThresholdController {

    private final StowThresholdService stowThresholdService;

    @Autowired
    public StowThresholdController(StowThresholdService stowThresholdService) {
        this.stowThresholdService = stowThresholdService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StowThresholdDTO> create(@RequestBody StowThresholdDTO dto) {
        return ResponseEntity.ok(stowThresholdService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        stowThresholdService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StowThresholdDTO> update(@RequestBody StowThresholdDTO dto) {
        return ResponseEntity.ok(stowThresholdService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StowThresholdDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(stowThresholdService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StowThresholdDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(stowThresholdService.getByClientId(clientId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StowThresholdDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(stowThresholdService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<StowThresholdDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(stowThresholdService.getBySearchTerm(search, pageable));
    }
}
