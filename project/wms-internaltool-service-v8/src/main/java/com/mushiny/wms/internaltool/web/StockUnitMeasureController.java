package com.mushiny.wms.internaltool.web;

import com.mushiny.wms.internaltool.service.StockUnitMeasureService;
import com.mushiny.wms.internaltool.web.dto.ItemDataDTO;
import com.mushiny.wms.internaltool.web.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 123 on 2017/11/8.
 */
@RestController
@RequestMapping("/internal-tool")
public class StockUnitMeasureController {

    private final StockUnitMeasureService stockUnitMeasureService;

    @Autowired
    public StockUnitMeasureController(StockUnitMeasureService stockUnitMeasureService){
        this.stockUnitMeasureService = stockUnitMeasureService;
    }

    //暂不用
    @RequestMapping(value = "/stockUnit-measures-all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockDTO>> getBySearchTerm() {
        return ResponseEntity.ok(stockUnitMeasureService.getAllStockUnit());
    }

    @RequestMapping(value = "/stockUnit-measures",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<StockDTO>> getBySearchTerm(Pageable pageable) {
        return ResponseEntity.ok(stockUnitMeasureService.getStockUnit(pageable));
    }

    @RequestMapping(value = "/findItem",
            method = RequestMethod.GET,
            params = {"itemNo","clientName"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataDTO> getItem(@RequestParam String itemNo,@RequestParam String clientName) {
        return ResponseEntity.ok(stockUnitMeasureService.getItem(itemNo,clientName));
    }

    @RequestMapping(value = "/searchStockUnit",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockDTO>> searchStockUnit(@RequestParam String param) {
        return ResponseEntity.ok(stockUnitMeasureService.searchStockUnit(param));
    }

    @RequestMapping(value = "/export-stockUnit-measures",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockDTO>> exportStockUnitMeasure() {
        return ResponseEntity.ok(stockUnitMeasureService.exportStockUnit());
    }
}
