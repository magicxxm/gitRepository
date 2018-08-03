package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReplenishStrategyDTO;
import com.mushiny.wms.masterdata.ibbasics.service.ReplenishStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/inbound/replenish-strategies")
public class ReplenishStrategyController {

    private final ReplenishStrategyService replenishStrategyService;

    @Autowired
    public ReplenishStrategyController(ReplenishStrategyService replenishStrategyService) {
        this.replenishStrategyService = replenishStrategyService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReplenishStrategyDTO> create(@RequestBody ReplenishStrategyDTO dto) {
        return ResponseEntity.ok(replenishStrategyService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        replenishStrategyService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReplenishStrategyDTO> update(@RequestBody ReplenishStrategyDTO dto) {
        return ResponseEntity.ok(replenishStrategyService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReplenishStrategyDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(replenishStrategyService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplenishStrategyDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(replenishStrategyService.getByClientId(clientId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplenishStrategyDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(replenishStrategyService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ReplenishStrategyDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(replenishStrategyService.getBySearchTerm(search, pageable));
    }
}
