package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliveryPointDTO;
import com.mushiny.wms.masterdata.obbasics.service.DeliveryPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Laptop-11 on 2017/6/8.
 */
@RestController
@RequestMapping("/masterdata/outbound/delivery-points")
public class DeliveryPointController {

    private final DeliveryPointService deliveryPointService;

    @Autowired
    public DeliveryPointController(DeliveryPointService deliveryPointService) {
        this.deliveryPointService = deliveryPointService;
    }
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryPointDTO> create(@RequestBody DeliveryPointDTO dto){
        return ResponseEntity.ok(deliveryPointService.create(dto));
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deliveryPointService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryPointDTO> update(@RequestBody DeliveryPointDTO dto) {
        return ResponseEntity.ok(deliveryPointService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryPointDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(deliveryPointService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliveryPointDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(deliveryPointService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<DeliveryPointDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(deliveryPointService.getBySearchTerm(search, pageable));
    }
}
