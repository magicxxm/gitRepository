package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchStationDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.SortingStationDTO;
import com.mushiny.wms.masterdata.obbasics.service.RebatchStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/rebatch-stations")
public class RebatchStationController {

    private final RebatchStationService rebatchStationService;

    @Autowired
    public RebatchStationController(RebatchStationService rebatchStationService) {
        this.rebatchStationService = rebatchStationService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody RebatchStationDTO dto) {
        rebatchStationService.createMore(dto);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RebatchStationDTO> update(@RequestBody RebatchStationDTO dto) {
        return ResponseEntity.ok(rebatchStationService.update(dto));
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        rebatchStationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RebatchStationDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(rebatchStationService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RebatchStationDTO>> getAll() {
        return ResponseEntity.ok(rebatchStationService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RebatchStationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(rebatchStationService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<RebatchStationDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(rebatchStationService.getBySearchTerm(search, pageable));
    }
}
