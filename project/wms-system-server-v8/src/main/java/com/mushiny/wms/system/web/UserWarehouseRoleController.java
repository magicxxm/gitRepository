package com.mushiny.wms.system.web;


import com.mushiny.wms.system.crud.dto.RoleDTO;
import com.mushiny.wms.system.crud.dto.UserDTO;
import com.mushiny.wms.system.service.UserWarehouseRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system")
public class UserWarehouseRoleController {

    private final UserWarehouseRoleService userWarehouseRoleService;

    @Autowired
    public UserWarehouseRoleController(UserWarehouseRoleService userWarehouseRoleService) {
        this.userWarehouseRoleService = userWarehouseRoleService;
    }

    @RequestMapping(value = "/warehouses/{warehouseId}/users/{userId}/roles",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWarehouseUserRoles(@PathVariable String userId,
                                                         @PathVariable String warehouseId,
                                                         @RequestBody List<String> roles) {
        userWarehouseRoleService.createWarehouseUserRoles(warehouseId, userId, roles);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/warehouses/{warehouseId}/roles/{roleId}/users",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWarehousesRoleUsers(@PathVariable String roleId,
                                                          @PathVariable String warehouseId,
                                                          @RequestBody List<String> users) {
        userWarehouseRoleService.createWarehousesRoleUsers(warehouseId, roleId, users);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/user-warehouse-role/warehouses/{id}/users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getWarehouseUserList(@PathVariable String id) {
        return ResponseEntity.ok(userWarehouseRoleService.getWarehouseUserList(id));
    }

    @RequestMapping(value = "/user-warehouse-role/warehouses/{id}/roles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleDTO>> getUserList(@PathVariable String id) {
        return ResponseEntity.ok(userWarehouseRoleService.getWarehouseRoleList(id));
    }

    @RequestMapping(value = "/warehouses/{warehouseId}/roles/{roleId}/users/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAssignedUserByWarehouseIdAndRoleId(@PathVariable String roleId,
                                                                               @PathVariable String warehouseId) {
        return ResponseEntity.ok(userWarehouseRoleService.getAssignedUserByWarehouseIdAndRoleId(warehouseId, roleId));
    }

    @RequestMapping(value = "/warehouses/{warehouseId}/roles/{roleId}/users/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUnassignedUserByWarehouseIdAndClientId(@PathVariable String roleId,
                                                                                   @PathVariable String warehouseId) {
        return ResponseEntity.ok(userWarehouseRoleService.getUnassignedUserByWarehouseIdAndRoleId(warehouseId, roleId));
    }

    @RequestMapping(value = "/warehouses/{warehouseId}/users/{userId}/roles/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleDTO>> getAssignedClientByWarehouseIdAndUserId(@PathVariable String userId,
                                                                                 @PathVariable String warehouseId) {
        return ResponseEntity.ok(userWarehouseRoleService.getAssignedRoleByWarehouseIdAndUserId(warehouseId, userId));
    }

    @RequestMapping(value = "/warehouses/{warehouseId}/users/{userId}/roles/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleDTO>> getUnassignedClientByWarehouseIdAndUserId(@PathVariable String userId,
                                                                                   @PathVariable String warehouseId) {
        return ResponseEntity.ok(userWarehouseRoleService.getUnassignedRoleByWarehouseIdAndUserId(warehouseId, userId));
    }
}
