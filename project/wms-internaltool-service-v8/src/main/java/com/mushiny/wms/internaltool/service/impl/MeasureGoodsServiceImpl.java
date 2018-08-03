package com.mushiny.wms.internaltool.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.internaltool.business.*;
import com.mushiny.wms.internaltool.common.domain.*;
import com.mushiny.wms.internaltool.common.enums.StockUnitRecordState;
import com.mushiny.wms.internaltool.common.enums.StockUnitState;
import com.mushiny.wms.internaltool.common.repository.*;
import com.mushiny.wms.internaltool.exception.InternalToolException;
import com.mushiny.wms.internaltool.service.MeasureGoodsService;
import com.mushiny.wms.internaltool.web.dto.ItemDataAmountDTO;
import com.mushiny.wms.internaltool.web.dto.MeasureGoodsDTO;
import com.mushiny.wms.internaltool.web.dto.StorageLocationAmountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class MeasureGoodsServiceImpl implements MeasureGoodsService {

    private final ScanningBusiness scanningBusiness;
    private final StorageLocationRepository storageLocationRepository;
    private final UnitLoadRepository unitLoadRepository;
    private final StockUnitRepository stockUnitRepository;
    private final ItemDataRepository itemDataRepository;
    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final CheckingBusiness checkingBusiness;
    private final GeneralBusiness generalBusiness;
    private final UnitLoadBusiness unitLoadBusiness;
    private final BuildBusiness buildBusiness;

    @Autowired
    public MeasureGoodsServiceImpl(ItemDataRepository itemDataRepository,
                                   StorageLocationRepository storageLocationRepository,
                                   UnitLoadRepository unitLoadRepository,
                                   StockUnitRepository stockUnitRepository,
                                   ItemDataGlobalRepository itemDataGlobalRepository,
                                   CheckingBusiness checkingBusiness,
                                   ScanningBusiness scanningBusiness,
                                   GeneralBusiness generalBusiness,
                                   BuildBusiness buildBusiness,
                                   UnitLoadBusiness unitLoadBusiness) {
        this.itemDataRepository = itemDataRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.unitLoadRepository = unitLoadRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.checkingBusiness = checkingBusiness;
        this.scanningBusiness = scanningBusiness;
        this.generalBusiness = generalBusiness;
        this.buildBusiness = buildBusiness;
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.unitLoadBusiness = unitLoadBusiness;
    }

    @Override
    public StorageLocationAmountDTO scanningSource(String sourceName) {
        return scanningBusiness.getStorageLocationAmount(sourceName);
    }

    @Override
    public ItemDataAmountDTO scanningItemData(String sourceId, String sku) {
        return scanningBusiness.getItemDataAmounts(sourceId, sku);
    }

    @Override
    public StorageLocationAmountDTO scanningDestination(String sourceId,
                                                        String itemDataId,
                                                        String destinationName) {
        return scanningBusiness.getStorageLocationAmount(sourceId, itemDataId, destinationName);
    }

    @Override
    public void measuring(MeasureGoodsDTO measureGoodsDTO) {
        StorageLocation source = storageLocationRepository.retrieve(measureGoodsDTO.getSourceId());
        StorageLocation destination = storageLocationRepository.retrieve(measureGoodsDTO.getDestinationId());
        ItemData itemData = itemDataRepository.retrieve(measureGoodsDTO.getItemDataId());
        UnitLoad sourceUnitLoad = unitLoadBusiness.getByStorageLocation(source);
        if (sourceUnitLoad == null) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName(), source.getName());
        }
        // 检查移动货物数量是否为零
        BigDecimal sourceAmount = generalBusiness.getStockUnitAmount(sourceUnitLoad, itemData);
        if (sourceAmount.compareTo(BigDecimal.ZERO) == 0) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName(), source.getName());
        }
        // 获取该商品所在当前容器的所有库存
        List<StockUnit> sourceStockUnits = stockUnitRepository.getByUnitLoadAndItemData(sourceUnitLoad, itemData);
        StockUnit sourceStockUnit = sourceStockUnits.get(0);
        Lot lot = sourceStockUnit.getLot();
        // 检查目的地容器是否可以存放这些物品
        if (!measureGoodsDTO.getSourceId().equalsIgnoreCase(measureGoodsDTO.getDestinationId())) {
            UnitLoad destinationUnitLoad = unitLoadBusiness.getByStorageLocation(destination);
            if (destinationUnitLoad == null) {
                destinationUnitLoad = buildBusiness.buildUnitLoad(destination);
            } else {
                // 检查目的地容器是否存在不同客户的相同商品
                checkingBusiness.checkStorageLocationClient(destinationUnitLoad, itemData);
                //检查目的地容器是否存在不同有效期的相同商品
                checkingBusiness.checkStorageLocationLot(destinationUnitLoad, itemData, lot);
            }
            // 放入目的容器
            // 检查目的容器是否存在这种商品
            List<StockUnit> destinationStockUnits = stockUnitRepository
                    .getByUnitLoadAndItemData(destinationUnitLoad, itemData);
            StockUnit destinationStockUnit;
            if (destinationStockUnits.isEmpty()) {
                destinationStockUnit = buildBusiness.buildStockUnit(
                        BigDecimal.ONE, destinationUnitLoad, itemData, lot);
            } else {
                destinationStockUnit = destinationStockUnits.get(0);
                destinationStockUnit.setAmount(destinationStockUnit.getAmount().add(BigDecimal.ONE));
                destinationStockUnit = stockUnitRepository.save(destinationStockUnit);
            }
            // 生成测量库存记录
            buildBusiness.buildStockUnitRecord(
                    sourceStockUnit, destinationStockUnit, BigDecimal.ONE,
                    StockUnitRecordState.MEASURE_GOODS_RECORD_CODE.getName(),
                    StockUnitRecordState.MEASURE_GOODS_RECORD_TOOL.getName(),
                    StockUnitRecordState.MEASURE_GOODS_RECORD_TYPE.getName());
            // 更新目的容器的重量
            destinationUnitLoad.setWeightCalculated(
                    destinationUnitLoad.getWeightCalculated().add(measureGoodsDTO.getWeight()));
            unitLoadRepository.save(destinationUnitLoad);
            // 检查sourceUnitLoad是否存在货物
            BigDecimal sourceUnitLoadAmount = stockUnitRepository.sumByUnitLoad(sourceUnitLoad);
            if (sourceUnitLoadAmount.compareTo(BigDecimal.ZERO) <= 0) {
                // 因为容器中已经不存在货物，删除UNITLOAD
                sourceUnitLoad.setEntityLock(Constant.GOING_TO_DELETE);
//                sourceUnitLoad.setStorageLocation(null);
                sourceUnitLoad.setWeightCalculated(BigDecimal.ZERO);
                unitLoadRepository.save(sourceUnitLoad);
            } else {
                // 更新原始容器重量
                sourceUnitLoad.setWeightCalculated(
                        sourceUnitLoad.getWeightCalculated().subtract(itemData.getWeight()));
                unitLoadRepository.save(sourceUnitLoad);
            }
            sourceStockUnit.setAmount(BigDecimal.ZERO);
        } else {
            // 更新原始容器重量
            sourceUnitLoad.setWeightCalculated(
                    sourceUnitLoad.getWeightCalculated()
                            .subtract(itemData.getWeight())
                            .add(measureGoodsDTO.getWeight()));
            unitLoadRepository.save(sourceUnitLoad);
            // 生成测量库存记录
            buildBusiness.buildStockUnitRecord(
                    sourceStockUnit, sourceStockUnit, BigDecimal.ONE,
                    StockUnitRecordState.MEASURE_GOODS_RECORD_CODE.getName(),
                    StockUnitRecordState.MEASURE_GOODS_RECORD_TOOL.getName(),
                    StockUnitRecordState.MEASURE_GOODS_RECORD_TYPE.getName());
        }
        sourceStockUnit.setState(StockUnitState.INVENTORY.getName());
        stockUnitRepository.save(sourceStockUnit);
        // 更新测量数据
        ItemDataGlobal itemDataGlobal = itemDataGlobalRepository.retrieve(itemData.getItemDataGlobalId());
        itemDataGlobal.setHeight(measureGoodsDTO.getHeight());
        itemDataGlobal.setWidth(measureGoodsDTO.getWidth());
        itemDataGlobal.setDepth(measureGoodsDTO.getDepth());
        itemDataGlobal.setWeight(measureGoodsDTO.getWeight());
        itemDataGlobal.setSize(measureGoodsDTO.getSize());
        BigDecimal volume = measureGoodsDTO.getHeight()
                .multiply(measureGoodsDTO.getDepth())
                .multiply(measureGoodsDTO.getWidth());
        itemDataGlobal.setVolume(volume);
        itemDataGlobal.setMeasured(false);
        itemDataGlobalRepository.save(itemDataGlobal);
        // 生成测量记录
        buildBusiness.buildMeasureRecord(itemData, itemDataGlobal, source);
        // 更新所有商品记录
        List<ItemData> items = itemDataRepository.getByItemDataGlobalId(itemDataGlobal.getId());
        for (ItemData item : items) {
            item.setHeight(measureGoodsDTO.getHeight());
            item.setWidth(measureGoodsDTO.getWidth());
            item.setDepth(measureGoodsDTO.getDepth());
            item.setWeight(measureGoodsDTO.getWeight());
            item.setSize(measureGoodsDTO.getSize());
            item.setVolume(volume);
            item.setMeasured(false);
        }
        itemDataRepository.save(items);

    }
}
