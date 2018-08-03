package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.AreaDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.MapDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.SabcRuleDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.SabcRule;
import com.mushiny.wms.masterdata.mdbasics.service.AreaService;
import com.mushiny.wms.masterdata.mdbasics.service.SabcRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/sabc-rules")
public class SabcRuleController {

    private final SabcRuleService sabcRuleService;

    @Autowired
    public SabcRuleController(SabcRuleService sabcRuleService) {
        this.sabcRuleService = sabcRuleService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SabcRuleDTO> create(@RequestBody SabcRuleDTO dto) {
        return ResponseEntity.ok(sabcRuleService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        sabcRuleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SabcRuleDTO> update(@RequestBody SabcRuleDTO dto) {
        return ResponseEntity.ok(sabcRuleService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SabcRuleDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(sabcRuleService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SabcRuleDTO>> getAll() {
        return ResponseEntity.ok(sabcRuleService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SabcRuleDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(sabcRuleService.getByClientId(clientId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SabcRuleDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(sabcRuleService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<SabcRuleDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(sabcRuleService.getBySearchTerm(search, pageable));
    }
}
