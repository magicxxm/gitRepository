package com.mushiny.wms.internaltool.web;

import com.mushiny.wms.internaltool.service.MeasureGoodsService;
import com.mushiny.wms.internaltool.web.dto.ItemDataAmountDTO;
import com.mushiny.wms.internaltool.web.dto.MeasureGoodsDTO;
import com.mushiny.wms.internaltool.web.dto.StorageLocationAmountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal-tool/measure-goods")
public class MeasureGoodsToolController {

    private final MeasureGoodsService measureGoodsService;

    @Autowired
    public MeasureGoodsToolController(MeasureGoodsService measureGoodsService) {
        this.measureGoodsService = measureGoodsService;
    }

    @RequestMapping(value = "/scanning/source",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageLocationAmountDTO> scanningSource(@RequestParam String sourceName) {
        return ResponseEntity.ok(measureGoodsService.scanningSource(sourceName));
    }

    @RequestMapping(value = "/scanning/sku",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataAmountDTO> scanningItemData(@RequestParam String sourceId,
                                                              @RequestParam String sku) {
        return ResponseEntity.ok(measureGoodsService.scanningItemData(sourceId, sku));
    }

    @RequestMapping(value = "/scanning/destination",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageLocationAmountDTO> scanningDestination(@RequestParam String sourceId,
                                                                        @RequestParam String itemDataId,
                                                                        @RequestParam String destinationName) {
        return ResponseEntity.ok(measureGoodsService.scanningDestination(sourceId, itemDataId, destinationName));
    }

    @RequestMapping(value = "/measuring",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> measuring(@RequestBody MeasureGoodsDTO measureGoodsDTO) {
        measureGoodsService.measuring(measureGoodsDTO);
        return ResponseEntity.ok().build();
    }
}
