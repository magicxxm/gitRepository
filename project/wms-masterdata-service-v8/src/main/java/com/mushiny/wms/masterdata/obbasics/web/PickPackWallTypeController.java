package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackWallTypeDTO;
import com.mushiny.wms.masterdata.obbasics.service.PickPackWallTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/pick-pack-wall-types")
public class PickPackWallTypeController {

    private final PickPackWallTypeService pickPackWallTypeService;

    @Autowired
    public PickPackWallTypeController(PickPackWallTypeService pickPackWallTypeService) {
        this.pickPackWallTypeService = pickPackWallTypeService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickPackWallTypeDTO> create(@RequestBody PickPackWallTypeDTO dto) {
        return ResponseEntity.ok(pickPackWallTypeService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        pickPackWallTypeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickPackWallTypeDTO> update(@RequestBody PickPackWallTypeDTO dto) {
        return ResponseEntity.ok(pickPackWallTypeService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickPackWallTypeDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(pickPackWallTypeService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickPackWallTypeDTO>> getAll() {
        return ResponseEntity.ok(pickPackWallTypeService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickPackWallTypeDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(pickPackWallTypeService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PickPackWallTypeDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(pickPackWallTypeService.getBySearchTerm(search, pageable));
    }
}
