package com.mushiny.wms.internaltool.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.RandomUtil;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.internaltool.common.domain.*;
import com.mushiny.wms.internaltool.common.enums.StockUnitRecordState;
import com.mushiny.wms.internaltool.common.repository.*;
import com.mushiny.wms.internaltool.exception.InternalToolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class BuildBusiness {

    private final ApplicationContext applicationContext;
    private final UnitLoadRepository unitLoadRepository;
    private final StockUnitRepository stockUnitRepository;
    private final StockUnitRecordRepository stockUnitRecordRepository;
    private final ItemDataSerialNumberRepository itemDataSerialNumberRepository;
    private final LotRepository lotRepository;
    private final LotRecordRepository lotRecordRepository;
    private final WarehouseRepository warehouseRepository;
    private final ClientRepository clientRepository;
    private final MeasureRecordRepository measureRecordRepository;
    private final GeneralBusiness generalBusiness;

    @Autowired
    public BuildBusiness(ApplicationContext applicationContext,
                         UnitLoadRepository unitLoadRepository,
                         StockUnitRepository stockUnitRepository,
                         StockUnitRecordRepository stockUnitRecordRepository,
                         GeneralBusiness generalBusiness,
                         LotRepository lotRepository,
                         LotRecordRepository lotRecordRepository,
                         ClientRepository clientRepository,
                         MeasureRecordRepository measureRecordRepository,
                         WarehouseRepository warehouseRepository,
                         ItemDataSerialNumberRepository itemDataSerialNumberRepository) {
        this.applicationContext = applicationContext;
        this.unitLoadRepository = unitLoadRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.stockUnitRecordRepository = stockUnitRecordRepository;
        this.generalBusiness = generalBusiness;
        this.lotRepository = lotRepository;
        this.lotRecordRepository = lotRecordRepository;
        this.clientRepository = clientRepository;
        this.measureRecordRepository = measureRecordRepository;
        this.warehouseRepository = warehouseRepository;
        this.itemDataSerialNumberRepository = itemDataSerialNumberRepository;
    }

    public ItemDataSerialNumber buildItemDataSerialNumber(ItemData itemData, String sn) {
        // 判断具体商品是否已经存在保质期
        ItemDataSerialNumber itemDataSerialNumber = itemDataSerialNumberRepository.getBySN(sn);
        if (itemDataSerialNumber != null) {
            throw new ApiException(InternalToolException.EX_IT_ITEMDATA_SN_HAS_USED.getName(), sn);
        }
        if (sn.length() != itemData.getSerialRecordLength()) {
            throw new ApiException(InternalToolException.EX_IT_ITEMDATA_SN_ERROR.getName(), sn);
        }
        itemDataSerialNumber = new ItemDataSerialNumber();
        itemDataSerialNumber.setSerialNo(sn);
        itemDataSerialNumber.setItemData(itemData);
        itemDataSerialNumber.setClientId(itemData.getClient().getId());
        itemDataSerialNumber.setWarehouseId(itemData.getWarehouseId());
        return itemDataSerialNumberRepository.save(itemDataSerialNumber);
    }

    public Lot buildLot(ItemData itemData, LocalDate useNotAfter) {
        // 判断具体商品是否已经存在保质期
        Lot lot = lotRepository.getByItemData(itemData, useNotAfter);
        if (lot == null) {
            lot = new Lot();
            String lotNo;
            boolean randomFlag = true;
            while (randomFlag) {
                lotNo = RandomUtil.getLotNo();
                Lot entity = lotRepository.getByLotNo(lotNo);
                if (entity == null) {
                    lot.setLotNo(lotNo);
                    randomFlag = false;
                }
            }
            lot.setUseNotAfter(useNotAfter);
            lot.setLotDate(LocalDate.now());
            lot.setItemData(itemData);
            lot.setClientId(itemData.getClient().getId());
            lot.setWarehouseId(itemData.getWarehouseId());
            lot = lotRepository.save(lot);
        }
        return lot;
    }

    public LotRecord buildLotRecord(ItemData itemData,
                                    Lot fromLot,
                                    Lot toLot,
                                    StorageLocation sourceStorageLocation,
                                    BigDecimal amount) {
        LotRecord lotRecord = new LotRecord();
        lotRecord.setItemNo(itemData.getItemNo());
        lotRecord.setSku(itemData.getSkuNo());
        lotRecord.setItemDateName(itemData.getName());
        lotRecord.setFromStorageLocation(sourceStorageLocation.getName());
        lotRecord.setAmount(amount);
        if(fromLot != null){
            lotRecord.setFromUseNotAfter(fromLot.getUseNotAfter());
        }else {
            lotRecord.setFromUseNotAfter(null);
        }
        lotRecord.setToUseNotAfter(toLot.getUseNotAfter());
        lotRecord.setRecordTool(StockUnitRecordState.ENTRY_LOT_RECORD_TOOL.getName());
        Client client = clientRepository.retrieve(itemData.getClient().getId());
        lotRecord.setClient(client);
        lotRecord.setWarehouseId(itemData.getWarehouseId());
        return lotRecordRepository.save(lotRecord);
    }

    public MeasureRecord buildMeasureRecord(ItemData itemData,
                                            ItemDataGlobal itemDataGlobal,
                                            StorageLocation sourceStorageLocation) {
        MeasureRecord measureRecord = new MeasureRecord();
        measureRecord.setItemNo(itemData.getItemNo());
        measureRecord.setSku(itemData.getSkuNo());
        measureRecord.setItemDateName(itemData.getName());
        measureRecord.setFromStorageLocation(sourceStorageLocation.getName());
        measureRecord.setFromHeight(itemData.getHeight());
        measureRecord.setFromWidth(itemData.getWidth());
        measureRecord.setFromDepth(itemData.getDepth());
        measureRecord.setFromWeight(itemData.getWeight());
        measureRecord.setToHeight(itemDataGlobal.getHeight());
        measureRecord.setToWidth(itemDataGlobal.getWidth());
        measureRecord.setToDepth(itemDataGlobal.getDepth());
        measureRecord.setToWeight(itemDataGlobal.getWeight());
        Client client = clientRepository.retrieve(itemData.getClient().getId());
        measureRecord.setClient(client);
        measureRecord.setWarehouse(warehouseRepository.retrieve(itemData.getWarehouseId()));
        return measureRecordRepository.save(measureRecord);
    }

    public StockUnit buildStockUnit(BigDecimal amount,
                                    UnitLoad unitLoad,
                                    ItemData itemData,
                                    Lot lot) {
        StockUnit stockUnit = new StockUnit();
        stockUnit.setAmount(amount);
        stockUnit.setReservedAmount(BigDecimal.ZERO);
        stockUnit.setState(generalBusiness.getInventoryState(unitLoad.getStorageLocation()));
        stockUnit.setItemData(itemData);
        stockUnit.setLot(lot);
        stockUnit.setUnitLoad(unitLoad);
        stockUnit.setClientId(itemData.getClient().getId());
        stockUnit.setWarehouseId(itemData.getWarehouseId());
        return stockUnitRepository.save(stockUnit);
    }

    public StockUnitRecord buildStockUnitRecord(StockUnit fromStockUnit,
                                                StockUnit toStockUnit,
                                                BigDecimal amount,
                                                String recordCode,
                                                String recordTool,
                                                String recordType,
                                                String adjustReason) {
        StockUnitRecord stockUnitRecord = buildStockUnitRecord(fromStockUnit, toStockUnit,
                amount, recordCode, recordTool, recordType);
        stockUnitRecord.setAdjustReason(adjustReason);
        return stockUnitRecordRepository.save(stockUnitRecord);
    }
    public StockUnitRecord buildStockUnitRecord(StockUnit fromStockUnit,
                                                StockUnit toStockUnit,
                                                BigDecimal amount,
                                                String recordCode,
                                                String recordTool,
                                                String recordType) {
        StockUnitRecord stockUnitRecord = new StockUnitRecord();
        stockUnitRecord.setRecordCode(recordCode);
        stockUnitRecord.setRecordTool(recordTool);
        stockUnitRecord.setRecordType(recordType);
        stockUnitRecord.setAmount(amount);
        stockUnitRecord.setAmountStock(toStockUnit.getAmount());
        stockUnitRecord.setFromStockUnit(fromStockUnit.getId());
        stockUnitRecord.setFromUnitLoad(fromStockUnit.getUnitLoad().getLabel());
        stockUnitRecord.setFromStorageLocation(fromStockUnit.getUnitLoad().getStorageLocation().getName());
        stockUnitRecord.setItemNo(toStockUnit.getItemData().getItemNo());
        stockUnitRecord.setSku(toStockUnit.getItemData().getSkuNo());
        stockUnitRecord.setFromState(fromStockUnit.getState());
        stockUnitRecord.setToState(toStockUnit.getState());
        if (toStockUnit.getLot() != null) {
            stockUnitRecord.setLot(toStockUnit.getLot().getLotNo());
        }
        stockUnitRecord.setOperator(SecurityUtils.getCurrentUsername());
        stockUnitRecord.setToStockUnit(toStockUnit.getId());
        stockUnitRecord.setToUnitLoad(toStockUnit.getUnitLoad().getLabel());
        stockUnitRecord.setToStorageLocation(toStockUnit.getUnitLoad().getStorageLocation().getName());
        Client client = clientRepository.retrieve(toStockUnit.getClientId());
        stockUnitRecord.setClient(client);
        stockUnitRecord.setWarehouseId(toStockUnit.getWarehouseId());
        return stockUnitRecordRepository.save(stockUnitRecord);
    }

    public UnitLoad buildUnitLoad(StorageLocation storageLocation) {
        UnitLoad unitLoad = new UnitLoad();
        boolean useFlag = true;
        while (useFlag) {
            String label = RandomUtil.getUnitLoadLabel();
            UnitLoad useUnitLoad = unitLoadRepository.getByLabel(label);
            if (useUnitLoad == null) {
                unitLoad.setLabel(label);
                useFlag = false;
            }
        }
        unitLoad.setStockTakingDate(LocalDateTime.now());
        unitLoad.setStorageLocation(storageLocation);
        unitLoad.setWeight(BigDecimal.ZERO);
        unitLoad.setWeightCalculated(BigDecimal.ZERO);
        unitLoad.setWeightMeasure(BigDecimal.ZERO);
        unitLoad.setLocationIndex(0);
        unitLoad.setCarrier(false);
        unitLoad.setClientId(applicationContext.getCurrentClient());
        unitLoad.setWarehouseId(applicationContext.getCurrentWarehouse());
        return unitLoadRepository.save(unitLoad);
    }


}
