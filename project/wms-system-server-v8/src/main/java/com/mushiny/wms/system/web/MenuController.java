package com.mushiny.wms.system.web;

import com.mushiny.wms.system.crud.dto.MenuDTO;
import com.mushiny.wms.system.crud.dto.MenuItemDTO;
import com.mushiny.wms.system.crud.dto.ModuleDTO;
import com.mushiny.wms.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menus")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @RequestMapping(value = "/{parentId}/menus",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@PathVariable String parentId,
                                       @RequestBody List<MenuDTO> menuDTOs) {
        menuService.createAll(parentId, menuDTOs);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@RequestBody List<MenuDTO> menuDTOs) {
        menuService.updateMore(menuDTOs);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuItemDTO>> getAll() {
        return ResponseEntity.ok(menuService.getMenuItem());
    }

    @RequestMapping(value = "/root/modules/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ModuleDTO>> getAssignedModuleByRoot() {
        return ResponseEntity.ok(menuService.getAssignedModuleByRoot());
    }

    @RequestMapping(value = "/{parentId}/modules/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ModuleDTO>> getAssignedModuleByParentId(@PathVariable String parentId) {
        return ResponseEntity.ok(menuService.getAssignedModuleByParentId(parentId));
    }

    @RequestMapping(value = "/{parentId}/modules/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ModuleDTO>> getUnassignedModuleByParentId(@PathVariable String parentId) {
        return ResponseEntity.ok(menuService.getUnassignedModuleByParentId(parentId));
    }
}
