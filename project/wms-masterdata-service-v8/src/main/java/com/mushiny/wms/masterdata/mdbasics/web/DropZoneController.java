package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.DropZoneDTO;
import com.mushiny.wms.masterdata.mdbasics.service.DropZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/drop-zones")
public class DropZoneController {

    private final DropZoneService dropZoneService;

    @Autowired
    public DropZoneController(DropZoneService dropZoneService) {
        this.dropZoneService = dropZoneService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DropZoneDTO> create(@RequestBody DropZoneDTO dto) {
        return ResponseEntity.ok(dropZoneService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        dropZoneService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DropZoneDTO> update(@RequestBody DropZoneDTO dto) {
        return ResponseEntity.ok(dropZoneService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DropZoneDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(dropZoneService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DropZoneDTO>> getAll() {
        return ResponseEntity.ok(dropZoneService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DropZoneDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(dropZoneService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<DropZoneDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(dropZoneService.getBySearchTerm(search, pageable));
    }
}
