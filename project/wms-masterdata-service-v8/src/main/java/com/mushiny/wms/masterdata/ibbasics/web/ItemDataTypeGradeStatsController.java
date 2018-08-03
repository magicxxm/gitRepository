package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.ItemDataTypeGradeStatsDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.TimeConfigDTO;
import com.mushiny.wms.masterdata.ibbasics.service.ItemDataTypeGradeStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/inbound/item-data-type-grade-stats")
public class ItemDataTypeGradeStatsController {

    private final ItemDataTypeGradeStatsService itemDataTypeGradeStatsService;

    @Autowired
    public ItemDataTypeGradeStatsController(ItemDataTypeGradeStatsService itemDataTypeGradeStatsService) {
        this.itemDataTypeGradeStatsService = itemDataTypeGradeStatsService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataTypeGradeStatsDTO> create(@RequestBody ItemDataTypeGradeStatsDTO dto) {
        return ResponseEntity.ok(itemDataTypeGradeStatsService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        itemDataTypeGradeStatsService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataTypeGradeStatsDTO> update(@RequestBody ItemDataTypeGradeStatsDTO dto) {
        return ResponseEntity.ok(itemDataTypeGradeStatsService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataTypeGradeStatsDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(itemDataTypeGradeStatsService.retrieve(id));
    }

    @RequestMapping(value = "/refreshTime",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeConfigDTO> getRefreshTime() {
        return ResponseEntity.ok(itemDataTypeGradeStatsService.getRefreshTime());
    }

    @RequestMapping(value = "/saveDayNumber/{dayNumber}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeConfigDTO> saveDayNumber(@PathVariable int dayNumber) {
        return ResponseEntity.ok(itemDataTypeGradeStatsService.saveDayNumber(dayNumber));
    }

    @RequestMapping(value = "/saveRefreshTime/{refreshTime}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeConfigDTO> saveRefreshTime(@PathVariable String refreshTime) {
        return ResponseEntity.ok(itemDataTypeGradeStatsService.saveRefreshTime(refreshTime));
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDataTypeGradeStatsDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(itemDataTypeGradeStatsService.getByClientId(clientId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDataTypeGradeStatsDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(itemDataTypeGradeStatsService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ItemDataTypeGradeStatsDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(itemDataTypeGradeStatsService.getBySearchTerm(search, pageable));
    }
}
