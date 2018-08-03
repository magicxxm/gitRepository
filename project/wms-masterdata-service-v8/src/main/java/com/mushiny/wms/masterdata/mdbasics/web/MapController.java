package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.MapDTO;
import com.mushiny.wms.masterdata.mdbasics.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/wd/maps")
public class MapController {

    private final MapService mapService;

    @Autowired
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MapDTO> create(@RequestBody MapDTO dto) {
        return ResponseEntity.ok(mapService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        mapService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MapDTO> update(@RequestBody MapDTO dto) {
        return ResponseEntity.ok(mapService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MapDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(mapService.retrieve(id));
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MapDTO>> getAll() {
        return ResponseEntity.ok(mapService.getAll());
    }

//    @RequestMapping(value = "",
//            method = RequestMethod.GET,
//            params = {"sectionId"},
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<MapDTO>> getBySectionId(@RequestParam String sectionId) {
//        return ResponseEntity.ok(mapService.getBySectionId(sectionId));
//    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MapDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(mapService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MapDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(mapService.getBySearchTerm(search, pageable));
    }
}
