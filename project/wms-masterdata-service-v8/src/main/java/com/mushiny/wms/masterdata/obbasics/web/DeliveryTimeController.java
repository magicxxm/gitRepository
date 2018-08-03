package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.CarrierDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliverySortCodeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliveryTimeDTO;
import com.mushiny.wms.masterdata.obbasics.service.DeliveryTimeService;
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
@RequestMapping("/masterdata/outbound/delivery-times")
public class DeliveryTimeController {

    private final DeliveryTimeService deliveryTimeService;

    @Autowired
    public DeliveryTimeController(DeliveryTimeService deliveryTimeService) {
        this.deliveryTimeService = deliveryTimeService;
    }
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryTimeDTO> create(@RequestBody DeliveryTimeDTO dto){
        return ResponseEntity.ok(deliveryTimeService.create(dto));
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deliveryTimeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryTimeDTO> update(@RequestBody DeliveryTimeDTO dto) {
        return ResponseEntity.ok(deliveryTimeService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryTimeDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(deliveryTimeService.retrieve(id));
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliveryTimeDTO>> getAll() {
        return ResponseEntity.ok(deliveryTimeService.getAll());
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliveryTimeDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(deliveryTimeService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<DeliveryTimeDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(deliveryTimeService.getBySearchTerm(search, pageable));
    }
}
