package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataSerialNumberDTO;
import com.mushiny.wms.masterdata.mdbasics.service.ItemDataSerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/item-serialnumber")
public class ItemDataSerialNumberController {

    private final ItemDataSerialNumberService itemDataSerialNumberService;

    @Autowired
    public ItemDataSerialNumberController(ItemDataSerialNumberService itemDataSerialNumberService) {
        this.itemDataSerialNumberService = itemDataSerialNumberService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataSerialNumberDTO> create(@RequestBody ItemDataSerialNumberDTO dto) {
        System.out.println("1111");
        return ResponseEntity.ok(itemDataSerialNumberService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        itemDataSerialNumberService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataSerialNumberDTO> update(@RequestBody ItemDataSerialNumberDTO dto) {
        return ResponseEntity.ok(itemDataSerialNumberService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataSerialNumberDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(itemDataSerialNumberService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDataSerialNumberDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(itemDataSerialNumberService.getByClientId(clientId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDataSerialNumberDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(itemDataSerialNumberService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ItemDataSerialNumberDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(itemDataSerialNumberService.getBySearchTerm(search, pageable));
    }
}
