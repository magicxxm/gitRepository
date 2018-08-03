package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveThresholdDTO;
import com.mushiny.wms.masterdata.ibbasics.service.ReceiveThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/inbound/receive-thresholds")
public class ReceiveThresholdController {

    private final ReceiveThresholdService receiveThresholdService;

    @Autowired
    public ReceiveThresholdController(ReceiveThresholdService receiveThresholdService) {
        this.receiveThresholdService = receiveThresholdService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveThresholdDTO> create(@RequestBody ReceiveThresholdDTO dto) {
        return ResponseEntity.ok(receiveThresholdService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        receiveThresholdService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveThresholdDTO> update(@RequestBody ReceiveThresholdDTO dto) {
        return ResponseEntity.ok(receiveThresholdService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveThresholdDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(receiveThresholdService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceiveThresholdDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(receiveThresholdService.getByClientId(clientId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceiveThresholdDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(receiveThresholdService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ReceiveThresholdDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(receiveThresholdService.getBySearchTerm(search, pageable));
    }
}
