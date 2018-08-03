package com.mushiny.wms.internaltool.service;

import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.ItemDataGlobal;
import com.mushiny.wms.internaltool.common.domain.StockUnitRecord;
import com.mushiny.wms.internaltool.web.dto.ItemInventoryRecordDTO;
import com.mushiny.wms.internaltool.web.dto.ItemPurchasingRecordDTO;
import com.mushiny.wms.internaltool.web.dto.StorageInventoryRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchInventoryService {

    List<StorageInventoryRecordDTO> getStorageRecords(String storageLocationName);

    Page<StockUnitRecord> getStorageHistoricalRecordsBySearchTerm(String storageLocation,
                                                                  String sku,
                                                                  String createdDate,
                                                                  String username,
                                                                  String fromStorageLocation,
                                                                  String toStorageLocation,
                                                                  String recordCode,
                                                                  String recordTool,
                                                                  String recordType,
                                                                  Pageable pageable);

    ItemData getBySKU(String sku);
//    ItemDataGlobal getBySKU(String sku);


    List<ItemInventoryRecordDTO> getItemRecords(String sku);

    Page<StockUnitRecord> getItemHistoricalRecordsBySearchTerm(String sku,
                                                               String createdDate,
                                                               String username,
                                                               String fromStorageLocation,
                                                               String toStorageLocation,
                                                               String recordCode,
                                                               String recordTool,
                                                               String recordType,
                                                               Pageable pageable);

    List<ItemPurchasingRecordDTO> getItemPurchasingRecords(String sku);

    Page<StockUnitRecord> getItemAdjustRecords(String sku,
                                               String createdDate,
                                               String username,
                                               String fromStorageLocation,
                                               String toStorageLocation,
                                               String recordCode,
                                               String recordTool,
                                               String recordType,
                                               Pageable pageable);
}
