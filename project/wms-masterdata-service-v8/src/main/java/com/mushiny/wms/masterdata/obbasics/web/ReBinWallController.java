package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinWallDTO;
import com.mushiny.wms.masterdata.obbasics.service.ReBinWallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/rebin-walls")
public class ReBinWallController {

    private final ReBinWallService reBinWallService;

    @Autowired
    public ReBinWallController(ReBinWallService reBinWallService) {
        this.reBinWallService = reBinWallService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody ReBinWallDTO dto) {
        reBinWallService.createMore(dto);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        reBinWallService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReBinWallDTO> update(@RequestBody ReBinWallDTO dto) {
        return ResponseEntity.ok(reBinWallService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReBinWallDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(reBinWallService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReBinWallDTO>> getAll() {
        return ResponseEntity.ok(reBinWallService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReBinWallDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(reBinWallService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ReBinWallDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(reBinWallService.getBySearchTerm(search, pageable));
    }
}
