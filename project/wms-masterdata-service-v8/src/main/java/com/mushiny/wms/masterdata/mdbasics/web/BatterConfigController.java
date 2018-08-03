package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.BatterConfigDTO;
import com.mushiny.wms.masterdata.mdbasics.service.BatterConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/robot/batter-configs")
public class BatterConfigController {

    private final BatterConfigService batterConfigService;

    @Autowired
    public BatterConfigController(BatterConfigService batterConfigService) {
        this.batterConfigService = batterConfigService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatterConfigDTO> create(@RequestBody BatterConfigDTO dto) {
        return ResponseEntity.ok(batterConfigService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        batterConfigService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatterConfigDTO> update(@RequestBody BatterConfigDTO dto) {
        return ResponseEntity.ok(batterConfigService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatterConfigDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(batterConfigService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BatterConfigDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(batterConfigService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<BatterConfigDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(batterConfigService.getBySearchTerm(search, pageable));
    }
}
