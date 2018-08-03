package com.mushiny.wms.internaltool.business;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.internaltool.common.domain.*;
import com.mushiny.wms.internaltool.common.repository.*;
import com.mushiny.wms.internaltool.exception.InternalToolException;
import com.mushiny.wms.internaltool.web.dto.LossGoodsDTO;
import com.mushiny.wms.internaltool.web.dto.OverageGoodsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class InventoryGoodsBusiness {

    private final ApplicationContext applicationContext;
    private final UnitLoadRepository unitLoadRepository;
    private final StockUnitRepository stockUnitRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final ItemDataRepository itemDataRepository;
    private final StockUnitRecordRepository stockUnitRecordRepository;
    private final ClientRepository clientRepository;
    private final CheckingBusiness checkingBusiness;
    private final UnitLoadBusiness unitLoadBusiness;
    private final GeneralBusiness generalBusiness;
    private final BuildBusiness buildBusiness;

    @Autowired
    public InventoryGoodsBusiness(UnitLoadRepository unitLoadRepository,
                                  StockUnitRepository stockUnitRepository,
                                  CheckingBusiness checkingBusiness,
                                  GeneralBusiness generalBusiness,
                                  BuildBusiness buildBusiness,
                                  StorageLocationRepository storageLocationRepository,
                                  ItemDataRepository itemDataRepository,
                                  StockUnitRecordRepository stockUnitRecordRepository,
                                  ApplicationContext applicationContext,
                                  ClientRepository clientRepository,
                                  UnitLoadBusiness unitLoadBusiness) {
        this.unitLoadRepository = unitLoadRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.checkingBusiness = checkingBusiness;
        this.generalBusiness = generalBusiness;
        this.buildBusiness = buildBusiness;
        this.storageLocationRepository = storageLocationRepository;
        this.itemDataRepository = itemDataRepository;
        this.stockUnitRecordRepository = stockUnitRecordRepository;
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.unitLoadBusiness = unitLoadBusiness;
    }

    @SuppressWarnings("Duplicates")
    public void overageGoods(OverageGoodsDTO overageGoodsDTO,
                             String recordCode,
                             String recordTool,
                             String recordType) {
        StorageLocation destination = storageLocationRepository.retrieve(overageGoodsDTO.getDestinationId());
        ItemData itemData = itemDataRepository.getByItemNo(
                overageGoodsDTO.getItemNo(), overageGoodsDTO.getClientId(), applicationContext.getCurrentWarehouse());
        if (itemData == null) {
            throw new ApiException(InternalToolException
                    .EX_IT_SKU_NOT_FOUND.getName(), overageGoodsDTO.getItemNo());
        }
        // 测量车牌不容许做为盘盈目的容器
        if (destination.getStorageLocationType().getInventoryState().equalsIgnoreCase("MEASURED")) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_IS_MEASURED.getName(), destination.getName());
        }
        // 目的容器属性是否和盘盈属性一致
        if (!destination.getStorageLocationType().getInventoryState().equalsIgnoreCase(overageGoodsDTO.getInventoryState())) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_NOT_USED.getName(), destination.getName());
        }
        // 获取检查SN
        if (itemData.getSerialRecordType().equalsIgnoreCase("ALWAYS_RECORD")) {
            buildBusiness.buildItemDataSerialNumber(itemData, overageGoodsDTO.getSn());
        }
        // 获取有效期
        Lot lot = null;
        if (itemData.isLotMandatory()) {
            lot = buildBusiness.buildLot(itemData, overageGoodsDTO.getUseNotAfter());
        }
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
        if (checkingBusiness.isBin(destination)) {
            // 超过货位的载重量
            checkingBusiness.checkStorageLocationWeight(destinationUnitLoad, itemData, overageGoodsDTO.getAmount());
            // 商品属性与货位设置不符
            checkingBusiness.checkStorageLocationItemGroup(destination, itemData);
            // 商品种类超过系统设置数量
            checkingBusiness.checkStorageLocationItemsAmount(destinationUnitLoad, itemData);
        }
        // 盘盈
        // 检查目的容器是否存在这种商品
        List<StockUnit> destinationStockUnits = stockUnitRepository
                .getByUnitLoadAndItemData(destinationUnitLoad, itemData);
        StockUnit destinationStockUnit;
        if (destinationStockUnits.isEmpty()) {
            destinationStockUnit = buildBusiness.buildStockUnit(
                    overageGoodsDTO.getAmount(), destinationUnitLoad, itemData, lot);
        } else {
            destinationStockUnit = destinationStockUnits.get(0);
            destinationStockUnit.setAmount(destinationStockUnit.getAmount().add(overageGoodsDTO.getAmount()));
            destinationStockUnit = stockUnitRepository.save(destinationStockUnit);
        }
        // 更新目的容器的重量
        BigDecimal itemDataWeight = itemData.getWeight().multiply(overageGoodsDTO.getAmount());
        destinationUnitLoad.setWeightCalculated(
                destinationUnitLoad.getWeightCalculated().add(itemDataWeight));
        unitLoadRepository.save(destinationUnitLoad);
        // 生成盘亏记录
        StockUnitRecord stockUnitRecord = new StockUnitRecord();
        stockUnitRecord.setRecordCode(recordCode);
        stockUnitRecord.setRecordTool(recordTool);
        stockUnitRecord.setRecordType(recordType);
        stockUnitRecord.setAmount(overageGoodsDTO.getAmount());
        stockUnitRecord.setItemNo(itemData.getItemNo());
        stockUnitRecord.setSku(itemData.getSkuNo());
        stockUnitRecord.setToState(overageGoodsDTO.getInventoryState());
        stockUnitRecord.setToStockUnit(destinationStockUnit.getId());
        stockUnitRecord.setToUnitLoad(destinationUnitLoad.getLabel());
        stockUnitRecord.setToStorageLocation(destination.getName());
        stockUnitRecord.setSerialNumber(overageGoodsDTO.getSn());
        if (lot != null) {
            stockUnitRecord.setLot(lot.getLotNo());
        }
        stockUnitRecord.setOperator(SecurityUtils.getCurrentUsername());
        Client client = clientRepository.retrieve(destinationStockUnit.getClientId());
        stockUnitRecord.setClient(client);
        stockUnitRecord.setWarehouseId(destinationStockUnit.getWarehouseId());
        stockUnitRecord.setAdjustReason(overageGoodsDTO.getAdjustReason());
        stockUnitRecord.setProblemDestination(overageGoodsDTO.getProblemDestination());
        stockUnitRecord.setThoseResponsible(overageGoodsDTO.getThoseResponsible());
        stockUnitRecordRepository.save(stockUnitRecord);
    }

    @SuppressWarnings("Duplicates")
    public void lossGoods(LossGoodsDTO lossGoodsDTO,
                          String recordCode,
                          String recordTool,
                          String recordType) {
        StorageLocation source = storageLocationRepository.retrieve(lossGoodsDTO.getSourceId());
        ItemData itemData = itemDataRepository.retrieve(lossGoodsDTO.getItemDataId());
        UnitLoad sourceUnitLoad = unitLoadBusiness.getByStorageLocation(source);
        if (sourceUnitLoad == null) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName(), source.getName());
        }
        // 检查移动货物数量是否超过系统设定
        BigDecimal sourceAmount = generalBusiness.getStockUnitAmount(sourceUnitLoad, itemData);
        if (sourceAmount.compareTo(lossGoodsDTO.getAmount()) == -1) {
            throw new ApiException(InternalToolException.EX_IT_AMOUNT_MORE_THAN_SYSTEM_AMOUNT.getName(),source.getName());
        }
        // 减去盘亏的数量
        List<StockUnit> sourceStockUnits = stockUnitRepository.getByUnitLoadAndItemData(sourceUnitLoad, itemData);
        StockUnit sourceStockUnit=sourceStockUnits.get(0);
        BigDecimal moveAmount=lossGoodsDTO.getAmount();
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
            BigDecimal itemDataWeight = itemData.getWeight().multiply(lossGoodsDTO.getAmount());
            sourceUnitLoad.setWeightCalculated(
                    sourceUnitLoad.getWeightCalculated().subtract(itemDataWeight));
            unitLoadRepository.save(sourceUnitLoad);
        }
        // 生成盘亏记录
        StockUnitRecord stockUnitRecord = new StockUnitRecord();
        stockUnitRecord.setRecordCode(recordCode);
        stockUnitRecord.setRecordTool(recordTool);
        stockUnitRecord.setRecordType(recordType);
        stockUnitRecord.setAmount(lossGoodsDTO.getAmount());
        stockUnitRecord.setFromStockUnit(sourceStockUnit.getId());
        stockUnitRecord.setFromUnitLoad(sourceUnitLoad.getLabel());
        stockUnitRecord.setFromStorageLocation(source.getName());
        stockUnitRecord.setItemNo(itemData.getItemNo());
        stockUnitRecord.setSku(itemData.getSkuNo());
        stockUnitRecord.setFromState(sourceStockUnit.getState());
        stockUnitRecord.setToState(sourceStockUnit.getState());
        if (sourceStockUnit.getLot() != null) {
            stockUnitRecord.setLot(sourceStockUnit.getLot().getLotNo());
        }
        stockUnitRecord.setOperator(SecurityUtils.getCurrentUsername());
        Client client = clientRepository.retrieve(sourceStockUnit.getClientId());
        stockUnitRecord.setClient(client);
        stockUnitRecord.setWarehouseId(sourceStockUnit.getWarehouseId());
        stockUnitRecord.setAdjustReason(lossGoodsDTO.getAdjustReason());
        stockUnitRecord.setProblemDestination(lossGoodsDTO.getProblemDestination());
        stockUnitRecord.setThoseResponsible(lossGoodsDTO.getThoseResponsible());
        stockUnitRecordRepository.save(stockUnitRecord);
    }
}
