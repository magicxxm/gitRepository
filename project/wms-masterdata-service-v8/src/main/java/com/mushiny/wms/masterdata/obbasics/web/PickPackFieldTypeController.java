package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackFieldTypeDTO;
import com.mushiny.wms.masterdata.obbasics.service.PickPackFieldTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/pick-pack-field-types")
public class PickPackFieldTypeController {

    private final PickPackFieldTypeService pickPackFieldTypeService;

    @Autowired
    public PickPackFieldTypeController(PickPackFieldTypeService pickPackFieldTypeService) {
        this.pickPackFieldTypeService = pickPackFieldTypeService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickPackFieldTypeDTO> create(@RequestBody PickPackFieldTypeDTO dto) {
        return ResponseEntity.ok(pickPackFieldTypeService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        pickPackFieldTypeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickPackFieldTypeDTO> update(@RequestBody PickPackFieldTypeDTO dto) {
        return ResponseEntity.ok(pickPackFieldTypeService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickPackFieldTypeDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(pickPackFieldTypeService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickPackFieldTypeDTO>> getAll() {
        return ResponseEntity.ok(pickPackFieldTypeService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickPackFieldTypeDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(pickPackFieldTypeService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PickPackFieldTypeDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(pickPackFieldTypeService.getBySearchTerm(search, pageable));
    }
}
