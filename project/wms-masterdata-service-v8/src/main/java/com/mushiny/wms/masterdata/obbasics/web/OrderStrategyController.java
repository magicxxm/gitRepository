package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.OrderStrategyDTO;
import com.mushiny.wms.masterdata.obbasics.service.OrderStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/order-strategies")
public class OrderStrategyController {

    private final OrderStrategyService orderStrategyService;

    @Autowired
    public OrderStrategyController(OrderStrategyService orderStrategyService) {
        this.orderStrategyService = orderStrategyService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderStrategyDTO> create(@RequestBody OrderStrategyDTO dto) {
        return ResponseEntity.ok(orderStrategyService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        orderStrategyService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderStrategyDTO> update(@RequestBody OrderStrategyDTO dto) {
        return ResponseEntity.ok(orderStrategyService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderStrategyDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(orderStrategyService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderStrategyDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(orderStrategyService.getByClientId(clientId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderStrategyDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(orderStrategyService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<OrderStrategyDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(orderStrategyService.getBySearchTerm(search, pageable));
    }
}
