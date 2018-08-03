package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPCellTypeDTO;
import com.mushiny.wms.masterdata.obbasics.service.OBPCellTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/obp/obp-cell-types")
public class OBPCellTypeController {

    private final OBPCellTypeService obpCellTypeService;

    @Autowired
    public OBPCellTypeController(OBPCellTypeService obpCellTypeService) {
        this.obpCellTypeService = obpCellTypeService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPCellTypeDTO> create(@RequestBody OBPCellTypeDTO dto) {
        return ResponseEntity.ok(obpCellTypeService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        obpCellTypeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPCellTypeDTO> update(@RequestBody OBPCellTypeDTO dto) {
        return ResponseEntity.ok(obpCellTypeService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OBPCellTypeDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(obpCellTypeService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBPCellTypeDTO>> getAll() {
        return ResponseEntity.ok(obpCellTypeService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OBPCellTypeDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(obpCellTypeService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<OBPCellTypeDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(obpCellTypeService.getBySearchTerm(search, pageable));
    }
}
