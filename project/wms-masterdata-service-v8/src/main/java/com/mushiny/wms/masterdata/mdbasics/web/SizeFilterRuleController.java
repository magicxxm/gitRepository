package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.SizeFilterRuleDTO;
import com.mushiny.wms.masterdata.mdbasics.service.SizeFilterRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Laptop-11 on 2017/6/8.
 */
@RestController
@RequestMapping("/masterdata/size-filter-rules")
public class SizeFilterRuleController {

    private final SizeFilterRuleService sizeFilterRuleService;

    @Autowired
    public SizeFilterRuleController(SizeFilterRuleService sizeFilterRuleService) {
        this.sizeFilterRuleService = sizeFilterRuleService;
    }
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SizeFilterRuleDTO> create(@RequestBody SizeFilterRuleDTO dto){
        return ResponseEntity.ok(sizeFilterRuleService.create(dto));
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        sizeFilterRuleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SizeFilterRuleDTO> update(@RequestBody SizeFilterRuleDTO dto) {
        return ResponseEntity.ok(sizeFilterRuleService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SizeFilterRuleDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(sizeFilterRuleService.retrieve(id));
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SizeFilterRuleDTO>> getAll() {
        return ResponseEntity.ok(sizeFilterRuleService.getAll());
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SizeFilterRuleDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(sizeFilterRuleService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<SizeFilterRuleDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(sizeFilterRuleService.getBySearchTerm(search, pageable));
    }
}
