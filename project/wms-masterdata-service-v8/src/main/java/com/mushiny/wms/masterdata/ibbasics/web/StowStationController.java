package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowStationDTO;
import com.mushiny.wms.masterdata.ibbasics.service.StowStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/inbound/stow-stations")
public class StowStationController {

    private final StowStationService stowStationService;

    @Autowired
    public StowStationController(StowStationService stowStationService) {
        this.stowStationService = stowStationService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody StowStationDTO dto) {
        stowStationService.createMore(dto);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StowStationDTO> update(@RequestBody StowStationDTO dto) {
        return ResponseEntity.ok(stowStationService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        stowStationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StowStationDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(stowStationService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StowStationDTO>> getAll() {
        return ResponseEntity.ok(stowStationService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StowStationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(stowStationService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<StowStationDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(stowStationService.getBySearchTerm(search, pageable));
    }
}
