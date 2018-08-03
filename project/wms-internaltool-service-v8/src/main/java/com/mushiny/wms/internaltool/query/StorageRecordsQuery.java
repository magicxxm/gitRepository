package com.mushiny.wms.internaltool.query;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.internaltool.common.domain.*;
import com.mushiny.wms.internaltool.common.repository.*;
import com.mushiny.wms.internaltool.exception.InternalToolException;
import com.mushiny.wms.internaltool.web.dto.StorageInventoryRecordDTO;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class StorageRecordsQuery {
    org.slf4j.Logger log = LoggerFactory.getLogger(StorageRecordsQuery.class);

    private final ApplicationContext applicationContext;
    private final StorageLocationRepository storageLocationRepository;
    private final UnitLoadRepository unitLoadRepository;
    private final StockUnitRepository stockUnitRepository;
    private final PickingOrderPositionRepository pickingOrderPositionRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public StorageRecordsQuery(ApplicationContext applicationContext,
                               StorageLocationRepository storageLocationRepository,
                               UnitLoadRepository unitLoadRepository,
                               StockUnitRepository stockUnitRepository,
                               PickingOrderPositionRepository pickingOrderPositionRepository,
                               ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.storageLocationRepository = storageLocationRepository;
        this.unitLoadRepository = unitLoadRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.pickingOrderPositionRepository = pickingOrderPositionRepository;
        this.clientRepository = clientRepository;
    }

    public List<StorageInventoryRecordDTO> getStorageRecords(String storageLocationName) {
        log.info("查询的容器是："+storageLocationName);
        List<StorageInventoryRecordDTO> records = new ArrayList<>();
        StorageLocation storageLocation = storageLocationRepository
                .getByName(applicationContext.getCurrentWarehouse(), storageLocationName);
        if (storageLocation == null) {
            //return records;
            throw new ApiException(InternalToolException.EX_IT_STORAGE_LOCATION_NOT_FOUND.getName(),storageLocationName);
        }

        //如果容器是Packed，，Problem Solving，，Problem Solved时， storageLocation对应多个unitLoad，要查所有的unitLoad的库存信息
       /* if("Packed".equals(storageLocationName) || "Problem Solving".equals(storageLocationName) || "Problem Solved".equals(storageLocationName)){
            List<UnitLoad> unitLoads = unitLoadRepository.getByStorageLocationName(storageLocation);
        }*/

        List<UnitLoad> unitLoads = unitLoadRepository.getByStorageLocationName(storageLocation);
        if (unitLoads.size() == 0) {
            return records;
        }
        for (UnitLoad unitLoad:unitLoads) {
            // 获取容器中所有商品库存
            List<StockUnit> stockUnits = stockUnitRepository.getByUnitLoad(unitLoad);
            BigDecimal amount = stockUnitRepository.sumByUnitLoad(unitLoad);
            if (stockUnits.isEmpty()
                    || amount.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
                //return records;
            }
            if (unitLoad.getShipments().isEmpty()) {
                records.addAll(getUseForStorageRecords(stockUnits, storageLocationName));
            } else {
                records.addAll(getUseForShipmentRecords(unitLoad, stockUnits));
            }
        }
        return records;
    }

    /**
     * 获取货位上的库存，这里是通用的库存，可以被任何Shipment锁定捡货
     */
    @SuppressWarnings("Duplicates")
    private List<StorageInventoryRecordDTO> getUseForStorageRecords(List<StockUnit> stockUnits,
                                                                    String storageLocationName) {
        List<StorageInventoryRecordDTO> records = new ArrayList<>();
        for (StockUnit stockUnit : stockUnits) {
            boolean useFlag = true;
            BigDecimal amount = stockUnit.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            // 检查库存是否存在被锁定的商品
            if (stockUnit.getReservedAmount() != null
                    && stockUnit.getReservedAmount().compareTo(BigDecimal.ZERO) > 0) {
                amount = amount.subtract(stockUnit.getReservedAmount());
                // 存放具体订单下锁定商品的总数量
                Map<String, BigDecimal> recordMap = new HashMap<>();
                // 按shipment区分锁定的商品数量
                List<PickingOrderPosition> orderPositions = pickingOrderPositionRepository.getByStockUnit(stockUnit);
                if (!orderPositions.isEmpty()) {
                    useFlag = false;
                    for (PickingOrderPosition orderPosition : orderPositions) {
                        String shipmentNo = orderPosition.getCustomerShipmentPosition()
                                .getCustomerShipment().getShipmentNo();
                        BigDecimal orderAmount = orderPosition.getAmount()
                                .subtract(orderPosition.getAmountPicked());
                        BigDecimal recordAmount = recordMap.get(shipmentNo);
                        if (recordAmount == null) {
                            recordAmount = orderAmount;
                        } else {
                            recordAmount = recordAmount.add(orderAmount);
                        }
                        recordMap.put(shipmentNo, recordAmount);
                    }
                }
                if (!useFlag) {
                    for (String shipmentNo : recordMap.keySet()) {
                        StorageInventoryRecordDTO record = new StorageInventoryRecordDTO();
                        record.setStorageLocationName(storageLocationName);
                        record.setItemNo(stockUnit.getItemData().getItemNo());
                        record.setSku(stockUnit.getItemData().getSkuNo());
                        record.setItemUnitName(stockUnit.getItemData().getItemUnit().getName());
                        record.setItemDataName(stockUnit.getItemData().getName());
                        record.setAmount(recordMap.get(shipmentNo));
                        record.setInventoryState(stockUnit.getState());
                        record.setShipmentNo(shipmentNo);
                        Client client = clientRepository.retrieve(stockUnit.getClientId());
                        record.setClientName(client.getName());
                        records.add(record);
                    }
                }
            }
            // 处理没有被锁定的商品数量
            if (useFlag || amount.compareTo(BigDecimal.ZERO) == 1) {
                StorageInventoryRecordDTO record = new StorageInventoryRecordDTO();
                record.setStorageLocationName(storageLocationName);
                record.setItemNo(stockUnit.getItemData().getItemNo());
                record.setSku(stockUnit.getItemData().getSkuNo());
                record.setItemUnitName(stockUnit.getItemData().getItemUnit().getName());
                record.setItemDataName(stockUnit.getItemData().getName());
                record.setAmount(amount);
                record.setInventoryState(stockUnit.getState());
                Client client = clientRepository.retrieve(stockUnit.getClientId());
                record.setClientName(client.getName());
                records.add(record);
            }
        }
        return records;
    }

    /**
     * 获取已经被shipment绑定容器的数量
     */
    @SuppressWarnings("Duplicates")
    private List<StorageInventoryRecordDTO> getUseForShipmentRecords(UnitLoad unitLoad,
                                                                     List<StockUnit> stockUnits) {
        // StockUnit按照同种商品进行合并操作
        List<StockUnit> shipmentStockUnits = new ArrayList<>();
        for (StockUnit stockUnit : stockUnits) {
            BigDecimal amount = stockUnit.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            StockUnit shipmentStockUnit = shipmentStockUnits.stream()
                    .filter(entity -> entity.getItemData().getId().equals(stockUnit.getItemData().getId()))
                    .findFirst()
                    .orElse(null);
            if (shipmentStockUnit == null) {
                shipmentStockUnits.add(stockUnit);
            } else {
                shipmentStockUnit.setAmount(shipmentStockUnit.getAmount().add(stockUnit.getAmount()));
            }
        }
        // 按Shipment处理
        List<StorageInventoryRecordDTO> records = new ArrayList<>();
        for (StockUnit shipmentStockUnit : shipmentStockUnits) {
            BigDecimal amount = shipmentStockUnit.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            ItemData itemData = shipmentStockUnit.getItemData();
            // 筛选包含此商品的shipment
            for (CustomerShipment shipment : unitLoad.getShipments()) {
                CustomerShipmentPosition shipmentPosition = shipment.getPositions().stream()
                        .filter(entity -> entity.getItemData().getId().equals(itemData.getId()))
                        .findFirst()
                        .orElse(null);
                if (shipmentPosition != null) {
                    StorageInventoryRecordDTO record = new StorageInventoryRecordDTO();
                    record.setStorageLocationName(unitLoad.getStorageLocation().getName());
                    record.setItemNo(itemData.getItemNo());
                    record.setSku(itemData.getSkuNo());
                    record.setItemUnitName(itemData.getItemUnit().getName());
                    record.setItemDataName(itemData.getName());
                    record.setInventoryState(shipmentStockUnit.getState());
                    record.setShipmentNo(shipment.getShipmentNo());
                    Client client = clientRepository.retrieve(shipmentStockUnit.getClientId());
                    record.setClientName(client.getName());
                    records.add(record);
                    if (shipmentStockUnit.getAmount().compareTo(shipmentPosition.getAmount()) > 0) {
                        record.setAmount(shipmentPosition.getAmount());
                        shipmentStockUnit.setAmount(shipmentStockUnit.getAmount().subtract(shipmentPosition.getAmount()));
                    } else {
                        record.setAmount(shipmentStockUnit.getAmount());
                        shipmentStockUnit.setAmount(BigDecimal.ZERO);
                        break;
                    }
                }
            }
        }
        // 处理没有shipment的商品数量
        for (StockUnit shipmentStockUnit : shipmentStockUnits) {
            if (shipmentStockUnit.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            StorageInventoryRecordDTO record = new StorageInventoryRecordDTO();
            record.setStorageLocationName(unitLoad.getStorageLocation().getName());
            record.setItemNo(shipmentStockUnit.getItemData().getItemNo());
            record.setSku(shipmentStockUnit.getItemData().getSkuNo());
            record.setItemUnitName(shipmentStockUnit.getItemData().getItemUnit().getName());
            record.setItemDataName(shipmentStockUnit.getItemData().getName());
            record.setAmount(shipmentStockUnit.getAmount());
            record.setInventoryState(shipmentStockUnit.getState());
            Client client = clientRepository.retrieve(shipmentStockUnit.getClientId());
            record.setClientName(client.getName());
            records.add(record);
        }
        return records;
    }
}
