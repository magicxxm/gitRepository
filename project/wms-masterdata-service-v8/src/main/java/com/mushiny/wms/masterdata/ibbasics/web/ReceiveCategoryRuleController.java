package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveCategoryRuleDTO;
import com.mushiny.wms.masterdata.ibbasics.service.ReceiveCategoryRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/inbound/receive-category-rules")
public class ReceiveCategoryRuleController {

    private final ReceiveCategoryRuleService receivingCategoryRuleService;

    @Autowired
    public ReceiveCategoryRuleController(ReceiveCategoryRuleService receivingCategoryRuleService) {
        this.receivingCategoryRuleService = receivingCategoryRuleService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveCategoryRuleDTO> update(@RequestBody ReceiveCategoryRuleDTO dto) {
        return ResponseEntity.ok(receivingCategoryRuleService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveCategoryRuleDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(receivingCategoryRuleService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceiveCategoryRuleDTO>> getAll() {
        return ResponseEntity.ok(receivingCategoryRuleService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceiveCategoryRuleDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(receivingCategoryRuleService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ReceiveCategoryRuleDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(receivingCategoryRuleService.getBySearchTerm(search, pageable));
    }
}
