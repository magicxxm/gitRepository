package com.mushiny.wms.system.web;

import com.mushiny.wms.system.crud.dto.ModuleDTO;
import com.mushiny.wms.system.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/modules")
public class ModuleController {

    private final ModuleService moduleService;

    @Autowired
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModuleDTO> create(@RequestBody ModuleDTO dto) {
        ModuleDTO moduleDTO = moduleService.create(dto);
        return ResponseEntity.ok(moduleDTO);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        moduleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModuleDTO> update(@RequestBody ModuleDTO dto) {
        ModuleDTO moduleDTO = moduleService.update(dto);
        return ResponseEntity.ok(moduleDTO);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModuleDTO> get(@PathVariable String id) {
        ModuleDTO moduleDTO = moduleService.retrieve(id);
        return ResponseEntity.ok(moduleDTO);
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ModuleDTO>> getByCurrentWarehouseIdAndUserId() {
        List<ModuleDTO> moduleDTOs = moduleService.getByCurrentWarehouseIdAndUserId();
        return ResponseEntity.ok(moduleDTOs);
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = "search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ModuleDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(moduleService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ModuleDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(moduleService.getBySearchTerm(search, pageable));
    }
}
