package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.masterdata.mdbasics.business.dto.BayStorageLocationsDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.BayDTO;
import com.mushiny.wms.masterdata.mdbasics.service.BayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/bays")
public class BayController {

    private final BayService bayService;

    @Autowired
    public BayController(BayService bayService) {
        this.bayService = bayService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BayDTO> create(@RequestBody BayDTO dto) {
        return ResponseEntity.ok(bayService.create(dto));
    }

    @RequestMapping(value = "/storage-locations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody BayStorageLocationsDTO dto) {
        bayService.createMore(dto);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        bayService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BayDTO> update(@RequestBody BayDTO dto) {
        return ResponseEntity.ok(bayService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BayDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(bayService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BayDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(bayService.getByClientId(clientId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BayDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(bayService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<BayDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(bayService.getBySearchTerm(search, pageable));
    }
}
