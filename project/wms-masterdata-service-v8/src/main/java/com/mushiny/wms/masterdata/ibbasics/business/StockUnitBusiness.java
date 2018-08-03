package com.mushiny.wms.masterdata.ibbasics.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.ibbasics.domain.StockUnit;
import com.mushiny.wms.masterdata.ibbasics.repository.StockUnitRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StockUnitBusiness {

    private final StockUnitRepository stockUnitRepository;
    private final ApplicationContext applicationContext;

    @Autowired
    public StockUnitBusiness(StockUnitRepository stockUnitRepository,
                             ApplicationContext applicationContext) {
        this.stockUnitRepository = stockUnitRepository;
        this.applicationContext = applicationContext;
    }

    public StockUnit subtractStowingAmount(StockUnit stockUnit, BigDecimal amount) {
        // 减去相应的数量
        stockUnit.setAmount(stockUnit.getAmount().subtract(amount));
        return stockUnit;
    }

//    public void checkItemClient(Container container, ItemData itemData) {
//        // 容器中不能存放不同客户的相同商品
//        List<StockUnit> stockUnits = stockUnitRepository.getByContainer(container);
//        if (stockUnits != null && !stockUnits.isEmpty()) {
//            for (StockUnit stockUnit : stockUnits) {
//                ItemData receivingItemData = stockUnit.getItemData();
//                if (!(itemData.getClient().getId().equalsIgnoreCase(receivingItemData.getClient().getId()))
//                        && itemData.getItemNo().equalsIgnoreCase(receivingItemData.getItemNo())) {
//                    throw new ApiException(InBoundException
//                            .EX_CONTAINER_SKU_DIFFERENT_CLIENT.toString(), container.getName());
//                }
//            }
//        }
//    }

//    public void checkItemLot(Container container, ItemData itemData, LocalDate useNotAfter) {
//        if (itemData.isLotMandatory()) {
//            // 容器中不能存放同一商品的不同有效期
//            StockUnit stockUnit = stockUnitRepository.getByContainerAndItemData(container, itemData);
//            if (stockUnit != null) {
//                if (!stockUnit.getLot().getUseNotAfter().equals(useNotAfter)) {
//                    throw new ApiException(InBoundException
//                            .EX_CONTAINER_SKU_DIFFERENT_LOT.toString(), container.getName());
//                }
//            }
//        }
//    }
//
//    public void checkItemLot(Container container, ItemData itemData, Lot lot) {
//        if (itemData.isLotMandatory()) {
//            // 小车中不能存放同一商品的不同有效期
//            StockUnit stockUnit = stockUnitRepository.getByContainerAndItemData(container, itemData);
//            if (stockUnit != null) {
//                if (!stockUnit.getLot().getId().equalsIgnoreCase(lot.getId())) {
//                    throw new ApiException(InBoundException
//                            .EX_CONTAINER_SKU_DIFFERENT_LOT.toString(), container.getName());
//                }
//            }
//        }
//    }
//
//    public void checkItemLot(StockUnit stockUnit) {
//        if (stockUnit.getItemData().isLotMandatory()) {
//            // 检查SKU的有效期是否录入
//            if (stockUnit.getLot() == null) {
//                throw new ApiException(InBoundException
//                        .EX_LOT_ITEM_DATA_NOT_FOUND_DATE.toString(), stockUnit.getItemData().getItemNo());
//            }
//        }
//    }
//
//    public void checkCubiScanItemData(Container container, ItemData itemData) {
//        // 测量容只容许放入该SKU一件商品就好
//        StockUnit stockUnit = stockUnitRepository.getByContainerAndItemNo(container, itemData.getItemNo());
//        if (stockUnit != null) {
//            throw new ApiException(InBoundException
//                    .EX_CUBI_SCAN_CONTAINER_HAS_THIS_SKU.toString(), container.getName(), itemData.getItemNo());
//        }
//    }
//
//    public void checkContainerItemsAmount(Container container, ItemData itemData, BigDecimal amount) {
//        // 商品数量超过小车收货商品的总数量
//        BigDecimal receivingMaxAmount = stockUnitRepository.sumByContainerAndItemData(container, itemData);
//        if (amount.compareTo(receivingMaxAmount) == 1) {
//            throw new ApiException(InBoundException
//                    .EX_AMOUNT_ERROR.toString(), container.getName());
//        }
//    }

//    public void checkStorageLocationItemsAmount(StorageLocation storageLocation, ItemData itemData) {
//        // 商品种类超过系统设置数量
//        StockUnit stockUnit = stockUnitRepository.getByStorageLocationAndItemData(storageLocation, itemData);
//        if (stockUnit == null) {
//            long itemDataAmount = stockUnitRepository.countByStorageLocation(storageLocation) + 1;
//            if (itemDataAmount > storageLocation.getStorageLocationType().getMaxItemDataAmount()) {
//                throw new ApiException(InBoundException.
//                        EX_STORAGE_LOCATION_SKU_ITEMS_MAX_AMOUNT.toString(), storageLocation.getName());
//            }
//        }
//    }
//
//    public void checkStorageLocationLot(StorageLocation storageLocation, ItemData itemData, Lot lot) {
//        if(itemData.isLotMandatory()) {
//            // 相同商品不同有效期商品
//            StockUnit stockUnit = stockUnitRepository.getByStorageLocationAndItemData(storageLocation, itemData);
//            if (stockUnit != null) {
//                if (!stockUnit.getLot().getId().equalsIgnoreCase(lot.getId())) {
//                    throw new ApiException(InBoundException
//                            .EX_STORAGE_LOCATION_SKU_DIFFERENT_LOT.toString(), storageLocation.getName());
//                }
//            }
//        }
//    }
//
//    public void checkStorageLocationClient(StorageLocation storageLocation, ItemData itemData) {
//        // 货位所属客户和商品所属客户比较
//        if (!storageLocation.getClient().isSystemClient()
//                && storageLocation.getClient().getId().equalsIgnoreCase(itemData.getClient().getId())) {
//            throw new ApiException(InBoundException
//                    .EX_STORAGE_LOCATION_SKU_CLIENT_DIFFERENT.toString(), storageLocation.getName());
//        }
//        // 不同客户的相同商品是不容许放入同一个货位中
//        StockUnit stockUnit = stockUnitRepository.getByItemNoAndUnequalClient(
//                storageLocation, itemData.getClient(), itemData.getItemNo());
//        if (stockUnit != null ) {
//            throw new ApiException(InBoundException
//                    .EX_STORAGE_LOCATION_SKU_DIFFERENT_CLIENT.toString(), storageLocation.getName());
//        }
//    }
//
//    public void checkStorageLocationItemGroup(StorageLocation storageLocation, ItemData itemData) {
//        // 商品属性与货位设置不符
//        Set<ItemGroup> itemGroups = storageLocation.getZone().getItemGroups();
//        boolean useItemGroupFlag = false;
//        for (ItemGroup itemGroup : itemGroups) {
//            if (itemGroup.getId().equalsIgnoreCase(itemData.getItemGroup().getId())) {
//                useItemGroupFlag = true;
//            }
//        }
//        if (!useItemGroupFlag) {
//            throw new ApiException(InBoundException
//                    .EX_STORAGE_LOCATION_SKU_DIFFERENT_ITEM_GROUP.toString(), storageLocation.getName());
//        }
//    }
//
//    public StockUnit getContainerStockUnit(Container container, ItemData itemData, String itemNo) {
//        // 同一车中只容许出现唯一的SKU商品
//        StockUnit stockUnit;
//        if (itemData == null) {
//            stockUnit = stockUnitRepository.getByContainerAndItemNo(container, itemNo);
//        } else {
//            stockUnit = stockUnitRepository.getByContainerAndItemData(container, itemData);
//        }
//        // 检查SKU是否有效
//        if (stockUnit == null) {
//            throw new ApiException(
//                    InBoundException.EX_CONTAINER_SKU_NOT_FOUND.toString(), itemNo);
//        }
//        return stockUnit;
//    }
//
//    public StockUnit buildStockUnit(StockUnit source,
//                                    BigDecimal amount) {
//        StockUnit stockUnit = new StockUnit();
//        stockUnit.setAmount(amount);
//        stockUnit.setStrategyDate(DateTimeUtil.getNowDate());
//        stockUnit.setItemData(source.getItemData());
//        stockUnit.setLot(source.getLot());
//        stockUnit.setClient(source.getClient());
//        stockUnit.setWarehouse(source.getWarehouse());
//        return stockUnit;
//    }

    public StockUnit buildStockUnit(Warehouse warehouse,
                                    Client client,
                                    ItemData itemData,
//                                    Lot lot,
                                    BigDecimal amount) {
        StockUnit stockUnit = new StockUnit();
        stockUnit.setAmount(amount);
//        stockUnit.setStrategyDate(DateTimeUtil.getNowDate());
        stockUnit.setItemData(itemData);
//        stockUnit.setLot(lot);
        stockUnit.setClientId(applicationContext.getCurrentClient());
        stockUnit.setWarehouseId(applicationContext.getCurrentWarehouse());
        return stockUnit;
    }

    /**
    public StockUnitRecord buildStockUnitRecord(StockUnit stockUnit, String recordType,BigDecimal amount) {
        StockUnitRecord stockUnitRecord = new StockUnitRecord();
        stockUnitRecord.setAmount(amount);
        stockUnitRecord.setItemData(stockUnit.getItemData().getItemNo());
//        if (stockUnit.getLot() != null) {
//            stockUnitRecord.setLot(stockUnit.getLot().getLotNo());
//        }
        stockUnitRecord.setScale(0);
        stockUnitRecord.setOperator(applicationContext.getCurrentUser());
        stockUnitRecord.setRecordType(recordType);
        stockUnitRecord.setToStockUnit(stockUnit.getId());
//        if (stockUnit.getStorageLocation() != null) {
//            stockUnitRecord.setToStorageLocation(stockUnit.getStorageLocation().getName());
//        }
//        if (stockUnit.getContainer() != null) {
//            stockUnitRecord.setToContainer(stockUnit.getContainer().getName());
//        }
        if (stockUnit.getUnitLoad() != null) {
            stockUnitRecord.setToUnitLoad(stockUnit.getUnitLoad().getLabel());
        }
        stockUnitRecord.setClientId(stockUnit.getClientId());
        stockUnitRecord.setWarehouseId(stockUnit.getWarehouseId());
        return stockUnitRecord;
    }
     */
}
