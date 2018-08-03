package com.mushiny.wms.system.web;


import com.mushiny.wms.system.crud.dto.WarehouseDTO;
import com.mushiny.wms.system.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WarehouseDTO> create(@RequestBody WarehouseDTO dto) {
        WarehouseDTO warehouseDTO = warehouseService.create(dto);
        return ResponseEntity.ok(warehouseDTO);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        warehouseService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WarehouseDTO> update(@RequestBody WarehouseDTO dto) {
        WarehouseDTO warehouseDTO = warehouseService.update(dto);
        return ResponseEntity.ok(warehouseDTO);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WarehouseDTO> get(@PathVariable String id) {
        WarehouseDTO warehouseDTO = warehouseService.retrieve(id);
        return ResponseEntity.ok(warehouseDTO);
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WarehouseDTO>> getSelectList() {
        List<WarehouseDTO> warehouseDTOs = warehouseService.getByCurrentUserId();
        return ResponseEntity.ok(warehouseDTOs);
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = "search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WarehouseDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(warehouseService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<WarehouseDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(warehouseService.getBySearchTerm(search, pageable));
    }
}
