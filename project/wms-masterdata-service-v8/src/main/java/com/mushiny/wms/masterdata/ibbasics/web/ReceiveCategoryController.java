package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveCategoryDTO;
import com.mushiny.wms.masterdata.ibbasics.service.ReceiveCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/inbound/receive-categories")
public class ReceiveCategoryController {

    private final ReceiveCategoryService receivingCategoryService;

    @Autowired
    public ReceiveCategoryController(ReceiveCategoryService receivingCategoryService) {
        this.receivingCategoryService = receivingCategoryService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveCategoryDTO> create(@RequestBody ReceiveCategoryDTO dto) {
        return ResponseEntity.ok(receivingCategoryService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        receivingCategoryService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveCategoryDTO> update(@RequestBody ReceiveCategoryDTO dto) {
        return ResponseEntity.ok(receivingCategoryService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveCategoryDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(receivingCategoryService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceiveCategoryDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(receivingCategoryService.getByClientId(clientId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceiveCategoryDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(receivingCategoryService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ReceiveCategoryDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(receivingCategoryService.getBySearchTerm(search, pageable));
    }
}
