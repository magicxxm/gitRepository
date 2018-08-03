package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.StockTakingStationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowStationDTO;
import com.mushiny.wms.masterdata.ibbasics.service.StockTakingStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/icqa/stocktaking-stations")
public class StockTakingStationController {

    private final StockTakingStationService stockTakingStationService;

    @Autowired
    public StockTakingStationController(StockTakingStationService stockTakingStationService) {
        this.stockTakingStationService = stockTakingStationService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody StockTakingStationDTO dto) {
        stockTakingStationService.createMore(dto);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockTakingStationDTO> update(@RequestBody StockTakingStationDTO dto) {
        return ResponseEntity.ok(stockTakingStationService.update(dto));
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        stockTakingStationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockTakingStationDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(stockTakingStationService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockTakingStationDTO>> getAll() {
        return ResponseEntity.ok(stockTakingStationService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockTakingStationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(stockTakingStationService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<StockTakingStationDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(stockTakingStationService.getBySearchTerm(search, pageable));
    }
}
