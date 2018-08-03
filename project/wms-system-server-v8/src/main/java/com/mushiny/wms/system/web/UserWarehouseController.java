package com.mushiny.wms.system.web;


import com.mushiny.wms.system.crud.dto.UserDTO;
import com.mushiny.wms.system.crud.dto.WarehouseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system")
public class UserWarehouseController {

    private final com.mushiny.wms.system.service.UserWarehouseService UserWarehouseService;

    @Autowired
    public UserWarehouseController(com.mushiny.wms.system.service.UserWarehouseService UserWarehouseService) {
        this.UserWarehouseService = UserWarehouseService;
    }

    @RequestMapping(value = "/warehouses/{id}/users",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWarehouseClients(@PathVariable String id,
                                                       @RequestBody List<String> users) {
        UserWarehouseService.createWarehouseUsers(id, users);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/users/{id}/warehouses",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUserWarehouses(@PathVariable String id,
                                                     @RequestBody List<String> warehouses) {
        UserWarehouseService.createUserWarehouses(id, warehouses);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/user-warehouse/warehouses",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WarehouseDTO>> getWarehouseList() {
        return ResponseEntity.ok(UserWarehouseService.getWarehouseList());
    }

    @RequestMapping(value = "/user-warehouse/users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUserList() {
        return ResponseEntity.ok(UserWarehouseService.getUserList());
    }

    @RequestMapping(value = "/users/{id}/warehouses/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WarehouseDTO>> getAssignedWarehouseByUserId(@PathVariable String id) {
        return ResponseEntity.ok(UserWarehouseService.getAssignedWarehouseByUserId(id));
    }

    @RequestMapping(value = "/users/{id}/warehouses/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WarehouseDTO>> getUnassignedWarehouseByClientId(@PathVariable String id) {
        return ResponseEntity.ok(UserWarehouseService.getUnassignedWarehouseByUserId(id));
    }

    @RequestMapping(value = "/warehouses/{id}/users/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAssignedUserByWarehouseId(@PathVariable String id) {
        return ResponseEntity.ok(UserWarehouseService.getAssignedUserByWarehouseId(id));
    }

    @RequestMapping(value = "/warehouses/{id}/users/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUnassignedUserByWarehouseId(@PathVariable String id) {
        return ResponseEntity.ok(UserWarehouseService.getUnassignedUserByWarehouseId(id));
    }
}
