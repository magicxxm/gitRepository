package com.mushiny.wms.outboundproblem.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.RandomUtil;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.outboundproblem.domain.common.*;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.repository.OBPStationRepository;
import com.mushiny.wms.outboundproblem.repository.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class BuildBusiness {

    private final ApplicationContext applicationContext;
    private final UnitLoadRepository unitLoadRepository;
    private final StockUnitRepository stockUnitRepository;
    private final StockUnitRecordRepository stockUnitRecordRepository;
    private final LotRepository lotRepository;
    private final LotRecordRepository lotRecordRepository;
    private final ClientRepository clientRepository;
    private final MeasureRecordRepository measureRecordRepository;
    private final GeneralBusiness generalBusiness;
    private final ItemDataSerialNumberRepository itemDataSerialNumberRepository;
    private final StorageLocationTypeRepository storageLocationTypeRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final UserRepository userRepository;
    private final OBPStationRepository obpStationRepository;
    private final CustomerShipmentRecordRepository customerShipmentRecordRepository;
    private final CustomerShipmentHotPickRepository hotPickRepository;

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
                         ItemDataSerialNumberRepository itemDataSerialNumberRepository,
                         StorageLocationTypeRepository storageLocationTypeRepository,
                         StorageLocationRepository storageLocationRepository,
                         CustomerShipmentRepository customerShipmentRepository,
                         UserRepository userRepository,
                         OBPStationRepository obpStationRepository,
                         CustomerShipmentRecordRepository customerShipmentRecordRepository,
                         CustomerShipmentHotPickRepository hotPickRepository) {
        this.applicationContext = applicationContext;
        this.unitLoadRepository = unitLoadRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.stockUnitRecordRepository = stockUnitRecordRepository;
        this.generalBusiness = generalBusiness;
        this.lotRepository = lotRepository;
        this.lotRecordRepository = lotRecordRepository;
        this.clientRepository = clientRepository;
        this.measureRecordRepository = measureRecordRepository;
        this.itemDataSerialNumberRepository = itemDataSerialNumberRepository;
        this.storageLocationTypeRepository = storageLocationTypeRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.customerShipmentRepository = customerShipmentRepository;
        this.userRepository = userRepository;
        this.obpStationRepository = obpStationRepository;
        this.customerShipmentRecordRepository = customerShipmentRecordRepository;
        this.hotPickRepository = hotPickRepository;
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
            lot.setClientId(itemData.getClientId());
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
        lotRecord.setFromUseNotAfter(fromLot.getUseNotAfter());
        lotRecord.setToUseNotAfter(toLot.getUseNotAfter());
        Client client = clientRepository.retrieve(itemData.getClientId());
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
        Client client = clientRepository.retrieve(itemData.getClientId());
        measureRecord.setClient(client);
        measureRecord.setWarehouseId(itemData.getWarehouseId());
        return measureRecordRepository.save(measureRecord);
    }

    public StockUnit buildStockUnit(BigDecimal amount,
                                    UnitLoad unitLoad,
                                    ItemData itemData,
                                    Lot lot,
                                    String serialNo) {
        StockUnit stockUnit = new StockUnit();
        stockUnit.setAmount(amount);
        stockUnit.setReservedAmount(BigDecimal.ZERO);
        stockUnit.setState(generalBusiness.getInventoryState(unitLoad.getStorageLocation()));
        stockUnit.setItemData(itemData);
        stockUnit.setLot(lot);
        stockUnit.setUnitLoad(unitLoad);
        stockUnit.setClientId(itemData.getClientId());
        stockUnit.setWarehouseId(itemData.getWarehouseId());
        stockUnit.setSerialNo(serialNo);
        return stockUnitRepository.save(stockUnit);
    }

//    public StockUnitRecord buildStockUnitRecord(StockUnit fromStockUnit,
//                                                StockUnit toStockUnit,
//                                                BigDecimal amount,
//                                                String recordCode,
//                                                String recordTool,
//                                                String recordType,
//                                                String adjustReason) {
//        StockUnitRecord stockUnitRecord = buildStockUnitRecord(fromStockUnit, toStockUnit,
//                amount, recordCode, recordTool, recordType);
//        stockUnitRecord.setAdjustReason(adjustReason);
//        return stockUnitRecordRepository.save(stockUnitRecord);
//    }
    public StockUnitRecord buildStockUnitRecordProblem(StockUnit fromStockUnit,
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
        stockUnitRecord.setFromStockUnit(toStockUnit.getId());
        stockUnitRecord.setFromUnitLoad(toStockUnit.getUnitLoad().getLabel());
        stockUnitRecord.setFromStorageLocation(toStockUnit.getUnitLoad().getStorageLocation().getName());
        stockUnitRecord.setItemNo(toStockUnit.getItemData().getItemNo());
        stockUnitRecord.setSku(toStockUnit.getItemData().getSkuNo());
        stockUnitRecord.setFromState(toStockUnit.getState());
        stockUnitRecord.setToState(toStockUnit.getState());
        if (toStockUnit.getLot() != null) {
            stockUnitRecord.setLot(toStockUnit.getLot().getLotNo());
        }
        stockUnitRecord.setOperator(SecurityUtils.getCurrentUsername());
        stockUnitRecord.setToStockUnit(toStockUnit.getId());
        stockUnitRecord.setToUnitLoad(toStockUnit.getUnitLoad().getLabel());
        stockUnitRecord.setToStorageLocation(toStockUnit.getUnitLoad().getStorageLocation().getName());
        stockUnitRecord.setClientId(toStockUnit.getClientId());
        stockUnitRecord.setWarehouseId(toStockUnit.getWarehouseId());
        stockUnitRecordRepository.save(stockUnitRecord);

        StockUnitRecord stockUnitRecords = new StockUnitRecord();
        stockUnitRecords.setRecordCode(recordCode);
        stockUnitRecords.setRecordTool(recordTool);
        stockUnitRecords.setRecordType(recordType);
        stockUnitRecords.setAmount(amount);
        stockUnitRecords.setAmountStock(fromStockUnit.getAmount());
        stockUnitRecords.setFromStockUnit(fromStockUnit.getId());
        stockUnitRecords.setFromUnitLoad(fromStockUnit.getUnitLoad().getLabel());
        stockUnitRecords.setFromStorageLocation(fromStockUnit.getUnitLoad().getStorageLocation().getName());
        stockUnitRecords.setItemNo(fromStockUnit.getItemData().getItemNo());
        stockUnitRecords.setSku(fromStockUnit.getItemData().getSkuNo());
        stockUnitRecords.setFromState(fromStockUnit.getState());
        stockUnitRecords.setToState(fromStockUnit.getState());
        if (fromStockUnit.getLot() != null) {
            stockUnitRecords.setLot(fromStockUnit.getLot().getLotNo());
        }
        stockUnitRecords.setOperator(SecurityUtils.getCurrentUsername());
        stockUnitRecords.setToStockUnit(fromStockUnit.getId());
        stockUnitRecords.setToUnitLoad(fromStockUnit.getUnitLoad().getLabel());
        stockUnitRecords.setToStorageLocation(fromStockUnit.getUnitLoad().getStorageLocation().getName());
        stockUnitRecords.setClientId(fromStockUnit.getClientId());
        stockUnitRecords.setWarehouseId(fromStockUnit.getWarehouseId());
        return stockUnitRecordRepository.save(stockUnitRecords);
    }

    public StockUnitRecord buildStockUnitRecord(StockUnit fromStockUnit,
                                                StockUnit toStockUnit,
                                                BigDecimal amount,
                                                String recordCode,
                                                String recordTool,
                                                String recordType,
                                                String serialNo) {
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
        stockUnitRecord.setSerialNo(serialNo);
        if (toStockUnit.getLot() != null) {
            stockUnitRecord.setLot(toStockUnit.getLot().getLotNo());
        }
        stockUnitRecord.setOperator(SecurityUtils.getCurrentUsername());
        stockUnitRecord.setToStockUnit(toStockUnit.getId());
        stockUnitRecord.setToUnitLoad(toStockUnit.getUnitLoad().getLabel());
        stockUnitRecord.setToStorageLocation(toStockUnit.getUnitLoad().getStorageLocation().getName());
        Client client = clientRepository.retrieve(toStockUnit.getClientId());
        stockUnitRecord.setClientId(client.getId());
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
        unitLoad.setCarrier(false);
        unitLoad.setClientId(applicationContext.getCurrentClient());
        unitLoad.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (storageLocation.getPod() == null) {
            unitLoad.setLocationIndex(0);
        } else {
            unitLoad.setLocationIndex(storageLocation.getPod().getPodIndex());
        }
        return unitLoadRepository.save(unitLoad);
    }

    public ItemDataSerialNumber buildItemDataSerialNumber(ItemData itemData, String sn) {
        // 判断具体商品是否已经存在保质期
        ItemDataSerialNumber itemDataSerialNumber = itemDataSerialNumberRepository.getBySN(sn);
        if (itemDataSerialNumber != null) {
            throw new ApiException(OutBoundProblemException.EX_IT_ITEMDATA_SN_HAS_USED.getName(), sn);
        }
        if (sn.length() != itemData.getSerialRecordLength()) {
            throw new ApiException(OutBoundProblemException.EX_IT_ITEMDATA_SN_ERROR.getName(), sn);
        }
        itemDataSerialNumber = new ItemDataSerialNumber();
        itemDataSerialNumber.setSerialNo(sn);
        itemDataSerialNumber.setItemData(itemData);
        itemDataSerialNumber.setClientId(itemData.getClientId());
        itemDataSerialNumber.setWarehouseId(itemData.getWarehouseId());
        return itemDataSerialNumberRepository.save(itemDataSerialNumber);
    }

    public StorageLocation buildVirtualStorageLocation(String name) {
        StorageLocation storageLocation = new StorageLocation();
        storageLocation.setName(name);
        storageLocation.setxPos(0);
        storageLocation.setyPos(0);
        storageLocation.setzPos(0);
        storageLocation.setOrderIndex(0);
       // storageLocation.setStorageLocationType(storageLocationTypeRepository.getInventoryContainer());
        storageLocation.setWarehouseId(applicationContext.getCurrentWarehouse());
        storageLocation.setClientId(applicationContext.getCurrentClient());

        return Optional
                .ofNullable(storageLocationRepository.save(storageLocation))
                .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_CREATE_VIRTUAL_STORAGELOCATION_IS_FAILED.getName(), name));
    }

    public void buildCustomerShipmentRecord(String shipmentNo,String stationId,String stateName,int state){
        CustomerShipmentRecord customerShipmentRecord=new CustomerShipmentRecord();
        customerShipmentRecord.setCustomerShipmentId(customerShipmentRepository.getByShipmentNo(shipmentNo).getId());
        customerShipmentRecord.setStateName(stateName);
        customerShipmentRecord.setState(state);
        customerShipmentRecord.setOperatorId(applicationContext.getCurrentUser());
        customerShipmentRecord.setStationName(obpStationRepository.retrieve(stationId).getName());
        customerShipmentRecord.setWarehouseId(applicationContext.getCurrentWarehouse());
        customerShipmentRecord.setClientId(applicationContext.getCurrentClient());
        customerShipmentRecordRepository.save(customerShipmentRecord);
    }

    public void buildCustomerShipmentHotPick(String shipmentNo,String itemDataId,BigDecimal amount){
        CustomerShipmentHotPick hotPick=new CustomerShipmentHotPick();
        hotPick.setShipmentId(customerShipmentRepository.getByShipmentNo(shipmentNo).getId());
        hotPick.setItemDataId(itemDataId);
        hotPick.setAmount(amount);
        hotPick.setState(0);
        hotPickRepository.save(hotPick);
    }
}
