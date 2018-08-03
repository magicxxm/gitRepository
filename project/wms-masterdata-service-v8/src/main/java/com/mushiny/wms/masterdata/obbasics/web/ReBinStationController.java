package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinStationDTO;
import com.mushiny.wms.masterdata.obbasics.service.ReBinStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/rebin-stations")
public class ReBinStationController {

    private final ReBinStationService reBinStationService;

    @Autowired
    public ReBinStationController(ReBinStationService reBinStationService) {
        this.reBinStationService = reBinStationService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReBinStationDTO> create(@RequestBody ReBinStationDTO dto) {
        return ResponseEntity.ok(reBinStationService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        reBinStationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReBinStationDTO> update(@RequestBody ReBinStationDTO dto) {
        return ResponseEntity.ok(reBinStationService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReBinStationDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(reBinStationService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReBinStationDTO>> getAll() {
        return ResponseEntity.ok(reBinStationService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReBinStationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(reBinStationService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ReBinStationDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(reBinStationService.getBySearchTerm(search, pageable));
    }
}
