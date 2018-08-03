package com.mushiny.wms.internaltool.web;

import com.mushiny.wms.internaltool.common.domain.StockUnit;
import com.mushiny.wms.internaltool.service.AdjustInventoryService;
import com.mushiny.wms.internaltool.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal-tool/adjust-inventory")
public class AdjustInventoryToolController {

    private final AdjustInventoryService adjustInventoryService;

    @Autowired
    public AdjustInventoryToolController(AdjustInventoryService adjustInventoryService) {
        this.adjustInventoryService = adjustInventoryService;
    }

    @RequestMapping(value = "/scanning/source",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageLocationAmountDTO> scanningSource(@RequestParam String sourceName) {
        return ResponseEntity.ok(adjustInventoryService.scanningSource(sourceName));
    }

    @RequestMapping(value = "/scanning/sku",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataAmountDTO> scanningItemData(@RequestParam String sourceId,
                                                              @RequestParam String sku) {
        return ResponseEntity.ok(adjustInventoryService.scanningItemData(sourceId, sku));
    }

    @RequestMapping(value = "/scanning/global-sku",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataGlobalAmountDTO> scanningItemDataGlobal(@RequestParam String sourceId,
                                                                          @RequestParam String sku) {
        return ResponseEntity.ok(adjustInventoryService.scanningItemDataGlobal(sourceId, sku));
    }

    @RequestMapping(value = "/scanning/destination",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageLocationAmountDTO> scanningDestination(@RequestParam String sourceId,
                                                                        @RequestParam String itemDataId,
                                                                        @RequestParam String destinationName) {
        return ResponseEntity.ok(adjustInventoryService.scanningDestination(sourceId, itemDataId, destinationName));
    }

    @RequestMapping(value = "/storage-location/items",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockUnit>> getStorageLocationItemData(@RequestParam String storageLocationId) {
        return ResponseEntity.ok(adjustInventoryService.getStorageLocationItemData(storageLocationId));
    }

    @RequestMapping(value = "/checking/username",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> checkUser(@RequestParam String username) {
        adjustInventoryService.checkUser(username);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/inventory/attributes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateInventoryAttributes(@RequestBody InventoryAttributesDTO inventoryAttributesDTO) {
        adjustInventoryService.updateInventoryAttributes(inventoryAttributesDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/moving/goods/all",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> moveAllGoods(@RequestBody MoveGoodsDTO moveGoodsDTO) {
        adjustInventoryService.moveAllGoods(moveGoodsDTO);
        return ResponseEntity.ok().build();

    }

    @RequestMapping(value = "/moving/goods",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> moveGoods(@RequestBody MoveGoodsDTO moveGoodsDTO) {
        adjustInventoryService.moveGoods(moveGoodsDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/overage-goods",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> overageGoods(@RequestBody OverageGoodsDTO overageGoodsDTO) {
        adjustInventoryService.overageGoods(overageGoodsDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/loss-goods",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> lossGoods(@RequestBody LossGoodsDTO lossGoodsDTO) {
        adjustInventoryService.lossGoods(lossGoodsDTO);
        return ResponseEntity.ok().build();
    }
}
