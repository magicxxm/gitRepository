package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowStationTypeDTO;
import com.mushiny.wms.masterdata.ibbasics.service.StowStationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/inbound/stow-station-types")
public class StowStationTypeController {

    private final StowStationTypeService stowStationTypeService;

    @Autowired
    public StowStationTypeController(StowStationTypeService stowStationTypeService) {
        this.stowStationTypeService = stowStationTypeService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StowStationTypeDTO> create(@RequestBody StowStationTypeDTO dto) {
        return ResponseEntity.ok(stowStationTypeService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        stowStationTypeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StowStationTypeDTO> update(@RequestBody StowStationTypeDTO dto) {
        return ResponseEntity.ok(stowStationTypeService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StowStationTypeDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(stowStationTypeService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StowStationTypeDTO>> getAll() {
        return ResponseEntity.ok(stowStationTypeService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StowStationTypeDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(stowStationTypeService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<StowStationTypeDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(stowStationTypeService.getBySearchTerm(search, pageable));
    }
}
