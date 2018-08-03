package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveDestinationDTO;
import com.mushiny.wms.masterdata.ibbasics.service.ReceiveDestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/inbound/receive-destinations")
public class ReceiveDestinationController {

    private final ReceiveDestinationService receivingDestinationService;

    @Autowired
    public ReceiveDestinationController(ReceiveDestinationService receivingDestinationService) {
        this.receivingDestinationService = receivingDestinationService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveDestinationDTO> create(@RequestBody ReceiveDestinationDTO dto) {
        return ResponseEntity.ok(receivingDestinationService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        receivingDestinationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveDestinationDTO> update(@RequestBody ReceiveDestinationDTO dto) {
        return ResponseEntity.ok(receivingDestinationService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveDestinationDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(receivingDestinationService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceiveDestinationDTO>> getAll() {
        return ResponseEntity.ok(receivingDestinationService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceiveDestinationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(receivingDestinationService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ReceiveDestinationDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(receivingDestinationService.getBySearchTerm(search, pageable));
    }
}
