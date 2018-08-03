package com.mushiny.wms.masterdata.mdbasics.web;


import com.mushiny.wms.masterdata.mdbasics.crud.dto.TurnAreaDTO;
import com.mushiny.wms.masterdata.mdbasics.service.TurnAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/turn-areas")
public class TurnAreaController {

    private final TurnAreaService trurnAreaService;

    @Autowired
    public TurnAreaController(TurnAreaService trurnAreaService) {
        this.trurnAreaService = trurnAreaService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TurnAreaDTO> create(@RequestBody TurnAreaDTO dto) {
        return ResponseEntity.ok(trurnAreaService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        trurnAreaService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TurnAreaDTO> update(@RequestBody TurnAreaDTO dto) {
        return ResponseEntity.ok(trurnAreaService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TurnAreaDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(trurnAreaService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TurnAreaDTO>> getAll() {
        return ResponseEntity.ok(trurnAreaService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TurnAreaDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(trurnAreaService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<TurnAreaDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(trurnAreaService.getBySearchTerm(search, pageable));
    }
}
