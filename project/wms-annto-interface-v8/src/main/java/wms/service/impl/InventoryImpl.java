package wms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.repository.common.*;
import wms.service.Inventory;
import wms.common.context.ApplicationContext;
import wms.common.crud.InventoryAccessDTO;
import wms.crud.common.dto.ItemInventoryRecordDTO;
import wms.domain.ItemData;
import wms.domain.common.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryImpl implements Inventory {

    private final ApplicationContext applicationContext;
    private final StockUnitRepository stockUnitRepository;
    private final WarehouseRepository warehouseRepository;
    private final ClientRepository clientRepository;
    private final PickingOrderPositionRepository pickingOrderPositionRepository;
    private final LotRepository lotRepository;

    @Autowired
    public InventoryImpl(ApplicationContext applicationContext,
                         StockUnitRepository stockUnitRepository,
                         WarehouseRepository warehouseRepository,
                         ClientRepository clientRepository,
                         PickingOrderPositionRepository pickingOrderPositionRepository,
                         LotRepository lotRepository) {
        this.applicationContext = applicationContext;
        this.stockUnitRepository = stockUnitRepository;
        this.warehouseRepository = warehouseRepository;
        this.clientRepository = clientRepository;
        this.pickingOrderPositionRepository = pickingOrderPositionRepository;
        this.lotRepository = lotRepository;
    }

    @Override
    public InventoryAccessDTO getInventory(String warehouseCode, String itemCode) {
        InventoryAccessDTO records = new InventoryAccessDTO();
        // 获取商品所有库存
        String warehouseNo = warehouseCode;
        String warehouseId = warehouseRepository.getByWarehouseNo(warehouseNo).getId();

        List<StockUnit> allItemStockUnits = stockUnitRepository.getBySKU(itemCode, warehouseId);
        BigDecimal amount = stockUnitRepository.sumBySKU(itemCode, warehouseId);
        if (allItemStockUnits.isEmpty()
                || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return records;
        }
        // 按容器分组商品
        Map<String, List<StockUnit>> stockUnitMap = allItemStockUnits.stream().collect(
                Collectors.groupingBy(entity -> entity.getUnitLoad().getId()));
        // 按UnitLoad处理库存
        List<ItemInventoryRecordDTO> itemList = new ArrayList<>();
        for (String key : stockUnitMap.keySet()) {//获取map中key的集合
            List<StockUnit> stockUnits = stockUnitMap.get(key);
            if (stockUnits.isEmpty()) {
                continue;
            }
            UnitLoad unitLoad = stockUnits.get(0).getUnitLoad();
            if (unitLoad.getShipments().isEmpty()) {
                List<ItemInventoryRecordDTO> list = getUseForStorageRecords(unitLoad, stockUnits);
                if (list.size() > 0) {
                    for (ItemInventoryRecordDTO item : list) {
                        itemList.add(item);
                    }
                }
//                records.setData(getUseForStorageRecords(unitLoad, stockUnits));
            } else {
                List<ItemInventoryRecordDTO> list = getUseForShipmentRecords(unitLoad, stockUnits);
                if (list.size() > 0) {
                    for (ItemInventoryRecordDTO item : list) {
                        itemList.add(item);
                    }
                }
//                records.setData(getUseForShipmentRecords(unitLoad, stockUnits));
            }
        }
//        records.setData(itemList);
//        records.setCount(String.valueOf(records.getData().size()));
        return records;
    }

    /**
     * 获取货位上的库存，这里是通用的库存，可以被任何Shipment锁定捡货
     */
    @SuppressWarnings("Duplicates")
    private List<ItemInventoryRecordDTO> getUseForStorageRecords(UnitLoad unitLoad,
                                                                 List<StockUnit> stockUnits) {
        List<ItemInventoryRecordDTO> records = new ArrayList<>();
        for (StockUnit stockUnit : stockUnits) {
            boolean useFlag = true;
            BigDecimal amount = stockUnit.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            // 检查库存是否存在被锁定的商品
            if (stockUnit.getReservedAmount() != null
                    && stockUnit.getReservedAmount().compareTo(BigDecimal.ZERO) > 0) {
                amount = amount.subtract(stockUnit.getReservedAmount());//如果存在被锁定数量，则库存数量要减去锁定的数量
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
                        recordMap.put(shipmentNo, recordAmount);//
                    }
                }
                if (!useFlag) {
                    for (String shipmentNo : recordMap.keySet()) {
                        ItemInventoryRecordDTO record = new ItemInventoryRecordDTO();
                        record.setStorageLocationName(unitLoad.getStorageLocation().getName());
                        record.setItemNo(stockUnit.getItemData().getItemNo());
                        record.setSku(stockUnit.getItemData().getSkuNo());
                        record.setItemUnitName(stockUnit.getItemData().getItemUnit().getName());
                        record.setItemDataName(stockUnit.getItemData().getName());
                        record.setAmount(recordMap.get(shipmentNo));
                        record.setInventoryState(stockUnit.getState());
                        record.setShipmentNo(shipmentNo);
                        Client client = clientRepository.retrieve(stockUnit.getClientId());
                        record.setClientName(client.getName());
                        if (stockUnit.getLotId() != null) {
                            Lot lot = lotRepository.findOne(stockUnit.getLotId());
                            record.setUseNotAfter(lot.getUseNotAfter());
                        }
                        records.add(record);
                    }
                }
            }
            // 处理没有被锁定的商品数量
            if (useFlag || amount.compareTo(BigDecimal.ZERO) == 1) {
                ItemInventoryRecordDTO record = new ItemInventoryRecordDTO();
                record.setStorageLocationName(unitLoad.getStorageLocation().getName());
                record.setItemNo(stockUnit.getItemData().getItemNo());
                record.setSku(stockUnit.getItemData().getSkuNo());
                record.setItemUnitName(stockUnit.getItemData().getItemUnit().getName());
                record.setItemDataName(stockUnit.getItemData().getName());
                record.setAmount(amount);
                record.setInventoryState(stockUnit.getState());
                Client client = clientRepository.retrieve(stockUnit.getClientId());
                record.setClientName(client.getName());
                if (stockUnit.getLotId() != null) {
                    Lot lot = lotRepository.findOne(stockUnit.getLotId());
                    record.setUseNotAfter(lot.getUseNotAfter());
                }
                records.add(record);
            }
        }
        return records;
    }

    /**
     * 获取已经被shipment绑定容器的数量
     */
    @SuppressWarnings("Duplicates")
    private List<ItemInventoryRecordDTO> getUseForShipmentRecords(UnitLoad unitLoad,
                                                                  List<StockUnit> stockUnits) {
        // StockUnit按照同种商品进行合并操作
      /*  List<StockUnit> shipmentStockUnits = new ArrayList<>();
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
        }*/
        // 按Shipment处理
        List<ItemInventoryRecordDTO> records = new ArrayList<>();
        for (StockUnit shipmentStockUnit : stockUnits) {
            BigDecimal amount = shipmentStockUnit.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            ItemData itemData = shipmentStockUnit.getItemData();
            // 筛选包含此商品的shipment
            List<CustomerShipment> shipments = unitLoad.getShipments();
            for (CustomerShipment shipment : unitLoad.getShipments()) {
                CustomerShipmentPosition shipmentPosition = shipment.getPositions().stream()
                        .filter(entity -> entity.getItemData().getId().equals(itemData.getId()))
                        .findFirst()
                        .orElse(null);
                if (shipmentPosition != null) {
                    ItemInventoryRecordDTO record = new ItemInventoryRecordDTO();
                    record.setStorageLocationName(unitLoad.getStorageLocation().getName());
                    record.setItemNo(itemData.getItemNo());
                    record.setSku(itemData.getSkuNo());
                    record.setItemUnitName(itemData.getItemUnit().getName());
                    record.setItemDataName(itemData.getName());
                    record.setInventoryState(shipmentStockUnit.getState());
                    record.setShipmentNo(shipment.getShipmentNo());
                    Client client = clientRepository.retrieve(shipmentStockUnit.getClientId());
                    record.setClientName(client.getName());
                    if (shipmentStockUnit.getLotId() != null) {
                        Lot lot = lotRepository.findOne(shipmentStockUnit.getLotId());
                        record.setUseNotAfter(lot.getUseNotAfter());
                    }
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
        for (StockUnit shipmentStockUnit : stockUnits) {
            if (shipmentStockUnit.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            ItemInventoryRecordDTO record = new ItemInventoryRecordDTO();
            record.setStorageLocationName(unitLoad.getStorageLocation().getName());
            record.setItemNo(shipmentStockUnit.getItemData().getItemNo());
            record.setSku(shipmentStockUnit.getItemData().getSkuNo());
            record.setItemUnitName(shipmentStockUnit.getItemData().getItemUnit().getName());
            record.setItemDataName(shipmentStockUnit.getItemData().getName());
            record.setAmount(shipmentStockUnit.getAmount());
            record.setInventoryState(shipmentStockUnit.getState());
            Client client = clientRepository.retrieve(shipmentStockUnit.getClientId());
            record.setClientName(client.getName());
            if (shipmentStockUnit.getLotId() != null) {
                Lot lot = lotRepository.findOne(shipmentStockUnit.getLotId());
                record.setUseNotAfter(lot.getUseNotAfter());
            }
            records.add(record);
        }
        return records;
    }
}
