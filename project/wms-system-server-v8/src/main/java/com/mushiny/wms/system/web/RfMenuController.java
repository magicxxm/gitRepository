package com.mushiny.wms.system.web;

import com.mushiny.wms.system.crud.dto.ModuleDTO;
import com.mushiny.wms.system.crud.dto.RfMenuDTO;
import com.mushiny.wms.system.crud.dto.RfMenuItemDTO;
import com.mushiny.wms.system.service.RfMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/rf-menus")
public class RfMenuController {

    private final RfMenuService rfMenuService;

    @Autowired
    public RfMenuController(RfMenuService rfMenuService) {
        this.rfMenuService = rfMenuService;
    }

    @RequestMapping(value = "/{parentId}/rf-menus",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@PathVariable String parentId,
                                       @RequestBody List<RfMenuDTO> rfMenuDTOS) {
        rfMenuService.createAll(parentId, rfMenuDTOS);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@RequestBody List<RfMenuDTO> rfMenuDTOS) {
        rfMenuService.updateMore(rfMenuDTOS);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RfMenuItemDTO>> getAll() {
        return ResponseEntity.ok(rfMenuService.getMenuItem());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"moduleId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RfMenuDTO>> getByModuleId(@RequestParam String moduleId) {
        return ResponseEntity.ok(rfMenuService.getByModuleId(moduleId));
    }

    @RequestMapping(value = "/root/modules/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ModuleDTO>> getAssignedModuleByRoot() {
        return ResponseEntity.ok(rfMenuService.getAssignedModuleByRoot());
    }

    @RequestMapping(value = "/{parentId}/modules/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ModuleDTO>> getAssignedModuleByParentId(@PathVariable String parentId) {
        return ResponseEntity.ok(rfMenuService.getAssignedModuleByParentId(parentId));
    }

    @RequestMapping(value = "/{parentId}/modules/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ModuleDTO>> getUnassignedModuleByParentId(@PathVariable String parentId) {
        return ResponseEntity.ok(rfMenuService.getUnassignedModuleByParentId(parentId));
    }
}
