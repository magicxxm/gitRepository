package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackCellDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackWallDTO;
import com.mushiny.wms.masterdata.obbasics.service.PickPackCellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/pick-pack-cells")
public class PickPackCellController {

    private final PickPackCellService pickPackCellService;

    @Autowired
    public PickPackCellController(PickPackCellService pickPackCellService) {
        this.pickPackCellService = pickPackCellService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickPackCellDTO> create(@RequestBody PickPackCellDTO dto) {
        return ResponseEntity.ok(pickPackCellService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        pickPackCellService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickPackCellDTO> update(@RequestBody PickPackCellDTO dto) {
        return ResponseEntity.ok(pickPackCellService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PickPackCellDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(pickPackCellService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickPackCellDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(pickPackCellService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PickPackCellDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(pickPackCellService.getBySearchTerm(search, pageable));
    }

    @RequestMapping(value = "/batchUpdate",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateMore(@RequestBody PickPackWallDTO dto) {
        pickPackCellService.updateMore(dto);
        return ResponseEntity.ok().build();
    }
}
