package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingAreaDTO;
import com.mushiny.wms.masterdata.obbasics.service.PickingAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/picking-areas")
public class PickingAreaController {

    private final PickingAreaService pickingAreaService;

    @Autowired
    public PickingAreaController(PickingAreaService pickingAreaService) {
        this.pickingAreaService = pickingAreaService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickingAreaDTO> create(@RequestBody PickingAreaDTO dto) {
        return ResponseEntity.ok(pickingAreaService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        pickingAreaService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickingAreaDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(pickingAreaService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickingAreaDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(pickingAreaService.getByClientId(clientId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickingAreaDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(pickingAreaService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PickingAreaDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(pickingAreaService.getBySearchTerm(search, pageable));
    }
}
