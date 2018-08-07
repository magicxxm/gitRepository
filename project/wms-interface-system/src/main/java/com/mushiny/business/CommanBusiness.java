package com.mushiny.business;

import com.mushiny.common.utils.RandomUtil;
import com.mushiny.constants.State;
import com.mushiny.model.*;
import com.mushiny.repository.LotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by 123 on 2018/2/9.
 */
@Component
public class CommanBusiness {
    private final Logger log = LoggerFactory.getLogger(CommanBusiness.class);

    private final EntityManager manager;
    private final LotRepository lotRepository;

    public CommanBusiness(EntityManager manager,
                          LotRepository lotRepository){
        this.manager = manager;
        this.lotRepository = lotRepository;
    }

    /**
     * 新建客户
     * @param clientNo
     * @return
     */
    public Client generateClient(String clientNo){
        Client client = new Client();
        client.setClientNo(clientNo);
        client.setName(clientNo);
        manager.persist(client);
        return client;
    }

    /**
     * 新建客户的sku
     * @param itemDataGlobal
     * @param client
     * @param warehouse
     * @return
     */
    public ItemData generateItemdata(ItemDataGlobal itemDataGlobal, Client client, Warehouse warehouse) {
        ItemData itemData = new ItemData();

        itemData.setName(itemDataGlobal.getName());//商品名称
        itemData.setItemNo(itemDataGlobal.getItemNo());
        itemData.setSkuNo(itemDataGlobal.getSkuNo());
        itemData.setDepth(itemDataGlobal.getDepth());
        itemData.setHeight(itemDataGlobal.getHeight());
        itemData.setWidth(itemDataGlobal.getWidth());
        //单位默认 “个”，即wms中的 “单品”
        itemData.setItemUnit(itemDataGlobal.getItemUnit());

        //商品分组  和安得确认用具体代码表示哪一类分组
        itemData.setItemGroup(itemDataGlobal.getItemGroup());

        //是否是有效期商品
        itemData.setLotMandatory(itemDataGlobal.isLotMandatory());
        //拒收天数
        itemData.setLotThreshold(itemDataGlobal.getLotThreshold());
        //有效期类型
        itemData.setLotType(itemDataGlobal.getLotType());

        //有效期单位
        itemData.setLotUnit(itemDataGlobal.getLotUnit());
        //是否测量
        itemData.setMeasured(itemDataGlobal.isMeasured());
        //是否自带包装
        itemData.setPreferOwnBox(itemDataGlobal.isPreferOwnBox());
        //是否提供袋子
        itemData.setPreferBag(itemDataGlobal.isPreferBag());
        //是否使用气垫膜
        itemData.setUseBubbleFilm(itemDataGlobal.isUseBubbleFilm());
        itemData.setMultiplePartAmount(itemDataGlobal.getMultiplePartAmount());
        itemData.setMultiplePart(itemDataGlobal.isMultiplePart());
        //安全库存
        itemData.setSafetyStock(itemDataGlobal.getSafetyStock());
        //序列号规则
        itemData.setSerialRegular(itemDataGlobal.getSerialRegular());
        //是否记录序列号
        itemData.setSerialRecordType(itemDataGlobal.getSerialRecordType());
        itemData.setVolume(itemDataGlobal.getVolume());
        itemData.setWeight(itemDataGlobal.getWeight());
        itemData.setShelflife(itemDataGlobal.getShelflife());

        itemData.setState(State.RAW);
        itemData.setItemDataGlobalId(itemDataGlobal.getId());

        itemData.setClientId(client.getId());
        itemData.setWarehouseId(warehouse.getId());

        manager.persist(itemData);

        return itemData;
    }

    /**
     * 新建客户商品对应的有效期
     * @param date
     * @param bestBeforeEnd
     * @param itemData
     * @param client
     * @param warehouse
     * @return
     */
    public Lot generateLot(LocalDate date, LocalDate bestBeforeEnd, ItemData itemData, Client client, Warehouse warehouse) {
        Lot lot = new Lot();
        boolean randomFlag = true;
        String lotNo = "";
        while (randomFlag) {
            lotNo = RandomUtil.getLotNo();
            if (lotRepository.getByLotNo(lotNo) == null) {
                lot.setNumber(lotNo);
                randomFlag = false;
            }
        }
        lot.setName(lotNo);
        lot.setBestBeforeEnd(bestBeforeEnd);
        lot.setDate(date);
        lot.setProductDate(date);
        lot.setItemData(itemData);
        lot.setClientId(client.getId());
        lot.setWarehouseId(warehouse.getId());

        manager.persist(lot);

        return lot;
    }

    /**
     * 新建库存数据
     */
    public StockUnit generateStockUnit(BigDecimal notifiedAmount, String stockState, ItemData itemData, UnitLoad unitLoad, Lot lot,String batchNo, Client client, Warehouse warehouse) {
        StockUnit stockUnit = new StockUnit();

        stockUnit.setAmount(notifiedAmount);
        stockUnit.setItemData(itemData);
        stockUnit.setLot(lot);
        stockUnit.setBatchOrder(batchNo);
        stockUnit.setState(stockState);
        stockUnit.setUnitLoad(unitLoad);
        stockUnit.setClientId(client.getId());
        stockUnit.setWarehouseId(warehouse.getId());

        manager.persist(stockUnit);

        return stockUnit;
    }

    public StockUnitRecord generateStockUnitRecord(StockUnit stockUnit, ItemData itemData,Lot lot,UnitLoad unitLoad, Client client, Warehouse warehouse) {
        StockUnitRecord record = new StockUnitRecord();

        record.setAmount(stockUnit.getAmount());
        record.setItemDataItemNo(itemData.getItemNo());
        record.setItemDataSKU(itemData.getSkuNo());
        if(lot != null){
            record.setLot(lot.getNumber());
        }
        record.setOperator("system");
        record.setToState(stockUnit.getState());
        record.setToStockUnit(stockUnit.getId());
        record.setToStorageLoaction(unitLoad.getStorageLocation().getName());
        record.setToUnitLoad(unitLoad.getLabelId());
        record.setRecordCode("M");
        record.setRecordTool("Interface");
        record.setRecordType("RECEIPT_ORDER_TO_STOCKUNIT");

        record.setClientId(client.getId());
        record.setWarehouseId(warehouse.getId());

        manager.persist(record);

        return record;
    }

    public void generateClientWarehouse(Client client, Warehouse warehouse) {
        WarehouseClient warehouseClient = new WarehouseClient();
        warehouseClient.setClientId(client.getId());
        warehouseClient.setWarehouseId(warehouse.getId());
        manager.persist(warehouseClient);
    }
}
