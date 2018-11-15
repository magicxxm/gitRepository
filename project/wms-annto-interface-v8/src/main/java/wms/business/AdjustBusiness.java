package wms.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import wms.business.dto.AdjustConfirmDTO;
import wms.common.context.ApplicationContext;
import wms.common.crud.AccessDTO;

import wms.config.security.SecurityUtils;
import wms.domain.ItemData;
import wms.domain.common.*;
import wms.repository.common.*;
import wms.domain.PendingAdjustRecord;


import javax.persistence.EntityManager;
import java.math.BigDecimal;

/**
 * Created by 123 on 2018/1/3.
 */
@Component
public class AdjustBusiness {
    private final Logger log = LoggerFactory.getLogger(AdjustBusiness.class);
    private final EntityManager manager;
    private final PendingAdjustRecordRepository pendingAdjustRecordRepository;
    private final StockUnitRepository stockUnitRepository;
    private final UnitLoadRepository unitLoadRepository;
    private final ItemDataRepository itemDataRepository;
    private final ApplicationContext applicationContext;
    private final StorageLocationRepository storageLocationRepository;
    private final StockUnitRecordRepository stockUnitRecordRepository;

    public AdjustBusiness(EntityManager manager,
                          PendingAdjustRecordRepository pendingAdjustRecordRepository,
                          StockUnitRepository stockUnitRepository,
                          UnitLoadRepository unitLoadRepository,
                          ItemDataRepository itemDataRepository,
                          ApplicationContext applicationContext,
                          StorageLocationRepository storageLocationRepository, StockUnitRecordRepository stockUnitRecordRepository){
        this.manager = manager;
        this.pendingAdjustRecordRepository = pendingAdjustRecordRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.unitLoadRepository = unitLoadRepository;
        this.itemDataRepository = itemDataRepository;
        this.applicationContext = applicationContext;
        this.storageLocationRepository = storageLocationRepository;
        this.stockUnitRecordRepository = stockUnitRecordRepository;
    }


    public AccessDTO adjust(AdjustConfirmDTO adjustConfirmDTO) {
        AccessDTO accessDTO = new AccessDTO();
        /*PendingAdjustRecord pendingAdjustRecord= pendingAdjustRecordRepository.getPendingAdjust(adjustConfirmDTO.getPendingAdjustId());
        if (pendingAdjustRecord.getRecordType().equalsIgnoreCase("INVENTORY OVERAGE")){
            overageGoods(pendingAdjustRecord);
        }else {
            lossGoods(pendingAdjustRecord);
        }
        pendingAdjustRecord.setState(adjustConfirmDTO.getState());
        pendingAdjustRecordRepository.saveAndFlush(pendingAdjustRecord);*/
        accessDTO.setMsg("success");
        accessDTO.setCode("0");
        return accessDTO;
    }


    public void lossGoods(PendingAdjustRecord pendingAdjustRecord) {
        StorageLocation source = storageLocationRepository.getByName(applicationContext.getCurrentWarehouse(),pendingAdjustRecord.getFromStorageLocation());
        ItemData itemData = itemDataRepository.getByItemCode(applicationContext.getCurrentWarehouse(), applicationContext.getCurrentClient(),pendingAdjustRecord.getItemNo());
        UnitLoad sourceUnitLoad = unitLoadRepository.getByStorageLocation(source);
        StockUnit pendingStockUnit = stockUnitRepository.retrieve(pendingAdjustRecord.getStockUnitId());

        // StockUnit  减去盘亏的数量 , 更新state(Adjust--->Inventory)
        BigDecimal stockAmount = pendingStockUnit.getAmount().subtract(pendingAdjustRecord.getAmount());
        pendingStockUnit.setAmount(stockAmount);
        pendingStockUnit.setState("Inventory");
        stockUnitRepository.save(pendingStockUnit);

        // 更新原始容器重量
        BigDecimal itemDataWeight = itemData.getWeight().multiply(pendingAdjustRecord.getAmount());
        sourceUnitLoad.setWeightCalculated(
                sourceUnitLoad.getWeightCalculated().subtract(itemDataWeight));
        unitLoadRepository.save(sourceUnitLoad);
        //  生成盘亏记录
        StockUnitRecord stockUnitRecord = new StockUnitRecord();
        stockUnitRecord.setRecordCode(pendingAdjustRecord.getRecordCode());
        stockUnitRecord.setRecordTool(pendingAdjustRecord.getRecordTool());
        stockUnitRecord.setRecordType(pendingAdjustRecord.getRecordType());
        stockUnitRecord.setAmount(pendingAdjustRecord.getAmount());
        stockUnitRecord.setFromStockUnit(pendingStockUnit.getId());
        stockUnitRecord.setFromUnitLoad(sourceUnitLoad.getLabel());
        stockUnitRecord.setFromStorageLocation(source.getName());
        stockUnitRecord.setItemDataItemNo(pendingAdjustRecord.getItemNo());
        stockUnitRecord.setItemDataSKU(pendingAdjustRecord.getSku());
        stockUnitRecord.setFromState(pendingAdjustRecord.getFromState());
        stockUnitRecord.setToState(pendingAdjustRecord.getFromState());
        if (pendingStockUnit.getLotId() != null) {
            stockUnitRecord.setLot(pendingStockUnit.getLotId());
        }
        stockUnitRecord.setOperator(SecurityUtils.getCurrentUsername());
        stockUnitRecord.setClientId(pendingAdjustRecord.getClient().getId());
        stockUnitRecord.setWarehouseId(pendingAdjustRecord.getWarehouseId());
        stockUnitRecordRepository.save(stockUnitRecord);
    }


    public void overageGoods(PendingAdjustRecord pendingAdjustRecord) {
        StorageLocation destination = storageLocationRepository.getByName(applicationContext.getCurrentWarehouse(),pendingAdjustRecord.getToStorageLocation());
        ItemData itemData = itemDataRepository.getByItemCode(applicationContext.getCurrentWarehouse(), applicationContext.getCurrentClient(),pendingAdjustRecord.getItemNo());
        StockUnit pendingStockUnit = stockUnitRepository.retrieve(pendingAdjustRecord.getStockUnitId());

        // StockUnit 更新state(Adjust--->Inventory)
        pendingStockUnit.setState("Inventory");
        stockUnitRepository.save(pendingStockUnit);

        // 更新目的容器的重量
        UnitLoad destinationUnitLoad = unitLoadRepository.getByStorageLocation(destination);
        BigDecimal itemDataWeight = itemData.getWeight().multiply(pendingAdjustRecord.getAmount());
        destinationUnitLoad.setWeightCalculated(
                destinationUnitLoad.getWeightCalculated().add(itemDataWeight));
        unitLoadRepository.save(destinationUnitLoad);
        // 生成盘亏记录
        StockUnitRecord stockUnitRecord = new StockUnitRecord();
        stockUnitRecord.setRecordCode(pendingAdjustRecord.getRecordCode());
        stockUnitRecord.setRecordTool(pendingAdjustRecord.getRecordTool());
        stockUnitRecord.setRecordType(pendingAdjustRecord.getRecordType());
        stockUnitRecord.setAmount(pendingAdjustRecord.getAmount());
        stockUnitRecord.setItemDataItemNo(pendingAdjustRecord.getItemNo());
        stockUnitRecord.setItemDataSKU(pendingAdjustRecord.getSku());
        stockUnitRecord.setToState(pendingAdjustRecord.getFromState());

        stockUnitRecord.setToStockUnit(pendingStockUnit.getId());
        stockUnitRecord.setToUnitLoad(destinationUnitLoad.getLabel());
        stockUnitRecord.setToStorageLoaction(destination.getName());
        if (pendingStockUnit.getLotId() != null) {
            stockUnitRecord.setLot(pendingStockUnit.getLotId());
        }
        stockUnitRecord.setOperator(SecurityUtils.getCurrentUsername());
        stockUnitRecord.setClientId(pendingStockUnit.getClientId());
        stockUnitRecord.setWarehouseId(pendingStockUnit.getWarehouseId());
        stockUnitRecordRepository.save(stockUnitRecord);

    }








}
