package com.mushiny.wms.system.web;

import com.mushiny.wms.system.crud.dto.ResourceDTO;
import com.mushiny.wms.system.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/resources")
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceDTO> create(@RequestBody ResourceDTO dto) {
        ResourceDTO resourceDTO = resourceService.create(dto);
        return ResponseEntity.ok(resourceDTO);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        resourceService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceDTO> update(@RequestBody ResourceDTO dto) {
        ResourceDTO resourceDTO = resourceService.update(dto);
        return ResponseEntity.ok(resourceDTO);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceDTO> get(@PathVariable String id) {
        ResourceDTO resourceDTO = resourceService.retrieve(id);
        return ResponseEntity.ok(resourceDTO);
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = "locale",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getByLocale(@RequestParam String locale) {
        return ResponseEntity.ok(resourceService.getByLocale(locale));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = "search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResourceDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(resourceService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ResourceDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(resourceService.getBySearchTerm(search, pageable));
    }
}
