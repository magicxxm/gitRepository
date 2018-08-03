package com.mushiny.wms.internaltool.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.internaltool.business.*;
import com.mushiny.wms.internaltool.common.domain.*;
import com.mushiny.wms.internaltool.common.enums.StockUnitRecordState;
import com.mushiny.wms.internaltool.common.repository.ItemDataGlobalRepository;
import com.mushiny.wms.internaltool.common.repository.ItemDataRepository;
import com.mushiny.wms.internaltool.common.repository.StockUnitRepository;
import com.mushiny.wms.internaltool.common.repository.StorageLocationRepository;
import com.mushiny.wms.internaltool.exception.InternalToolException;
import com.mushiny.wms.internaltool.service.AdjustInventoryService;
import com.mushiny.wms.internaltool.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdjustInventoryServiceImpl implements AdjustInventoryService {


    private final StorageLocationRepository storageLocationRepository;
    private final ItemDataRepository itemDataRepository;
    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final StockUnitRepository stockUnitRepository;
    private final CheckingBusiness checkingBusiness;
    private final ScanningBusiness scanningBusiness;
    private final MoveGoodsBusiness moveGoodsBusiness;
    private final UnitLoadBusiness unitLoadBusiness;
    private final InventoryAttributesBusiness inventoryAttributesBusiness;
    private final InventoryGoodsBusiness inventoryGoodsBusiness;
    private final ApplicationContext applicationContext;

    @Autowired
    public AdjustInventoryServiceImpl(ItemDataRepository itemDataRepository,
                                      StorageLocationRepository storageLocationRepository,
                                      CheckingBusiness checkingBusiness,
                                      ScanningBusiness scanningBusiness,
                                      MoveGoodsBusiness moveGoodsBusiness,
                                      InventoryAttributesBusiness inventoryAttributesBusiness,
                                      InventoryGoodsBusiness inventoryGoodsBusiness,
                                      ItemDataGlobalRepository itemDataGlobalRepository,
                                      StockUnitRepository stockUnitRepository,
                                      ApplicationContext applicationContext,
                                      UnitLoadBusiness unitLoadBusiness) {
        this.itemDataRepository = itemDataRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.applicationContext = applicationContext;
        this.checkingBusiness = checkingBusiness;
        this.scanningBusiness = scanningBusiness;
        this.moveGoodsBusiness = moveGoodsBusiness;
        this.inventoryAttributesBusiness = inventoryAttributesBusiness;
        this.inventoryGoodsBusiness = inventoryGoodsBusiness;
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.unitLoadBusiness = unitLoadBusiness;
    }

    @Override
    public StorageLocationAmountDTO scanningSource(String sourceName) {
        return scanningBusiness.getStorageLocationAmount(sourceName);
    }

    @Override
    public List<StockUnit> getStorageLocationItemData(String storageLocationId) {
        StorageLocation storageLocation = storageLocationRepository.retrieve(storageLocationId);
        UnitLoad unitLoad = unitLoadBusiness.getByStorageLocation(storageLocation);
        List<StockUnit> items = new ArrayList<>();
        if (unitLoad != null) {
            List<StockUnit> stockUnits = stockUnitRepository.getByUnitLoad(unitLoad);
            for (StockUnit stockUnit : stockUnits) {
                if (stockUnit.getAmount().compareTo(BigDecimal.ZERO) == 1) {
                    items.add(stockUnit);
                }
            }
        }
        return items;
    }

    @Override
    public ItemDataAmountDTO scanningItemData(String sourceId, String sku) {
        return scanningBusiness.getItemDataAmounts(sourceId, sku);
    }

    @Override
    public ItemDataGlobalAmountDTO scanningItemDataGlobal(String sourceId, String sku) {
        /*List<ItemDataGlobal> itemDataGlobals = itemDataGlobalRepository.getBySKU(sku);
        if (itemDataGlobals.isEmpty()) {
            throw new ApiException(InternalToolException.EX_IT_SKU_NOT_FOUND.toString(), sku);
        }
        if (itemDataGlobals.size() > 1) {
            throw new ApiException(InternalToolException.EX_IT_SKU_HAS_MORE_ITEM_DATA.toString(), sku);
        }*/
        List<ItemData> itemDataList = itemDataRepository.getBysku(sku,applicationContext.getCurrentWarehouse());
        if(itemDataList.isEmpty()){
            throw new ApiException(InternalToolException.EX_IT_SKU_NOT_FOUND.getName(), sku);
        }
        if(itemDataList.size() > 1){
            throw new ApiException(InternalToolException.EX_IT_SKU_HAS_MORE_ITEM_DATA.getName(), sku);
        }
        ItemDataGlobalAmountDTO dto = new ItemDataGlobalAmountDTO();
        dto.setItemData(itemDataList.get(0));
        dto.setAmount(scanningBusiness.getItemDataGlobalAmount(sourceId, sku));
        return dto;
    }

    @Override
    public StorageLocationAmountDTO scanningDestination(String sourceId,
                                                        String itemDataId,
                                                        String destinationName) {
        return scanningBusiness.getStorageLocationAmount(sourceId, itemDataId, destinationName);
    }

    @Override
    public void checkUser(String username) {
        checkingBusiness.checkingUsername(username);
    }

    @Override
    public void updateInventoryAttributes(InventoryAttributesDTO inventoryAttributesDTO) {
        StorageLocation source = storageLocationRepository.retrieve(inventoryAttributesDTO.getSourceId());
        StorageLocation destination = storageLocationRepository.retrieve(inventoryAttributesDTO.getDestinationId());
        ItemData itemData = itemDataRepository.retrieve(inventoryAttributesDTO.getItemDataId());
        inventoryAttributesBusiness.updateInventoryAttributes(
                source, destination, itemData,
                inventoryAttributesDTO.getInventoryState(),
                inventoryAttributesDTO.getAmount(),
                inventoryAttributesDTO.getAdjustReason());

    }

    @Override
    public void moveAllGoods(MoveGoodsDTO moveGoodsDTO) {
        StorageLocation source = storageLocationRepository.retrieve(moveGoodsDTO.getSourceId());
        StorageLocation destination = storageLocationRepository.retrieve(moveGoodsDTO.getDestinationId());
        moveGoodsBusiness.moveAll(source, destination,
                StockUnitRecordState.UPDATE_INVENTORY_STATE_RECORD_MOVE_CONTAINER_CODE.getName(),
                StockUnitRecordState.UPDATE_INVENTORY_STATE_RECORD_MOVE_CONTAINER_TOOL.getName(),
                StockUnitRecordState.UPDATE_INVENTORY_STATE_RECORD_MOVE_CONTAINER_TYPE.getName());
    }

    @Override
    public void moveGoods(MoveGoodsDTO moveGoodsDTO) {
        StorageLocation source = storageLocationRepository.retrieve(moveGoodsDTO.getSourceId());
        StorageLocation destination = storageLocationRepository.retrieve(moveGoodsDTO.getDestinationId());
        ItemData itemData = itemDataRepository.retrieve(moveGoodsDTO.getItemDataId());
        moveGoodsBusiness.moving(source, destination, itemData, moveGoodsDTO.getAmount(),
                StockUnitRecordState.UPDATE_INVENTORY_STATE_RECORD_MOVE_ITEM_CODE.getName(),
                StockUnitRecordState.UPDATE_INVENTORY_STATE_RECORD_MOVE_ITEM_TOOL.getName(),
                StockUnitRecordState.UPDATE_INVENTORY_STATE_RECORD_MOVE_ITEM_TYPE.getName());
    }

    @Override
    public void overageGoods(OverageGoodsDTO overageGoodsDTO) {
        inventoryGoodsBusiness.overageGoods(overageGoodsDTO,
                StockUnitRecordState.UPDATE_INVENTORY_STATE_RECORD_INVENTORY_OVERAGE_CODE.getName(),
                StockUnitRecordState.UPDATE_INVENTORY_STATE_RECORD_INVENTORY_OVERAGE_TOOL.getName(),
                StockUnitRecordState.UPDATE_INVENTORY_STATE_RECORD_INVENTORY_OVERAGE_TYPE.getName());
    }

    @Override
    public void lossGoods(LossGoodsDTO lossGoodsDTO) {
        inventoryGoodsBusiness.lossGoods(lossGoodsDTO,
                StockUnitRecordState.UPDATE_INVENTORY_STATE_RECORD_INVENTORY_LOSS_CODE.getName(),
                StockUnitRecordState.UPDATE_INVENTORY_STATE_RECORD_INVENTORY_LOSS_TOOL.getName(),
                StockUnitRecordState.UPDATE_INVENTORY_STATE_RECORD_INVENTORY_LOSS_TYPE.getName());
    }
}
