package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.LabelControllerDTO;
import com.mushiny.wms.masterdata.obbasics.service.LabelControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/label-controllers")
public class LabelControllerController {

    private final LabelControllerService labelControllerService;

    @Autowired
    public LabelControllerController(LabelControllerService labelControllerService) {
        this.labelControllerService = labelControllerService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody LabelControllerDTO dto) {
        labelControllerService.createMore(dto);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        labelControllerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LabelControllerDTO> update(@RequestBody LabelControllerDTO dto) {
        return ResponseEntity.ok(labelControllerService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LabelControllerDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(labelControllerService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LabelControllerDTO>> getAll() {
        return ResponseEntity.ok(labelControllerService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LabelControllerDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(labelControllerService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<LabelControllerDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(labelControllerService.getBySearchTerm(search, pageable));
    }
}
