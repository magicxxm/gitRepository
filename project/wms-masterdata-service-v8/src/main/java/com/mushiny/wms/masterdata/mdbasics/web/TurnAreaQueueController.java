package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.TurnAreaQueueDTO;
import com.mushiny.wms.masterdata.mdbasics.service.TurnAreaQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/turn-area-queues")
public class TurnAreaQueueController {

    private final TurnAreaQueueService trurnAreaQueueService;

    @Autowired
    public TurnAreaQueueController(TurnAreaQueueService trurnAreaQueueService) {
        this.trurnAreaQueueService = trurnAreaQueueService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TurnAreaQueueDTO> create(@RequestBody TurnAreaQueueDTO dto) {
        return ResponseEntity.ok(trurnAreaQueueService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        trurnAreaQueueService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TurnAreaQueueDTO> update(@RequestBody TurnAreaQueueDTO dto) {
        return ResponseEntity.ok(trurnAreaQueueService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TurnAreaQueueDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(trurnAreaQueueService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TurnAreaQueueDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(trurnAreaQueueService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<TurnAreaQueueDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(trurnAreaQueueService.getBySearchTerm(search, pageable));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TurnAreaQueueDTO>> getAll() {
        return ResponseEntity.ok(trurnAreaQueueService.getAll());
    }
}
