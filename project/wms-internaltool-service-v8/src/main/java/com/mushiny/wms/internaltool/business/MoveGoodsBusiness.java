package com.mushiny.wms.internaltool.business;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.internaltool.common.domain.*;
import com.mushiny.wms.internaltool.common.repository.StockUnitRepository;
import com.mushiny.wms.internaltool.common.repository.UnitLoadRepository;
import com.mushiny.wms.internaltool.exception.InternalToolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MoveGoodsBusiness {

    private final UnitLoadBusiness unitLoadBusiness;
    private final StockUnitRepository stockUnitRepository;
    private final UnitLoadRepository unitLoadRepository;
    private final CheckingBusiness checkingBusiness;
    private final GeneralBusiness generalBusiness;
    private final BuildBusiness buildBusiness;

    @Autowired
    public MoveGoodsBusiness(UnitLoadBusiness unitLoadBusiness,
                             StockUnitRepository stockUnitRepository,
                             CheckingBusiness checkingBusiness,
                             GeneralBusiness generalBusiness,
                             BuildBusiness buildBusiness,
                             UnitLoadRepository unitLoadRepository) {
        this.unitLoadBusiness = unitLoadBusiness;
        this.stockUnitRepository = stockUnitRepository;
        this.checkingBusiness = checkingBusiness;
        this.generalBusiness = generalBusiness;
        this.buildBusiness = buildBusiness;
        this.unitLoadRepository = unitLoadRepository;
    }

    public void moveAll(StorageLocation source,
                        StorageLocation destination,
                        String recordCode,
                        String recordTool,
                        String recordType) {
        UnitLoad sourceUnitLoad = unitLoadBusiness.getByStorageLocation(source);
        if (sourceUnitLoad == null) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName(),source.getName());
        }
        // 获取该当前容器的所有库存
        List<StockUnit> sourceStockUnits = stockUnitRepository.getByUnitLoad(sourceUnitLoad);
        // 按商品分组
        Map<String, List<StockUnit>> stockUnitMap = sourceStockUnits.stream().collect(
                Collectors.groupingBy(entity -> entity.getItemData().getId()));
        // 按UnitLoad处理库存
        for (String key : stockUnitMap.keySet()) {
            List<StockUnit> stockUnits = stockUnitMap.get(key);
            if (stockUnits.isEmpty()) {
                continue;
            }
            ItemData itemData = stockUnits.get(0).getItemData();
            BigDecimal amount = stockUnitRepository.sumByUnitLoadAndItemData(sourceUnitLoad, itemData);
            moving(source, destination, itemData, amount, recordCode, recordTool, recordType);
        }
    }

    @SuppressWarnings("Duplicates")
    public void moving(StorageLocation source,
                       StorageLocation destination,
                       ItemData itemData,
                       BigDecimal moveItemDataAmount,
                       String recordCode,
                       String recordTool,
                       String recordType) {
        UnitLoad sourceUnitLoad = unitLoadBusiness.getByStorageLocation(source);
        if (sourceUnitLoad == null) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName(), source.getName());
        }
        // 检查移动货物数量是否超过系统设定
        BigDecimal sourceAmount = generalBusiness.getStockUnitAmount(sourceUnitLoad, itemData);
        if (sourceAmount.compareTo(moveItemDataAmount) == -1) {
            throw new ApiException(InternalToolException.EX_IT_AMOUNT_MORE_THAN_SYSTEM_AMOUNT.getName(),destination.getName());
        }
        // 获取该商品所在当前容器的所有库存
        List<StockUnit> sourceStockUnits = stockUnitRepository.getByUnitLoadAndItemData(sourceUnitLoad, itemData);
        Lot lot = sourceStockUnits.get(0).getLot();
        // 检查目的地容器
        UnitLoad destinationUnitLoad = unitLoadBusiness.getByStorageLocation(destination);
        if (destinationUnitLoad == null) {
            destinationUnitLoad = buildBusiness.buildUnitLoad(destination);
        } else {
            // 检查目的地容器是否存在不同客户的相同商品
            checkingBusiness.checkStorageLocationClient(destinationUnitLoad, itemData);
            //检查目的地容器是否存在不同有效期的相同商品
            checkingBusiness.checkStorageLocationLot(destinationUnitLoad, itemData, lot);
            if (checkingBusiness.isBin(destination)) {
                // 货位中存在相似商品
                checkingBusiness.checkStorageLocationSimilar(destinationUnitLoad, itemData);
            }
        }
        /*if (!source.getStorageLocationType().getInventoryState().equals(
                destination.getStorageLocationType().getInventoryState())) {
            throw new ApiException(InternalToolException.EX_IT_STORAGE_LOCATION_DIFFERENT_INVENTORY_STATE.toString());
        }*/
        if(!sourceStockUnits.get(0).getState().equalsIgnoreCase(destination.getStorageLocationType().getInventoryState())){
            throw new ApiException(InternalToolException.EX_IT_STORAGE_LOCATION_DIFFERENT_INVENTORY_STATE.getName());
        }
        if (checkingBusiness.isBin(destination)) {
            // 超过货位的载重量
            checkingBusiness.checkStorageLocationWeight(destinationUnitLoad, itemData, moveItemDataAmount);
            // 商品属性与货位设置不符
            checkingBusiness.checkStorageLocationItemGroup(destination, itemData);
            // 商品种类超过系统设置数量
            checkingBusiness.checkStorageLocationItemsAmount(destinationUnitLoad, itemData);
        }
        // 移货
        // 减去原始容器的相应货物的数量
        BigDecimal moveAmount = moveItemDataAmount;
        StockUnit sourceStockUnit = sourceStockUnits.get(0);
        for (StockUnit stockUnit : sourceStockUnits) {
            // BigDecimal stockAmount = stockUnit.getAmount().subtract(stockUnit.getReservedAmount());
            //可用数量
            BigDecimal useAmount = stockUnit.getAmount().subtract(stockUnit.getReservedAmount());
            //库存总数
            BigDecimal stockAmount = stockUnit.getAmount();
            if (useAmount.compareTo(moveAmount) >= 0) {
                stockUnit.setAmount(stockAmount.subtract(moveAmount));
                break;
            } else {
                moveAmount = moveAmount.subtract(useAmount);
                stockUnit.setAmount(stockAmount.subtract(useAmount));
            }
        }
        stockUnitRepository.save(sourceStockUnits);
        // 放入目的容器
        // 检查目的容器是否存在这种商品
        List<StockUnit> destinationStockUnits = stockUnitRepository
                .getByUnitLoadAndItemData(destinationUnitLoad, itemData);
        StockUnit destinationStockUnit;
        if (destinationStockUnits.isEmpty()) {
            destinationStockUnit = buildBusiness.buildStockUnit(moveItemDataAmount, destinationUnitLoad, itemData, lot);
        } else {
            destinationStockUnit = destinationStockUnits.get(0);
            destinationStockUnit.setAmount(destinationStockUnit.getAmount().add(moveItemDataAmount));
            destinationStockUnit = stockUnitRepository.save(destinationStockUnit);
        }
        // 生成移货库存记录
        buildBusiness.buildStockUnitRecord(
                sourceStockUnit, destinationStockUnit, moveItemDataAmount, recordCode, recordTool, recordType);
        // 更新目的容器的重量
        BigDecimal itemDataWeight = itemData.getWeight().multiply(moveItemDataAmount);
        destinationUnitLoad.setWeightCalculated(
                destinationUnitLoad.getWeightCalculated().add(itemDataWeight));
        unitLoadRepository.save(destinationUnitLoad);
        // 检查sourceUnitLoad是否存在货物
        BigDecimal sourceUnitLoadAmount = stockUnitRepository.sumByUnitLoad(sourceUnitLoad);
        if (sourceUnitLoadAmount.compareTo(BigDecimal.ZERO) <= 0) {
            // 因为容器中已经不存在货物，删除UNITLOAD
            sourceUnitLoad.setEntityLock(Constant.GOING_TO_DELETE);
//            sourceUnitLoad.setStorageLocation(null);
            sourceUnitLoad.setWeightCalculated(BigDecimal.ZERO);
            unitLoadRepository.save(sourceUnitLoad);
        } else {
            // 更新原始容器重量
            sourceUnitLoad.setWeightCalculated(
                    sourceUnitLoad.getWeightCalculated().subtract(itemDataWeight));
            unitLoadRepository.save(sourceUnitLoad);
        }
    }
}
