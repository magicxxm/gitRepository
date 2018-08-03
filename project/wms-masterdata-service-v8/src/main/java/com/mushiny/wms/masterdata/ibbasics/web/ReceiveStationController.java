package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationDTO;
import com.mushiny.wms.masterdata.ibbasics.service.ReceiveStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/inbound/receive-stations")
public class ReceiveStationController {

    private final ReceiveStationService receivingStationService;

    @Autowired
    public ReceiveStationController(ReceiveStationService receivingStationService) {
        this.receivingStationService = receivingStationService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody ReceiveStationDTO dto) {
        receivingStationService.createMore(dto);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveStationDTO> update(@RequestBody ReceiveStationDTO dto) {
        return ResponseEntity.ok(receivingStationService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveStationDTO> delete(@PathVariable String id) {
        receivingStationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveStationDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(receivingStationService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceiveStationDTO>> getAll() {
        return ResponseEntity.ok(receivingStationService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceiveStationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(receivingStationService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ReceiveStationDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(receivingStationService.getBySearchTerm(search, pageable));
    }
}
