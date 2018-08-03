package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.ChargingPileDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.NodeDTO;
import com.mushiny.wms.masterdata.mdbasics.service.ChargingPileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/robot/charging-piles")
public class ChargingPileController {

    private final ChargingPileService chargingPileService;

    @Autowired
    public ChargingPileController(ChargingPileService chargingPileService) {

        this.chargingPileService = chargingPileService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChargingPileDTO> create(@RequestBody ChargingPileDTO dto) {
        return ResponseEntity.ok(chargingPileService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        chargingPileService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChargingPileDTO> update(@RequestBody ChargingPileDTO dto) {
        return ResponseEntity.ok(chargingPileService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChargingPileDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(chargingPileService.retrieve(id));
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChargingPileDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(chargingPileService.getBySearchTerm(search, sort));
    }
//    @RequestMapping(value = "/placeMark",
//            method = RequestMethod.GET,
//            params = {"id"},
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<NodeDTO>> getPlaceMark(@RequestParam String id) {
//        return ResponseEntity.ok(chargingPileService.getPlaceMark(id));
//    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ChargingPileDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(chargingPileService.getBySearchTerm(search, pageable));
    }
}
