package com.mushiny.wms.internaltool.query;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.internaltool.common.domain.*;
import com.mushiny.wms.internaltool.common.enums.StockUnitState;
import com.mushiny.wms.internaltool.common.repository.ClientRepository;
import com.mushiny.wms.internaltool.common.repository.ItemDataRepository;
import com.mushiny.wms.internaltool.common.repository.PickingOrderPositionRepository;
import com.mushiny.wms.internaltool.common.repository.StockUnitRepository;
import com.mushiny.wms.internaltool.web.dto.ItemInventoryRecordDTO;
import com.mushiny.wms.internaltool.web.dto.StockDTO;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by 123 on 2017/11/8.
 */
@Component
public class StockUnitQuery {

    private final StockUnitRepository stockUnitRepository;
    private final PickingOrderPositionRepository pickingOrderPositionRepository;
    private final ClientRepository clientRepository;
    private final ApplicationContext applicationContext;
    private final ItemDataRepository itemDataRepository;
    private final static Logger log= LoggerFactory.getLogger(StockUnitQuery.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    public StockUnitQuery(StockUnitRepository stockUnitRepository,
                          PickingOrderPositionRepository pickingOrderPositionRepository,
                          ClientRepository clientRepository,
                          ApplicationContext applicationContext,
                          ItemDataRepository itemDataRepository){
        this.stockUnitRepository = stockUnitRepository;
        this.clientRepository = clientRepository;
        this.pickingOrderPositionRepository = pickingOrderPositionRepository;
        this.applicationContext = applicationContext;
        this.itemDataRepository = itemDataRepository;
    }

    public List<StockDTO> getStockInfo(String warehouseId){
        List<StockDTO> dtoList = new ArrayList<>();

        // 获取所有商品所有库存
        List<StockUnit> allItemStockUnits = stockUnitRepository.getBySKU(warehouseId);

        if (allItemStockUnits.isEmpty()) {
            return dtoList;
        }
        //按商品分组库存
        Map<String,List<StockUnit>> stockUnitMaps = allItemStockUnits.stream().collect(
                Collectors.groupingBy(entity -> entity.getItemData().getId()));
        for (String key:stockUnitMaps.keySet()) {
            List<StockDTO> stockDTOS = new ArrayList<>();
            List<ItemInventoryRecordDTO> records = new ArrayList<>();
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
            stockDTOS = getResult(records);
            //汇总所有结果
            for (StockDTO s:stockDTOS) {
                dtoList.add(s);
            }
        }
        return dtoList;
    }

    public List<StockDTO> getResult(List<ItemInventoryRecordDTO> records) {
        List<StockDTO> stockDTOS = new ArrayList<>();
        if(records.isEmpty()){
            return stockDTOS;
        }
        //根据客户分组商品
        Map<String,List<ItemInventoryRecordDTO>> stockMap = records.stream().collect(
                Collectors.groupingBy(record -> record.getClientName()));
        for (String key:stockMap.keySet()) {
            List<ItemInventoryRecordDTO> stockList = stockMap.get(key);
            StockDTO dto = new StockDTO();
            BigDecimal amountUse = BigDecimal.ZERO;
            BigDecimal amountReserve = BigDecimal.ZERO;
            BigDecimal amountDamage = BigDecimal.ZERO;
            BigDecimal amountPending = BigDecimal.ZERO;
            BigDecimal amountTotal = null;
            for (ItemInventoryRecordDTO r:stockList) {
                if(Constant.DAMAGE.equalsIgnoreCase(r.getInventoryState())){
                    amountDamage = amountDamage.add(r.getAmount());
                    continue;
                }
                if(Constant.PENDING.equalsIgnoreCase(r.getInventoryState())){
                    amountPending = amountPending.add(r.getAmount());
                    continue;
                }
                if(!"".equals(r.getShipmentNo()) && r.getShipmentNo() != null){
                    amountReserve = amountReserve.add(r.getAmount());
                    continue;
                }
                amountUse = amountUse.add(r.getAmount());
            }
            amountTotal = amountDamage.add(amountPending).add(amountReserve).add(amountUse);
            dto.setItemNo(stockList.get(0).getItemNo());
            dto.setSkuNo(stockList.get(0).getSku());
            dto.setName(stockList.get(0).getItemDataName());
            dto.setClient(key);
            dto.setAmountDamage(amountDamage);
            dto.setAmountPending(amountPending);
            dto.setAmountReserve(amountReserve);
            dto.setAmountUse(amountUse);
            dto.setAmountTotal(amountTotal);

            stockDTOS.add(dto);
        }

        return stockDTOS;

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
                        ItemInventoryRecordDTO record = new ItemInventoryRecordDTO();
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
                        }
                        records.add(record);
                    }
                }
            }
            // 处理没有被锁定的商品数量
            if (useFlag || amount.compareTo(BigDecimal.ZERO) == 1) {
                ItemInventoryRecordDTO record = new ItemInventoryRecordDTO();
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
        List<ItemInventoryRecordDTO> records = new ArrayList<>();
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
                    ItemInventoryRecordDTO record = new ItemInventoryRecordDTO();
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
            ItemInventoryRecordDTO record = new ItemInventoryRecordDTO();
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
            }
            records.add(record);
        }
        return records;
    }

    public Page<StockDTO> getStockUnitInfo(String warehouseId, Pageable pageable) {
        String sql = "SELECT MD_ITEMDATA.ITEM_NO as itemNo,MD_ITEMDATA.SKU_NO as sku, MD_ITEMDATA.NAME as itemName,INV_STOCKUNIT.STATE as stockState, " +
                "sum(INV_STOCKUNIT.AMOUNT) as amount, sum(INV_STOCKUNIT.RESERVED_AMOUNT) as reservedAmount, " +
                "sum(INV_STOCKUNIT.AMOUNT-INV_STOCKUNIT.RESERVED_AMOUNT) as useAmount, " +
                "SYS_WAREHOUSE.WAREHOUSE_NO as warehouseNo,SYS_CLIENT.CLIENT_NO as clientNo " +
                "FROM INV_STOCKUNIT INNER JOIN  MD_ITEMDATA " +
                "on INV_STOCKUNIT.ITEMDATA_ID=MD_ITEMDATA.ID " +
                "LEFT JOIN SYS_WAREHOUSE on INV_STOCKUNIT.WAREHOUSE_ID=SYS_WAREHOUSE.ID " +
                "LEFT JOIN SYS_CLIENT on INV_STOCKUNIT.CLIENT_ID=SYS_CLIENT.ID " +
                "where MD_ITEMDATA.ID in (SELECT DISTINCT MD_ITEMDATA.ID from MD_ITEMDATA) " +
                "and INV_STOCKUNIT.ENTITY_LOCK<>2 and INV_STOCKUNIT.AMOUNT<>0 " +
                "and INV_STOCKUNIT.WAREHOUSE_ID=:warehouseId " +
                "GROUP BY itemNo,sku,itemName,stockState,warehouseNo,clientNo order by itemNo";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("warehouseId", warehouseId);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<Object[]> list = query.getResultList();
        //查询总数
        String sql2 = "SELECT COUNT(*) FROM " +
                "(select DISTINCT INV_STOCKUNIT.STATE,MD_ITEMDATA.ITEM_NO,SYS_WAREHOUSE.WAREHOUSE_NO,SYS_CLIENT.CLIENT_NO " +
                "FROM INV_STOCKUNIT INNER JOIN MD_ITEMDATA " +
                "on INV_STOCKUNIT.ITEMDATA_ID=MD_ITEMDATA.ID " +
                "LEFT JOIN SYS_WAREHOUSE on INV_STOCKUNIT.WAREHOUSE_ID=SYS_WAREHOUSE.ID " +
                "LEFT JOIN SYS_CLIENT on INV_STOCKUNIT.CLIENT_ID=SYS_CLIENT.ID " +
                "where MD_ITEMDATA.ID in (SELECT DISTINCT MD_ITEMDATA.ID from MD_ITEMDATA) " +
                "and INV_STOCKUNIT.ENTITY_LOCK<>2 and INV_STOCKUNIT.AMOUNT<>0 " +
                "and INV_STOCKUNIT.WAREHOUSE_ID=:warehouseId) A ";
        Query query2=entityManager.createNativeQuery(sql2);
        query2.setParameter("warehouseId",warehouseId);
        long count=Long.valueOf(String.valueOf(query2.getSingleResult()));
        //库存商品总数
        long stockAmount=stockUnitRepository.getStockAmount(warehouseId);
        log.info("库存商品总数："+stockAmount);
        List<StockDTO> stockDTOs=new ArrayList<>();
        for(Object[] obj:list){
            StockDTO stockDTO=new StockDTO();
            stockDTO.setItemNo(obj[0].toString());
            stockDTO.setSkuNo(obj[1].toString());
            stockDTO.setName(obj[2].toString());
            stockDTO.setStockState(obj[3].toString());
            stockDTO.setAmountTotal(new BigDecimal(String.valueOf(obj[4])));
            stockDTO.setAmountReserve(new BigDecimal(String.valueOf(obj[5])));
            stockDTO.setAmountUse(new BigDecimal(String.valueOf(obj[6])));
            if(obj[8]!=null)
                stockDTO.setClient(obj[8].toString());
            stockDTO.setWarehouse(warehouseId);
            //获取该商品的残损数量
            Client client=clientRepository.getByClientName(obj[8].toString());
            List<StockUnit> damaged=stockUnitRepository.getByStorageLocationType(warehouseId,obj[0].toString(),client.getId(), StockUnitState.DAMAGE.getName());
            BigDecimal damagedAmount=BigDecimal.ZERO;
            for(StockUnit s:damaged){
                damagedAmount.add(s.getAmount());
            }
            //获取该商品的待调查数量
            List<StockUnit> pending=stockUnitRepository.getByStorageLocationType(warehouseId,obj[0].toString(),client.getId(), StockUnitState.PENDING.getName());
            BigDecimal pendingAmount=BigDecimal.ZERO;
            for(StockUnit su:pending){
                pendingAmount.add(su.getAmount());
            }
            stockDTO.setAmountDamage(damagedAmount);
            stockDTO.setAmountPending(pendingAmount);
            stockDTO.setStockAmount(stockAmount);
            stockDTOs.add(stockDTO);
        }
        return new PageImpl<StockDTO>(stockDTOs,pageable,count);
    }

    public List<StockDTO> getByStockUnit(String param){
        String warehouseId=applicationContext.getCurrentWarehouse();
        log.info("库存查询的参数是："+param);
        //参数是商品条码或商品名称
        String sql = "SELECT MD_ITEMDATA.ITEM_NO as itemNo,MD_ITEMDATA.SKU_NO as skuNo, MD_ITEMDATA.NAME as itemName,INV_STOCKUNIT.STATE as stockState, " +
                "sum(INV_STOCKUNIT.AMOUNT) as amount, sum(INV_STOCKUNIT.RESERVED_AMOUNT) as reservedAmount, " +
                "sum(INV_STOCKUNIT.AMOUNT-INV_STOCKUNIT.RESERVED_AMOUNT) as useAmount, " +
                "SYS_WAREHOUSE.WAREHOUSE_NO as warehouseNo,SYS_CLIENT.CLIENT_NO as clientNo " +
                "FROM INV_STOCKUNIT INNER JOIN  MD_ITEMDATA " +
                "on INV_STOCKUNIT.ITEMDATA_ID=MD_ITEMDATA.ID " +
                "LEFT JOIN SYS_WAREHOUSE on INV_STOCKUNIT.WAREHOUSE_ID=SYS_WAREHOUSE.ID " +
                "LEFT JOIN SYS_CLIENT on INV_STOCKUNIT.CLIENT_ID=SYS_CLIENT.ID " +
                "WHERE INV_STOCKUNIT.ENTITY_LOCK<>2 and INV_STOCKUNIT.AMOUNT<>0 " +
                "AND INV_STOCKUNIT.WAREHOUSE_ID=:warehouseId ";
        String str="GROUP BY itemNo,skuNo,itemName,stockState,warehouseNo,clientNo order by itemNo";
        List<ItemData> itemDatas=itemDataRepository.getBysku(param,warehouseId);
        if(itemDatas.size()==1){
            param=itemDatas.get(0).getItemNo();
            sql=sql+" AND MD_ITEMDATA.ITEM_NO='"+param+"'";
        }else{
            sql=sql+" AND MD_ITEMDATA.NAME like '%"+param+"%'";
        }
        sql=sql+str;
        Query query=entityManager.createNativeQuery(sql);
        query.setParameter("warehouseId",warehouseId);
        List<Object[]> list=query.getResultList();
        List<StockDTO> stockDTOs=new ArrayList<>();
        for(Object[] obj:list){
            StockDTO stockDTO=new StockDTO();
            stockDTO.setItemNo(obj[0].toString());
            stockDTO.setSkuNo(obj[1].toString());
            stockDTO.setName(obj[2].toString());
            stockDTO.setStockState(obj[3].toString());
            stockDTO.setAmountTotal(new BigDecimal(String.valueOf(obj[4])));
            stockDTO.setAmountReserve(new BigDecimal(String.valueOf(obj[5])));
            stockDTO.setAmountUse(new BigDecimal(String.valueOf(obj[6])));
            stockDTO.setClient(obj[8].toString());
            stockDTO.setWarehouse(warehouseId);
            //获取该商品的残损数量
            Client client=clientRepository.getByClientName(obj[8].toString());
            List<StockUnit> damaged=stockUnitRepository.getByStorageLocationType(warehouseId,obj[0].toString(),client.getId(), StockUnitState.DAMAGE.getName());
            BigDecimal damagedAmount=BigDecimal.ZERO;
            for(StockUnit s:damaged){
                damagedAmount.add(s.getAmount());
            }
            //获取该商品的待调查数量
            List<StockUnit> pending=stockUnitRepository.getByStorageLocationType(warehouseId,obj[0].toString(),client.getId(), StockUnitState.PENDING.getName());
            BigDecimal pendingAmount=BigDecimal.ZERO;
            for(StockUnit su:pending){
                pendingAmount.add(su.getAmount());
            }
            stockDTO.setAmountDamage(damagedAmount);
            stockDTO.setAmountPending(pendingAmount);
            stockDTOs.add(stockDTO);
        }
        return stockDTOs;
    }

    public List<StockDTO> exportStockUnitInfo() {
        String sql = "SELECT MD_ITEMDATA.ITEM_NO as itemNo, MD_ITEMDATA.NAME as name,INV_STOCKUNIT.STATE as stockState, " +
                "sum(INV_STOCKUNIT.AMOUNT) as amountTotal, sum(INV_STOCKUNIT.RESERVED_AMOUNT) as amountReserve, " +
                "sum(INV_STOCKUNIT.AMOUNT-INV_STOCKUNIT.RESERVED_AMOUNT) as amountUse, " +
                "SYS_WAREHOUSE.WAREHOUSE_NO as warehouse,SYS_CLIENT.CLIENT_NO as client " +
                "FROM INV_STOCKUNIT INNER JOIN  MD_ITEMDATA " +
                "on INV_STOCKUNIT.ITEMDATA_ID=MD_ITEMDATA.ID " +
                "LEFT JOIN SYS_WAREHOUSE on INV_STOCKUNIT.WAREHOUSE_ID=SYS_WAREHOUSE.ID " +
                "LEFT JOIN SYS_CLIENT on INV_STOCKUNIT.CLIENT_ID=SYS_CLIENT.ID " +
                "where MD_ITEMDATA.ID in (SELECT DISTINCT MD_ITEMDATA.ID from MD_ITEMDATA) " +
                "and INV_STOCKUNIT.ENTITY_LOCK<>2 and INV_STOCKUNIT.AMOUNT<>0 " +
                "GROUP BY itemNo,name,stockState,warehouse,client order by itemNo";
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(StockDTO.class));
        List<StockDTO> list = query.getResultList();
        return list;
    }
}
