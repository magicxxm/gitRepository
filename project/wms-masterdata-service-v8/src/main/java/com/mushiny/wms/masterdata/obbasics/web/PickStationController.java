package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.PickStationDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchStationDTO;
import com.mushiny.wms.masterdata.obbasics.service.PickStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/pick-stations")
public class PickStationController {

    private final PickStationService pickStationService;

    @Autowired
    public PickStationController(PickStationService pickStationService) {
        this.pickStationService = pickStationService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody PickStationDTO dto) {
        pickStationService.createMore(dto);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickStationDTO> update(@RequestBody PickStationDTO dto) {
        return ResponseEntity.ok(pickStationService.update(dto));
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        pickStationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickStationDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(pickStationService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickStationDTO>> getAll() {
        return ResponseEntity.ok(pickStationService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickStationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(pickStationService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PickStationDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(pickStationService.getBySearchTerm(search, pageable));
    }
}
