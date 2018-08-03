package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.DigitalLabelDTO;
import com.mushiny.wms.masterdata.obbasics.service.DigitalLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/digital-labels")
public class DigitalLabelController {

    private final DigitalLabelService digitalLabelService;

    @Autowired
    public DigitalLabelController(DigitalLabelService digitalLabelService) {
        this.digitalLabelService = digitalLabelService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DigitalLabelDTO> create(@RequestBody DigitalLabelDTO dto) {
        return ResponseEntity.ok(digitalLabelService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        digitalLabelService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DigitalLabelDTO> update(@RequestBody DigitalLabelDTO dto) {
        return ResponseEntity.ok(digitalLabelService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DigitalLabelDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(digitalLabelService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DigitalLabelDTO>> getAll() {
        return ResponseEntity.ok(digitalLabelService.getAll());
    }

    @RequestMapping(value = "/labelId/{ids}",
            method = RequestMethod.GET,
//            params = {"labelId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DigitalLabelDTO>> getByLabel(@PathVariable List<String> ids){
        return ResponseEntity.ok(digitalLabelService.getByLabel(ids));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DigitalLabelDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(digitalLabelService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<DigitalLabelDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(digitalLabelService.getBySearchTerm(search, pageable));
    }
}
