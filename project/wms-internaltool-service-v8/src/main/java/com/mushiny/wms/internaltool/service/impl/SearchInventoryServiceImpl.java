package com.mushiny.wms.internaltool.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.ItemDataGlobal;
import com.mushiny.wms.internaltool.common.domain.StockUnitRecord;
import com.mushiny.wms.internaltool.common.repository.ItemDataGlobalRepository;
import com.mushiny.wms.internaltool.common.repository.ItemDataRepository;
import com.mushiny.wms.internaltool.exception.InternalToolException;
import com.mushiny.wms.internaltool.query.*;
import com.mushiny.wms.internaltool.service.SearchInventoryService;
import com.mushiny.wms.internaltool.web.dto.ItemInventoryRecordDTO;
import com.mushiny.wms.internaltool.web.dto.ItemPurchasingRecordDTO;
import com.mushiny.wms.internaltool.web.dto.StorageInventoryRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SearchInventoryServiceImpl implements SearchInventoryService {

    private final StorageRecordsQuery storageRecordsQuery;
    private final ItemRecordsQuery itemRecordsQuery;
    private final HistoricalRecordsQuery historicalRecordsQuery;
    private final ItemPurchasingRecordsQuery itemPurchasingRecordsQuery;
    private final ItemAdjustRecordsQuery itemAdjustRecordsQuery;
    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final ItemDataRepository itemDataRepository;
    private final ApplicationContext applicationContext;

    @Autowired
    public SearchInventoryServiceImpl(StorageRecordsQuery storageRecordsQuery,
                                      ItemRecordsQuery itemRecordsQuery,
                                      HistoricalRecordsQuery historicalRecordsQuery,
                                      ItemPurchasingRecordsQuery itemPurchasingRecordsQuery,
                                      ItemAdjustRecordsQuery itemAdjustRecordsQuery,
                                      ItemDataRepository itemDataRepository,
                                      ItemDataGlobalRepository itemDataGlobalRepository,
                                      ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.storageRecordsQuery = storageRecordsQuery;
        this.itemRecordsQuery = itemRecordsQuery;
        this.historicalRecordsQuery = historicalRecordsQuery;
        this.itemPurchasingRecordsQuery = itemPurchasingRecordsQuery;
        this.itemAdjustRecordsQuery = itemAdjustRecordsQuery;
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.itemDataRepository = itemDataRepository;
    }

    @Override
    public List<StorageInventoryRecordDTO> getStorageRecords(String storageLocationName) {
        return storageRecordsQuery.getStorageRecords(storageLocationName);
    }

    @Override
    public Page<StockUnitRecord> getStorageHistoricalRecordsBySearchTerm(String storageLocation,
                                                                         String sku,
                                                                         String createdDate,
                                                                         String username,
                                                                         String fromStorageLocation,
                                                                         String toStorageLocation,
                                                                         String recordCode,
                                                                         String recordTool,
                                                                         String recordType,
                                                                         Pageable pageable) {
        return historicalRecordsQuery.getHistoricalRecords(storageLocation, sku, createdDate, username,
                fromStorageLocation, toStorageLocation, recordCode, recordTool, recordType, pageable);
    }

    @Override
    public ItemData getBySKU(String sku) {
        //查询商品需要按库存来查
        /*List<ItemDataGlobal> itemDataGlobals = itemDataGlobalRepository.getBySKU(sku);
        if (itemDataGlobals.isEmpty()) {
            throw new ApiException(InternalToolException.EX_IT_SKU_NOT_FOUND.toString(), "SKU："+sku+" 在库存中不存在！");
        }
        if (itemDataGlobals.size() > 1) {
            throw new ApiException(InternalToolException.EX_IT_SKU_HAS_MORE_ITEM_DATA.toString(), "SKU："+sku+" 是多条码商品！");
        }*/
        String warehouseId = applicationContext.getCurrentWarehouse();
        List<ItemData> itemDataList = itemDataRepository.getBysku(sku,warehouseId);
        if(itemDataList.isEmpty()){
            throw new ApiException(InternalToolException.EX_IT_SKU_NOT_FOUND.getName(), "SKU："+sku+" 条码扫描错误！");
        }
        if(itemDataList.size() > 1){
            throw new ApiException(InternalToolException.EX_IT_SKU_HAS_MORE_ITEM_DATA.getName(), "SKU："+sku+" 是多条码商品！");
        }
        return itemDataList.get(0);
    }

    @Override
    public List<ItemInventoryRecordDTO> getItemRecords(String sku) {
        String warehouseId = applicationContext.getCurrentWarehouse();
        return itemRecordsQuery.getItemRecords(sku,warehouseId);
    }

    @Override
    public Page<StockUnitRecord> getItemHistoricalRecordsBySearchTerm(String sku,
                                                                      String createdDate,
                                                                      String username,
                                                                      String fromStorageLocation,
                                                                      String toStorageLocation,
                                                                      String recordCode,
                                                                      String recordTool,
                                                                      String recordType,
                                                                      Pageable pageable) {
        return historicalRecordsQuery.getHistoricalRecords(null, sku, createdDate, username,
                fromStorageLocation, toStorageLocation, recordCode, recordTool, recordType, pageable);
    }

    @Override
    public List<ItemPurchasingRecordDTO> getItemPurchasingRecords(String sku) {
        return itemPurchasingRecordsQuery.getItemPurchasingRecords(sku);
    }

    @Override
    public Page<StockUnitRecord> getItemAdjustRecords(String sku,
                                                      String createdDate,
                                                      String username,
                                                      String fromStorageLocation,
                                                      String toStorageLocation,
                                                      String recordCode,
                                                      String recordTool,
                                                      String recordType,
                                                      Pageable pageable) {
        return itemAdjustRecordsQuery.getItemAdjustRecords(sku, createdDate, username, fromStorageLocation,
                toStorageLocation, recordCode, recordTool, recordType, pageable);
    }
}
