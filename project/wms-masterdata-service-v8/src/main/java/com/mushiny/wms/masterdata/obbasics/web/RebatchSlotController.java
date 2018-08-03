package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchSlotDTO;
import com.mushiny.wms.masterdata.obbasics.service.RebatchSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/rebatch-slots")
public class RebatchSlotController {

    private final RebatchSlotService rebatchSlotService;

    @Autowired
    public RebatchSlotController(RebatchSlotService rebatchSlotService) {
        this.rebatchSlotService = rebatchSlotService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RebatchSlotDTO> create(@RequestBody RebatchSlotDTO dto) {
        return ResponseEntity.ok(rebatchSlotService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        rebatchSlotService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RebatchSlotDTO> update(@RequestBody RebatchSlotDTO dto) {
        return ResponseEntity.ok(rebatchSlotService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RebatchSlotDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(rebatchSlotService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RebatchSlotDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(rebatchSlotService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<RebatchSlotDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(rebatchSlotService.getBySearchTerm(search, pageable));
    }
}
