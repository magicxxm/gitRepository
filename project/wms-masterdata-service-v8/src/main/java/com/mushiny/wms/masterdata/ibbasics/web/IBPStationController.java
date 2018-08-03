package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.IBPStationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationDTO;
import com.mushiny.wms.masterdata.ibbasics.service.IBPStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/ibp/ibp-stations")
public class IBPStationController {

    private final IBPStationService ibpStationService;

    @Autowired
    public IBPStationController(IBPStationService ibpStationService) {
        this.ibpStationService = ibpStationService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody IBPStationDTO dto) {
        ibpStationService.createMore(dto);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IBPStationDTO> update(@RequestBody IBPStationDTO dto) {
        return ResponseEntity.ok(ibpStationService.update(dto));
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        ibpStationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IBPStationDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(ibpStationService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IBPStationDTO>> getAll() {
        return ResponseEntity.ok(ibpStationService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IBPStationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(ibpStationService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<IBPStationDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(ibpStationService.getBySearchTerm(search, pageable));
    }
}
