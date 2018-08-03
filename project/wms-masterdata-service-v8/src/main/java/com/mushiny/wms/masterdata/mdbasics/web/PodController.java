package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.masterdata.mdbasics.business.dto.PodStorageLocationsDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.NodeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.PodDTO;
import com.mushiny.wms.masterdata.mdbasics.service.PodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/pods")
public class PodController {

    private final PodService mdPodService;

    @Autowired
    public PodController(PodService mdPodService) {
        this.mdPodService = mdPodService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PodDTO> create(@RequestBody PodDTO dto) {
        return ResponseEntity.ok(mdPodService.create(dto));
    }

    @RequestMapping(value = "/storage-locations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody PodStorageLocationsDTO dto) {
        mdPodService.createMore(dto);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        mdPodService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PodDTO> update(@RequestBody PodDTO dto) {
        return ResponseEntity.ok(mdPodService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PodDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(mdPodService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PodDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(mdPodService.getByClientId(clientId));
    }

    @RequestMapping(value = "/placeMark",
            method = RequestMethod.GET,
            params = {"id"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NodeDTO>> getPlaceMark(@RequestParam String id) {
        return ResponseEntity.ok(mdPodService.getPlaceMark(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PodDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(mdPodService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PodDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(mdPodService.getBySearchTerm(search, pageable));
    }
}
