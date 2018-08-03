package com.mushiny.wms.system.web;

import com.mushiny.wms.system.crud.dto.SelectionDTO;
import com.mushiny.wms.system.service.SelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/selections")
public class SelectionController {

    private final SelectionService selectionService;

    @Autowired
    public SelectionController(SelectionService selectionService) {
        this.selectionService = selectionService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SelectionDTO> create(@RequestBody SelectionDTO dto) {
        SelectionDTO selectionDTO = selectionService.create(dto);
        return ResponseEntity.ok(selectionDTO);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        selectionService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SelectionDTO> update(@RequestBody SelectionDTO dto) {
        SelectionDTO selectionDTO = selectionService.update(dto);
        return ResponseEntity.ok(selectionDTO);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SelectionDTO> get(@PathVariable String id) {
        SelectionDTO selectionDTO = selectionService.retrieve(id);
        return ResponseEntity.ok(selectionDTO);
    }

    @RequestMapping(value = "",
            params = "selectionKey",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SelectionDTO>> getBySelectionKey(@RequestParam String selectionKey) {
        List<SelectionDTO> selectionDTOs = selectionService.getBySelectionKey(selectionKey);
        return ResponseEntity.ok(selectionDTOs);
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = "search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SelectionDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(selectionService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<SelectionDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(selectionService.getBySearchTerm(search, pageable));
    }
}
