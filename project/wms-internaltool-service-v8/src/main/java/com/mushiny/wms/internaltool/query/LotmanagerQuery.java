package com.mushiny.wms.internaltool.query;

import com.mushiny.wms.internaltool.common.domain.*;
import com.mushiny.wms.internaltool.common.repository.ClientRepository;
import com.mushiny.wms.internaltool.common.repository.PickingOrderPositionRepository;
import com.mushiny.wms.internaltool.common.repository.StockUnitRepository;
import com.mushiny.wms.internaltool.web.dto.LotManagerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 123 on 2017/11/10.
 */
@Component
public class LotmanagerQuery {

    private final StockUnitRepository stockUnitRepository;
    private final PickingOrderPositionRepository pickingOrderPositionRepository;
    private final ClientRepository clientRepository;
    private final EntityManager entityManager;

    @Autowired
    public LotmanagerQuery(StockUnitRepository stockUnitRepository,
                          PickingOrderPositionRepository pickingOrderPositionRepository,
                          ClientRepository clientRepository,
                           EntityManager entityManager){
        this.stockUnitRepository = stockUnitRepository;
        this.clientRepository = clientRepository;
        this.pickingOrderPositionRepository = pickingOrderPositionRepository;
        this.entityManager = entityManager;
    }

    public List<LotManagerDTO> getStockInfo(String warehouseId){
        List<LotManagerDTO> dtoList = new ArrayList<>();

        // 获取所有商品所有库存
        List<StockUnit> allItemStockUnits = stockUnitRepository.getByWarehouse(warehouseId);

        if (allItemStockUnits.isEmpty()) {
            return dtoList;
        }
        //按商品有效期分组库存
        Map<String,List<StockUnit>> stockUnitMaps = allItemStockUnits.stream().collect(
                Collectors.groupingBy(entity -> entity.getLot().getId()));

        for (String key:stockUnitMaps.keySet()) {
            List<LotManagerDTO> records = new ArrayList<>();
            List<StockUnit> stockUnitList = stockUnitMaps.get(key);
            // 按容器分组商品
            Map<String, List<StockUnit>> stockUnitMap = stockUnitList.stream().collect(
                    Collectors.groupingBy(entity -> entity.getUnitLoad().getId()));
            // 按UnitLoad处理库存
            for (String unit : stockUnitMap.keySet()) {
                List<StockUnit> stockUnits = stockUnitMap.get(unit);
                if (stockUnits.isEmpty()) {
                    continue;
                }
                UnitLoad unitLoad = stockUnits.get(0).getUnitLoad();
                if (unitLoad.getShipments().isEmpty()) {
                    records.addAll(getUseForStorageRecords(unitLoad, stockUnits));
                } else {
                    records.addAll(getUseForShipmentRecords(unitLoad, stockUnits));
                }
            }
//            stockDTOS = getResult(records);
            //汇总所有结果
            for (LotManagerDTO s:records) {
                dtoList.add(s);
            }
        }

        //按有效期升序排列
        List<LotManagerDTO> list = dtoList.stream().sorted(Comparator.comparingLong(LotManagerDTO::getDays)).collect(Collectors.toList());
        return  list;
    }

    //条件查询
    public List<LotManagerDTO> getByParamAndWarehouse(String warehouse, String param) {
        List<LotManagerDTO> dtoList = new ArrayList<>();

        // 获取所有商品所有库存
        List<StockUnit> allItemStockUnits = getByWarehouseAndParam(warehouse,param);

        if (allItemStockUnits.isEmpty()) {
            return dtoList;
        }
        //按商品有效期分组库存
        Map<String,List<StockUnit>> stockUnitMaps = allItemStockUnits.stream().collect(
                Collectors.groupingBy(entity -> entity.getLot().getId()));

        for (String key:stockUnitMaps.keySet()) {
            List<LotManagerDTO> records = new ArrayList<>();
            List<StockUnit> stockUnitList = stockUnitMaps.get(key);
            // 按容器分组商品
            Map<String, List<StockUnit>> stockUnitMap = stockUnitList.stream().collect(
                    Collectors.groupingBy(entity -> entity.getUnitLoad().getId()));
            // 按UnitLoad处理库存
            for (String unit : stockUnitMap.keySet()) {
                List<StockUnit> stockUnits = stockUnitMap.get(unit);
                if (stockUnits.isEmpty()) {
                    continue;
                }
                UnitLoad unitLoad = stockUnits.get(0).getUnitLoad();
                if (unitLoad.getShipments().isEmpty()) {
                    records.addAll(getUseForStorageRecords(unitLoad, stockUnits));
                } else {
                    records.addAll(getUseForShipmentRecords(unitLoad, stockUnits));
                }
            }
//            stockDTOS = getResult(records);
            //汇总所有结果
            for (LotManagerDTO s:records) {
                dtoList.add(s);
            }
        }

        //按有效期升序排列
        List<LotManagerDTO> list = dtoList.stream().sorted(Comparator.comparingLong(LotManagerDTO::getDays)).collect(Collectors.toList());
        return  list;
    }

    private List<StockUnit> getByWarehouseAndParam(String warehouse, String param) {

        Client client = clientRepository.getByClientName(param);
        String clientId = "";
        if(client != null){
            clientId = client.getId();
        }

        Query query = entityManager.createQuery("SELECT s FROM "
                + StockUnit.class.getSimpleName() +
                " s, "
                + ItemData.class.getSimpleName() +
                " i " +
                " WHERE s.itemData = i " +
                " AND (i.itemNo = :itemNo" +
                " OR i.skuNo = :skuNo" +
                " OR i.name like :itemName" +
                " OR s.unitLoad.storageLocation.name = :storageName " +
                " OR s.state = :state" +
                " OR s.clientId = :clientId)" +
                " AND s.lot is not null ");

        query.setParameter("itemNo",param);
        query.setParameter("skuNo",param);
        query.setParameter("itemName","%"+param+"%");
        query.setParameter("storageName",param);
        query.setParameter("state",param);
        query.setParameter("clientId",clientId);

        List<StockUnit> list = query.getResultList();
        return list;
    }

    /**
     * 获取货位上的库存，这里是通用的库存，可以被任何Shipment锁定捡货
     */
    @SuppressWarnings("Duplicates")
    private List<LotManagerDTO> getUseForStorageRecords(UnitLoad unitLoad,
                                                                 List<StockUnit> stockUnits) {
        List<LotManagerDTO> records = new ArrayList<>();
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
                        if (orderPosition.getCustomerShipmentPosition()!=null) {
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
                }
                if (!useFlag) {
                    for (String shipmentNo : recordMap.keySet()) {
                        LotManagerDTO record = new LotManagerDTO();
                        if(unitLoad.getStorageLocation()!=null)
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
                        if (stockUnit.getLot() != null) {
                            record.setUseNotAfter(stockUnit.getLot().getUseNotAfter());
                            record.setDays(getdays(stockUnit.getLot().getUseNotAfter()));
                        }
                        records.add(record);
                    }
                }
            }
            // 处理没有被锁定的商品数量
            if (useFlag || amount.compareTo(BigDecimal.ZERO) == 1) {
                LotManagerDTO record = new LotManagerDTO();
                if(unitLoad.getStorageLocation()!=null)
                   record.setStorageLocationName(unitLoad.getStorageLocation().getName());
                record.setItemNo(stockUnit.getItemData().getItemNo());
                record.setSku(stockUnit.getItemData().getSkuNo());
                record.setItemUnitName(stockUnit.getItemData().getItemUnit().getName());
                record.setItemDataName(stockUnit.getItemData().getName());
                record.setAmount(amount);
                record.setInventoryState(stockUnit.getState());
                Client client = clientRepository.retrieve(stockUnit.getClientId());
                record.setClientName(client.getName());
                if (stockUnit.getLot() != null) {
                    record.setUseNotAfter(stockUnit.getLot().getUseNotAfter());
                    record.setDays(getdays(stockUnit.getLot().getUseNotAfter()));
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
    private List<LotManagerDTO> getUseForShipmentRecords(UnitLoad unitLoad,
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
        List<LotManagerDTO> records = new ArrayList<>();
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
                    LotManagerDTO record = new LotManagerDTO();
                    if(unitLoad.getStorageLocation()!=null)
                       record.setStorageLocationName(unitLoad.getStorageLocation().getName());
                    record.setItemNo(itemData.getItemNo());
                    record.setSku(itemData.getSkuNo());
                    record.setItemUnitName(itemData.getItemUnit().getName());
                    record.setItemDataName(itemData.getName());
                    record.setInventoryState(shipmentStockUnit.getState());
                    record.setShipmentNo(shipment.getShipmentNo());
                    Client client = clientRepository.retrieve(shipmentStockUnit.getClientId());
                    record.setClientName(client.getName());
                    if (shipmentStockUnit.getLot() != null) {
                        record.setUseNotAfter(shipmentStockUnit.getLot().getUseNotAfter());
                        record.setDays(getdays(shipmentStockUnit.getLot().getUseNotAfter()));
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
        for (StockUnit shipmentStockUnit : shipmentStockUnits) {
            if (shipmentStockUnit.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            LotManagerDTO record = new LotManagerDTO();
            if(unitLoad.getStorageLocation()!=null)
               record.setStorageLocationName(unitLoad.getStorageLocation().getName());
            record.setItemNo(shipmentStockUnit.getItemData().getItemNo());
            record.setSku(shipmentStockUnit.getItemData().getSkuNo());
            record.setItemUnitName(shipmentStockUnit.getItemData().getItemUnit().getName());
            record.setItemDataName(shipmentStockUnit.getItemData().getName());
            record.setAmount(shipmentStockUnit.getAmount());
            record.setInventoryState(shipmentStockUnit.getState());
            Client client = clientRepository.retrieve(shipmentStockUnit.getClientId());
            record.setClientName(client.getName());
            if (shipmentStockUnit.getLot() != null) {
                record.setUseNotAfter(shipmentStockUnit.getLot().getUseNotAfter());
                record.setDays(getdays(shipmentStockUnit.getLot().getUseNotAfter()));
            }
            records.add(record);
        }
        return records;
    }

    private long getdays(LocalDate date){
        long days = 0;
        //获取当前时间
        LocalDate currentDate = LocalDate.now();
        days = currentDate.until(date, ChronoUnit.DAYS);
        return days;
    }


    public Page<LotManagerDTO> getStock(String warehouseId, Pageable pageable){
        String sql = "SELECT MD_ITEMDATA.NAME as itemName, MD_ITEMDATA.ITEM_NO as itemNo,MD_ITEMDATA.SKU_NO as sku, INV_STOCKUNIT.STATE as stockState, " +
                "INV_STOCKUNIT.AMOUNT as stockAmount, INV_LOT.USE_NOT_AFTER as useNotAfter ,SYS_WAREHOUSE.WAREHOUSE_NO as warehouseNo, " +
                "SYS_CLIENT.CLIENT_NO as clientNo,MD_STORAGELOCATION.name as storageName " +
                "FROM INV_STOCKUNIT INNER JOIN  MD_ITEMDATA " +
                "on INV_STOCKUNIT.ITEMDATA_ID=MD_ITEMDATA.ID " +
                "INNER JOIN INV_UNITLOAD on INV_STOCKUNIT.UNITLOAD_ID=INV_UNITLOAD.ID " +
                "INNER JOIN MD_STORAGELOCATION on INV_UNITLOAD.STORAGELOCATION_ID=MD_STORAGELOCATION.ID " +
                "LEFT JOIN  INV_LOT on INV_STOCKUNIT.LOT_ID=INV_LOT.ID " +
                "LEFT JOIN SYS_WAREHOUSE on INV_STOCKUNIT.WAREHOUSE_ID=SYS_WAREHOUSE.ID " +
                "LEFT JOIN SYS_CLIENT on INV_STOCKUNIT.CLIENT_ID=SYS_CLIENT.ID " +
                "where MD_ITEMDATA.ID in (SELECT DISTINCT MD_ITEMDATA.ID from MD_ITEMDATA) and INV_STOCKUNIT.ENTITY_LOCK<>2 " +
                "and INV_STOCKUNIT.AMOUNT<>0 and MD_ITEMDATA.LOT_MANDATORY=1 and INV_STOCKUNIT.WAREHOUSE_ID=:warehouseId "+
                "GROUP BY itemName,itemNo,sku,stockState,stockAmount,useNotAfter,warehouseNo,clientNo,storageName order by itemNo";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("warehouseId",warehouseId);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<Object[]> list = query.getResultList();
        List<LotManagerDTO> lotManagerDTOS=new ArrayList<>();
        String sql2="SELECT COUNT(*) FROM (select DISTINCT MD_ITEMDATA.ITEM_NO,MD_STORAGELOCATION.NAME " +
                "FROM INV_STOCKUNIT INNER JOIN  MD_ITEMDATA " +
                "on INV_STOCKUNIT.ITEMDATA_ID=MD_ITEMDATA.ID " +
                "INNER JOIN INV_UNITLOAD on INV_STOCKUNIT.UNITLOAD_ID=INV_UNITLOAD.ID " +
                "INNER JOIN MD_STORAGELOCATION on INV_UNITLOAD.STORAGELOCATION_ID=MD_STORAGELOCATION.ID " +
                "LEFT JOIN SYS_WAREHOUSE on INV_STOCKUNIT.WAREHOUSE_ID=SYS_WAREHOUSE.ID " +
                "LEFT JOIN SYS_CLIENT on INV_STOCKUNIT.CLIENT_ID=SYS_CLIENT.ID " +
                "where MD_ITEMDATA.ID in (SELECT DISTINCT MD_ITEMDATA.ID from MD_ITEMDATA) " +
                "and INV_STOCKUNIT.ENTITY_LOCK<>2 " +
                "and INV_STOCKUNIT.AMOUNT<>0 and MD_ITEMDATA.LOT_MANDATORY=1 and " +
                "INV_STOCKUNIT.WAREHOUSE_ID=:warehouseId) A";
        Query query2 = entityManager.createNativeQuery(sql2);
        query2.setParameter("warehouseId",warehouseId);
        //总数
        long count=Long.valueOf(String.valueOf(query2.getSingleResult()));
        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(Object[] obj:list){
            LotManagerDTO lotManagerDTO=new LotManagerDTO();
            lotManagerDTO.setItemDataName(obj[0].toString());
            lotManagerDTO.setItemNo(obj[1].toString());
            lotManagerDTO.setSku(obj[2].toString());
            lotManagerDTO.setInventoryState(String.valueOf(obj[3]));
            lotManagerDTO.setAmount(new BigDecimal(String.valueOf(obj[4])));
            lotManagerDTO.setClientName(obj[7].toString());
            if(obj[5]!=null && !obj[5].equals("")) {
                lotManagerDTO.setUseNotAfter(LocalDate.parse(obj[5].toString(), df));
                lotManagerDTO.setDays(getdays(lotManagerDTO.getUseNotAfter()));
            }
            lotManagerDTO.setStorageLocationName(obj[8].toString());
            lotManagerDTOS.add(lotManagerDTO);
        }
        return new PageImpl<LotManagerDTO>(lotManagerDTOS,pageable,count);
    }
}
