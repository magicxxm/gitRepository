package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackWallDTO;
import com.mushiny.wms.masterdata.obbasics.service.PickPackWallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/pick-pack-walls")
public class PickPackWallController {

    private final PickPackWallService pickPackWallService;

    @Autowired
    public PickPackWallController(PickPackWallService pickPackWallService) {
        this.pickPackWallService = pickPackWallService;
    }

//    @RequestMapping(value = "",
//            method = RequestMethod.POST,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<PickPackWallDTO> create(@RequestBody PickPackWallDTO dto) {
//        return ResponseEntity.ok(pickPackWallService.create(dto));
//    }
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody PickPackWallDTO dto) {
        pickPackWallService.createMore(dto);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        pickPackWallService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickPackWallDTO> update(@RequestBody PickPackWallDTO dto) {
        return ResponseEntity.ok(pickPackWallService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickPackWallDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(pickPackWallService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickPackWallDTO>> getAll() {
        return ResponseEntity.ok(pickPackWallService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickPackWallDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(pickPackWallService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PickPackWallDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(pickPackWallService.getBySearchTerm(search, pageable));
    }
}
