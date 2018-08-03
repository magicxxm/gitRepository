package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.SortingStationDTO;
import com.mushiny.wms.masterdata.obbasics.service.SortingStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/sorting-stations")
public class SortingStationController {

    private final SortingStationService sortingStationService;

    @Autowired
    public SortingStationController(SortingStationService sortingStationService) {
        this.sortingStationService = sortingStationService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody SortingStationDTO dto) {
        sortingStationService.createMore(dto);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SortingStationDTO> update(@RequestBody SortingStationDTO dto) {
        return ResponseEntity.ok(sortingStationService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        sortingStationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SortingStationDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(sortingStationService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SortingStationDTO>> getAll() {
        return ResponseEntity.ok(sortingStationService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SortingStationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(sortingStationService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<SortingStationDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(sortingStationService.getBySearchTerm(search, pageable));
    }
}
