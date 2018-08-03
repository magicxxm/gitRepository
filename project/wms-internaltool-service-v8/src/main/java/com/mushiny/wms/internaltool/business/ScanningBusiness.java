package com.mushiny.wms.internaltool.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.internaltool.common.domain.*;
import com.mushiny.wms.internaltool.common.repository.ItemDataRepository;
import com.mushiny.wms.internaltool.common.repository.StockUnitRepository;
import com.mushiny.wms.internaltool.common.repository.StorageLocationRepository;
import com.mushiny.wms.internaltool.exception.InternalToolException;
import com.mushiny.wms.internaltool.web.dto.ItemDataAmountDTO;
import com.mushiny.wms.internaltool.web.dto.StorageLocationAmountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ScanningBusiness {

    private final ApplicationContext applicationContext;
    private final StorageLocationRepository storageLocationRepository;
    private final UnitLoadBusiness unitLoadBusiness;
    private final StockUnitRepository stockUnitRepository;
    private final ItemDataRepository itemDataRepository;
    private final CheckingBusiness checkingBusiness;
    private final GeneralBusiness generalBusiness;

    @Autowired
    public ScanningBusiness(ItemDataRepository itemDataRepository,
                            ApplicationContext applicationContext,
                            StorageLocationRepository storageLocationRepository,
                            StockUnitRepository stockUnitRepository,
                            CheckingBusiness checkingBusiness,
                            GeneralBusiness generalBusiness,
                            UnitLoadBusiness unitLoadBusiness) {
        this.itemDataRepository = itemDataRepository;
        this.applicationContext = applicationContext;
        this.storageLocationRepository = storageLocationRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.checkingBusiness = checkingBusiness;
        this.generalBusiness = generalBusiness;
        this.unitLoadBusiness = unitLoadBusiness;
    }

    public StorageLocationAmountDTO getStorageLocationAmount(String sourceName) {
        // 获取容器并检查容器是否存在
        String warehouseId = applicationContext.getCurrentWarehouse();
        StorageLocation storageLocation = storageLocationRepository.getByName(warehouseId, sourceName);
        if (storageLocation == null) {
            throw new ApiException(InternalToolException.EX_IT_STORAGE_LOCATION_NOT_FOUND.getName(), sourceName);
        }
        StorageLocationAmountDTO dto = new StorageLocationAmountDTO(storageLocation);
        // 汇总容器中商品的总数量
        UnitLoad unitLoad = unitLoadBusiness.getByStorageLocation(storageLocation);
        if (unitLoad != null) {
            BigDecimal amount = generalBusiness.getStockUnitAmount(unitLoad);
            dto.setItemDataAmount(amount);
        }
        return dto;
    }

    public StorageLocationAmountDTO getStorageLocationAmount(String sourceId,
                                                             String itemDataId,
                                                             String destinationName) {
        StorageLocation source = storageLocationRepository.retrieve(sourceId);
        UnitLoad sourceUnitLoad = unitLoadBusiness.getByStorageLocation(source);
        ItemData itemData = itemDataRepository.retrieve(itemDataId);
        // 获取容器并检查容器是否存在
        String warehouseId = applicationContext.getCurrentWarehouse();
        StorageLocation destination = storageLocationRepository.getByName(warehouseId, destinationName);
        if (destination == null) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName(), destinationName);
        }
        StorageLocationAmountDTO dto = new StorageLocationAmountDTO(destination);
        // 汇总容器中商品的总数量
        UnitLoad destinationUnitLoad = unitLoadBusiness.getByStorageLocation(destination);
        if (destinationUnitLoad != null) {
            // 检查是否存在不同客户的相同商品
            checkingBusiness.checkStorageLocationClient(destinationUnitLoad, itemData);
            // 检查是否存在相同商品不同有效期的商品
            if (itemData.isLotMandatory()) {
                List<StockUnit> sourceStockUnits = stockUnitRepository
                        .getByUnitLoadAndItemData(sourceUnitLoad, itemData);
                Lot lot = sourceStockUnits.get(0).getLot();
                checkingBusiness.checkStorageLocationLot(destinationUnitLoad, itemData, lot);
            }
            // 设置数量
            BigDecimal amount = generalBusiness.getStockUnitAmount(destinationUnitLoad);
            dto.setItemDataAmount(amount);
        }
        return dto;
    }

    public ItemDataAmountDTO getItemDataAmounts(String sourceId, String sku) {
        StorageLocation storageLocation = storageLocationRepository.retrieve(sourceId);
        //检查SKU
        checkingBusiness.checkingSKU(sku);
        // 获取汇总容器中商品
        UnitLoad unitLoad = unitLoadBusiness.getByStorageLocation(storageLocation);
        if (unitLoad != null) {
            // 构建返回结果
            List<StockUnit> stockUnits = stockUnitRepository.getByUnitLoadAndSKU(unitLoad, sku);
            if (!stockUnits.isEmpty()) {
                ItemDataAmountDTO dto = new ItemDataAmountDTO(stockUnits.get(0));
                BigDecimal amount = BigDecimal.ZERO;
                for (StockUnit stockUnit : stockUnits) {
                    if (stockUnit.getAmount() == null) {
                        continue;
                    }
                    amount = amount.add(stockUnit.getAmount());
                }
                // 数量如果不为零，那么返回具体的商品数量
                if (!(amount.compareTo(BigDecimal.ZERO) == 0)) {
                    dto.setAmount(amount);
                    return dto;
                }
            }
        }
        throw new ApiException(InternalToolException
                .EX_IT_STORAGE_LOCATION_NOT_FOUND_SKU.getName(), storageLocation.getName());
    }

    public BigDecimal getItemDataGlobalAmount(String sourceId, String sku) {
        StorageLocation storageLocation = storageLocationRepository.findOne(sourceId);
        // 获取汇总容器中商品
        BigDecimal amount = BigDecimal.ZERO;
        UnitLoad unitLoad = unitLoadBusiness.getByStorageLocation(storageLocation);
        if (unitLoad != null) {
            // 构建返回结果
            List<StockUnit> stockUnits = stockUnitRepository.getByUnitLoadAndSKU(unitLoad, sku);
            if (!stockUnits.isEmpty()) {
                for (StockUnit stockUnit : stockUnits) {
                    if (stockUnit.getAmount() == null) {
                        continue;
                    }
                    amount = amount.add(stockUnit.getAmount());
                }
            }
        }
        return amount;
    }
}
