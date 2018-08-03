package com.mushiny.wms.internaltool.web;

import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.ItemDataGlobal;
import com.mushiny.wms.internaltool.common.domain.StockUnitRecord;
import com.mushiny.wms.internaltool.service.SearchInventoryService;
import com.mushiny.wms.internaltool.web.dto.ItemInventoryRecordDTO;
import com.mushiny.wms.internaltool.web.dto.ItemPurchasingRecordDTO;
import com.mushiny.wms.internaltool.web.dto.StorageInventoryRecordDTO;
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

@RestController
@RequestMapping("/internal-tool/search-inventory")
public class SearchInventoryToolController {

    private final SearchInventoryService searchInventoryService;

    @Autowired
    public SearchInventoryToolController(SearchInventoryService searchInventoryService) {
        this.searchInventoryService = searchInventoryService;
    }

    //容器查询-库存记录
    @RequestMapping(value = "/storage-records",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StorageInventoryRecordDTO>> getStorageRecords(
            @RequestParam String storageLocationName) {
        return ResponseEntity.ok(searchInventoryService.getStorageRecords(storageLocationName));
    }

    //容器查询-历史记录
    @RequestMapping(value = "/storage-historical-records",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<StockUnitRecord>> getStorageHistoricalRecordsBySearchTerm(
            @RequestParam String storageLocation,
            @RequestParam(required = false) String itemNo,
            @RequestParam(required = false) String createdDate,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String fromStorageLocation,
            @RequestParam(required = false) String toStorageLocation,
            @RequestParam(required = false) String recordCode,
            @RequestParam(required = false) String recordTool,
            @RequestParam(required = false) String recordType,
            Pageable pageable) {
        return ResponseEntity.ok(searchInventoryService.getStorageHistoricalRecordsBySearchTerm(
                storageLocation, itemNo, createdDate, username, fromStorageLocation,
                toStorageLocation, recordCode, recordTool, recordType, pageable));
    }

    //商品信息查询
    @RequestMapping(value = "/item-data-global/sku",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemData> getByItemDataNo(@RequestParam String sku) {
        return ResponseEntity.ok(searchInventoryService.getBySKU(sku));
    }
    /*@RequestMapping(value = "/item-data-global/sku",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataGlobal> getByItemDataNo(@RequestParam String sku) {
        return ResponseEntity.ok(searchInventoryService.getBySKU(sku));
    }*/

    //查询商品库存记录
    @RequestMapping(value = "/item-records",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemInventoryRecordDTO>> getItemRecords(@RequestParam String sku) {
        return ResponseEntity.ok(searchInventoryService.getItemRecords(sku));
    }

    @RequestMapping(value = "/item-historical-records",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<StockUnitRecord>> getItemHistoricalRecordsBySearchTerm(
            @RequestParam String itemNo,
            @RequestParam(required = false) String createdDate,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String fromStorageLocation,
            @RequestParam(required = false) String toStorageLocation,
            @RequestParam(required = false) String recordCode,
            @RequestParam(required = false) String recordTool,
            @RequestParam(required = false) String recordType,
            Pageable pageable) {
        return ResponseEntity.ok(searchInventoryService.getItemHistoricalRecordsBySearchTerm(
                itemNo, createdDate, username, fromStorageLocation, toStorageLocation,
                recordCode, recordTool, recordType, pageable));
    }

    @RequestMapping(value = "/item-purchasing-records",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemPurchasingRecordDTO>> getItemPurchasingRecords(@RequestParam String sku) {
        return ResponseEntity.ok(searchInventoryService.getItemPurchasingRecords(sku));
    }

    @RequestMapping(value = "/item-adjust-records",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<StockUnitRecord>> getItemAdjustRecords(
            @RequestParam String sku,
            @RequestParam(required = false) String createdDate,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String fromStorageLocation,
            @RequestParam(required = false) String toStorageLocation,
            @RequestParam(required = false) String recordCode,
            @RequestParam(required = false) String recordTool,
            @RequestParam(required = false) String recordType,
            Pageable pageable) {
        return ResponseEntity.ok(searchInventoryService.getItemAdjustRecords(
                sku, createdDate, username, fromStorageLocation, toStorageLocation,
                recordCode, recordTool, recordType, pageable));
    }
}
