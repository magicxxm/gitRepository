package com.mushiny.wms.system.web;


import com.mushiny.wms.system.crud.dto.ClientDTO;
import com.mushiny.wms.system.crud.dto.WarehouseDTO;
import com.mushiny.wms.system.service.WarehouseClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system")
public class WarehouseClientController {

    private final WarehouseClientService warehouseClientService;

    @Autowired
    public WarehouseClientController(WarehouseClientService warehouseClientService) {
        this.warehouseClientService = warehouseClientService;
    }

    @RequestMapping(value = "/warehouses/{id}/clients",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWarehouseClients(@PathVariable String id,
                                                       @RequestBody List<String> clients) {
        warehouseClientService.createWarehouseClients(id, clients);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/clients/{id}/warehouses",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createClientWarehouses(@PathVariable String id,
                                                       @RequestBody List<String> warehouses) {
        warehouseClientService.createClientWarehouses(id, warehouses);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/warehouse-client/warehouses",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WarehouseDTO>> getWarehouseList() {
        return ResponseEntity.ok(warehouseClientService.getWarehouseList());
    }

    @RequestMapping(value = "/warehouse-client/clients",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDTO>> getClientList() {
        return ResponseEntity.ok(warehouseClientService.getClientList());
    }

    @RequestMapping(value = "/clients/{id}/warehouses/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WarehouseDTO>> getAssignedWarehouseByClientId(@PathVariable String id) {
        return ResponseEntity.ok(warehouseClientService.getAssignedWarehouseByClientId(id));
    }

    @RequestMapping(value = "/clients/{id}/warehouses/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WarehouseDTO>> getUnassignedWarehouseByClientId(@PathVariable String id) {
        return ResponseEntity.ok(warehouseClientService.getUnassignedWarehouseByClientId(id));
    }

    @RequestMapping(value = "/warehouses/{id}/clients/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDTO>> getAssignedClientByWarehouseId(@PathVariable String id) {
        return ResponseEntity.ok(warehouseClientService.getAssignedClientByWarehouseId(id));
    }

    @RequestMapping(value = "/warehouses/{id}/clients/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDTO>> getUnassignedClientByWarehouseId(@PathVariable String id) {
        return ResponseEntity.ok(warehouseClientService.getUnassignedClientByWarehouseId(id));
    }
}
