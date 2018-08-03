package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.CarrierDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliverySortCodeDTO;
import com.mushiny.wms.masterdata.obbasics.service.DeliverySortCodeService;
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
@RequestMapping("/masterdata/outbound/delivery-sort-codes")
public class DeliverySortCodeController {

    private final DeliverySortCodeService deliverySortCodeService;

    @Autowired
    public DeliverySortCodeController(DeliverySortCodeService deliverySortCodeService) {
        this.deliverySortCodeService = deliverySortCodeService;
    }
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliverySortCodeDTO> create(@RequestBody DeliverySortCodeDTO dto){

        return ResponseEntity.ok(deliverySortCodeService.create(dto));
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deliverySortCodeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliverySortCodeDTO> update(@RequestBody DeliverySortCodeDTO dto) {
        return ResponseEntity.ok(deliverySortCodeService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliverySortCodeDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(deliverySortCodeService.retrieve(id));
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliverySortCodeDTO>> getAll() {
        return ResponseEntity.ok(deliverySortCodeService.getAll());
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliverySortCodeDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(deliverySortCodeService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<DeliverySortCodeDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(deliverySortCodeService.getBySearchTerm(search, pageable));
    }
}
