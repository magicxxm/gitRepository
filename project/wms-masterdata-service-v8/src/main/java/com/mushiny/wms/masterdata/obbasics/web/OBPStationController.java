package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.IBPStationDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPStationDTO;
import com.mushiny.wms.masterdata.obbasics.service.OBPStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/obp/obp-stations")
public class OBPStationController {

    private final OBPStationService obpStationService;

    @Autowired
    public OBPStationController(OBPStationService obpStationService) {
        this.obpStationService = obpStationService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody OBPStationDTO dto) {
        obpStationService.createMore(dto);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPStationDTO> update(@RequestBody OBPStationDTO dto) {
        return ResponseEntity.ok(obpStationService.update(dto));
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        obpStationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPStationDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(obpStationService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBPStationDTO>> getAll() {
        return ResponseEntity.ok(obpStationService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBPStationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(obpStationService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<OBPStationDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(obpStationService.getBySearchTerm(search, pageable));
    }
}
