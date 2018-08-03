package com.mushiny.wms.internaltool.web;

import com.mushiny.wms.internaltool.service.MoveGoodsService;
import com.mushiny.wms.internaltool.web.dto.ItemDataAmountDTO;
import com.mushiny.wms.internaltool.web.dto.MoveGoodsDTO;
import com.mushiny.wms.internaltool.web.dto.StorageLocationAmountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal-tool/move-goods")
public class MoveGoodsToolController {

    private final MoveGoodsService moveGoodsService;

    @Autowired
    public MoveGoodsToolController(MoveGoodsService moveGoodsService) {
        this.moveGoodsService = moveGoodsService;
    }

    @RequestMapping(value = "/scanning/source",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageLocationAmountDTO> scanningSource(@RequestParam String sourceName) {
        return ResponseEntity.ok(moveGoodsService.scanningSource(sourceName));
    }

    @RequestMapping(value = "/scanning/sku",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataAmountDTO> scanningItemData(@RequestParam String sourceId,
                                                              @RequestParam String sku) {
        return ResponseEntity.ok(moveGoodsService.scanningItemData(sourceId, sku));
    }

    @RequestMapping(value = "/scanning/destination",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageLocationAmountDTO> scanningDestination(@RequestParam String sourceId,
                                                                        @RequestParam String itemDataId,
                                                                        @RequestParam String destinationName) {
        return ResponseEntity.ok(moveGoodsService.scanningDestination(sourceId, itemDataId, destinationName));
    }

    @RequestMapping(value = "/moving",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> moving(@RequestBody MoveGoodsDTO moveGoodsDTO) {
        moveGoodsService.moving(moveGoodsDTO);
        return ResponseEntity.ok().build();
    }
}
