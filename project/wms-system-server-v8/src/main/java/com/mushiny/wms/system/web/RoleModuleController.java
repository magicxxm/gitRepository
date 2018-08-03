package com.mushiny.wms.system.web;

import com.mushiny.wms.system.crud.dto.ModuleDTO;
import com.mushiny.wms.system.crud.dto.RoleDTO;
import com.mushiny.wms.system.service.RoleModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system")
public class RoleModuleController {

    private final RoleModuleService roleModuleService;

    @Autowired
    public RoleModuleController(RoleModuleService roleModuleService) {
        this.roleModuleService = roleModuleService;
    }

    @RequestMapping(value = "/modules/{id}/roles",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createModuleRoles(@PathVariable String id,
                                                  @RequestBody List<String> roles) {
        roleModuleService.createModuleRoles(id, roles);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/roles/{id}/modules",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createRoleModules(@PathVariable String id,
                                                  @RequestBody List<String> modules) {
        roleModuleService.createRoleModules(id, modules);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/role-module/roles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleDTO>> getRoleList() {
        return ResponseEntity.ok(roleModuleService.getRoleList());
    }

    @RequestMapping(value = "/role-module/modules",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ModuleDTO>> getModuleList() {
        return ResponseEntity.ok(roleModuleService.getModuleList());
    }

    @RequestMapping(value = "/modules/{id}/roles/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleDTO>> getAssignedRoleByModuleId(@PathVariable String id) {
        return ResponseEntity.ok(roleModuleService.getAssignedRoleByModuleId(id));
    }

    @RequestMapping(value = "/modules/{id}/roles/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleDTO>> getUnassignedRoleByModuleId(@PathVariable String id) {
        return ResponseEntity.ok(roleModuleService.getUnassignedRoleByModuleId(id));
    }

    @RequestMapping(value = "/roles/{id}/modules/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ModuleDTO>> getAssignedModuleByRoleId(@PathVariable String id) {
        return ResponseEntity.ok(roleModuleService.getAssignedModuleByRoleId(id));
    }

    @RequestMapping(value = "/roles/{id}/modules/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ModuleDTO>> getUnassignedModuleByRoleId(@PathVariable String id) {
        return ResponseEntity.ok(roleModuleService.getUnassignedModuleByRoleId(id));
    }
}
