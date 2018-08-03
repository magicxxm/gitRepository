package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinWallTypeDTO;
import com.mushiny.wms.masterdata.obbasics.service.ReBinWallTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/rebin-wall-types")
public class ReBinWallTypeController {

    private final ReBinWallTypeService reBinWallTypeService;

    @Autowired
    public ReBinWallTypeController(ReBinWallTypeService reBinWallTypeService) {
        this.reBinWallTypeService = reBinWallTypeService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReBinWallTypeDTO> create(@RequestBody ReBinWallTypeDTO dto) {
        return ResponseEntity.ok(reBinWallTypeService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        reBinWallTypeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReBinWallTypeDTO> update(@RequestBody ReBinWallTypeDTO dto) {
        return ResponseEntity.ok(reBinWallTypeService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReBinWallTypeDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(reBinWallTypeService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReBinWallTypeDTO>> getAll() {
        return ResponseEntity.ok(reBinWallTypeService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReBinWallTypeDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(reBinWallTypeService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ReBinWallTypeDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(reBinWallTypeService.getBySearchTerm(search, pageable));
    }
}
