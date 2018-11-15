package com.mushiny.wms.schedule.business;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.RandomUtil;
import com.mushiny.wms.schedule.common.*;
import com.mushiny.wms.schedule.crud.dto.FinishStowDTO;
import com.mushiny.wms.schedule.domin.*;
import com.mushiny.wms.schedule.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class CheckBusiness {
    private final StockUnitRepository stockUnitRepository;
    private final SemblenceRepository semblenceRepository;
    private final LotRepository lotRepository;
    private final StockUnitRecordRepository stockUnitRecordRepository;
    private final UnitLoadRepository unitLoadRepository;
    private final OrderBusiness orderBusiness;
    private final Logger log= LoggerFactory.getLogger(CheckBusiness.class);
    private final Parameter parameter;
    private final CustomerOrderRepository customerOrderRepository;
    private final ItemDataRepository itemDataRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CheckBusiness(StockUnitRepository stockUnitRepository,
                         SemblenceRepository semblenceRepository,
                         LotRepository lotRepository,
                         StockUnitRecordRepository stockUnitRecordRepository,
                         UnitLoadRepository unitLoadRepository,
                         OrderBusiness orderBusiness,
                         Parameter parameter,
                         CustomerOrderRepository customerOrderRepository,
                         ItemDataRepository itemDataRepository) {
        this.stockUnitRepository = stockUnitRepository;
        this.semblenceRepository = semblenceRepository;
        this.lotRepository = lotRepository;
        this.stockUnitRecordRepository = stockUnitRecordRepository;
        this.unitLoadRepository = unitLoadRepository;
        this.orderBusiness = orderBusiness;
        this.parameter = parameter;
        this.customerOrderRepository = customerOrderRepository;
        this.itemDataRepository = itemDataRepository;
    }

    //商品属性是否不符
    public void CheckItemWithStorageLocationIsSeem(StorageLocation storageLocation, ItemData itemData){
        if(storageLocation.getZone()==null){
            return;
        }
        Set<ItemGroup> itemGroups = storageLocation.getZone().getItemGroups();
        boolean flag = false;
        for (ItemGroup itemGroup:itemGroups){
            if(itemGroup.getId().equalsIgnoreCase(itemData.getItemGroup().getId())){
                flag = true;
                break;
            }
        }
        if(!flag)
            throw new ApiException("%"+itemData.getItemGroup().getName()+"&"+""+ StowConstant.STORAGEUNVALUE+":"+storageLocation.getName()+""+StowConstant.STORAGECATALOGNOTSEEM,itemData.getItemGroup().getName());
    }

    //货位中商品种类是否超过系统上限
    public void CheckIsBeyoungMaxCatalogs(UnitLoad unitLoad, StorageLocation storageLocation, ItemData itemData){
        //货位中实际商品种类
        List<ItemData> itemDataList =stockUnitRepository.itemByUnitLoad(unitLoad,itemData) ;
        if (itemDataList.size()==0){
            int amount = (int)stockUnitRepository.countByUnitLoad(unitLoad);
            //商品种类是否超过最大值
            int max = storageLocation.getStorageLocationType().getMaxItemDataAmount();
            if(amount >= max)
                throw new ApiException(StowConstant.STORAGEBEYOUNGMAXCATALOG);
        }
    }

    //是否超重
    public void CheckStorageLoadWeight(ItemData itemData, UnitLoad unitLoad, int skuNum){
        long unitloadWeight = unitLoad.getWeightCalculated();
        if((unitloadWeight+itemData.getItemDataGlobal().getWeight()*skuNum)>unitLoad.getStorageLocation().getStorageLocationType().getLiftingCapacity())
            throw new ApiException(StowConstant.STORAGEBEYOUNGMAXWERIGHT);
    }

    //是否存在不同供应商
    public void CheckClientIsSeem(List<StockUnit> stockUnitList, ItemData itemData){
        for (StockUnit stockUnit :stockUnitList){
            //供应商是否一致
            if(!stockUnit.getItemData().getClientId().equalsIgnoreCase(itemData.getClientId()))
                throw new ApiException(StowConstant.ReceiveStationInvalidCode,StowConstant.CIPERNUM+stockUnit.getUnitLoad().getStorageLocation().getName()+
                        StowConstant.CLIENTNOTSEEM);
        }
    }

    //是否存在相似商品
    public void CheckItemIsSeem(List<StockUnit> stockUnitList,ItemData itemData){
        String clientId = parameter.getClient();
        Semblence semblence = semblenceRepository.getByClientId(clientId);
        for (StockUnit stockUnit:stockUnitList){
            float seemValue = CommonUtil.semilar(stockUnit.getItemData().getName(),itemData.getName());
            //System.out.println(stockUnit.getItemData().getItemNo()+"与"+itemData.getItemNo()+"的相似值--->"+seemValue);
            if(seemValue<1&&seemValue>((float)semblence.getSemblence()/(float)100))
                throw new ApiException(StowConstant.SKUSEEMTOOHIGH);
            if(seemValue==1&&!stockUnit.getItemData().getId().equalsIgnoreCase(itemData.getId()))
                throw new ApiException(StowConstant.SKUSEEMTOOHIGH);
        }
    }

    //检查商品有效期
    public Lot CheckLotOrSave(ItemData itemData,LocalDate useAfter) {
        List<Lot> lots = lotRepository.getByItemData(itemData,useAfter);
        Lot lot = null;
        if(lots!=null&&!lots.isEmpty()&&lots.size()>0){
            lot = lots.get(0);
        }
        if(lot==null){//不存在有效期
            lot = new Lot();
            String lotNo = null;
            //随机标志位(是否停止生成编号)
            boolean flag = true;
            while(flag){
                lotNo = RandomUtil.getLotNo();
                Lot invLot = lotRepository.getByLotNo(lotNo);
                if(invLot==null){
                    lot.setLotNo(lotNo);
                    flag =false;
                }
            }
            lot.setItemData(itemData);
            lot.setLotDate(DateTimeUtil.getNowDate());
            lot.setUseNotAfter(useAfter);
            lot.setWarehouseId(itemData.getWarehouseId());
            lot.setClientId(itemData.getClientId());
            lot.setCreatedBy(parameter.getUserName());
            lot.setModifiedBy(parameter.getUserName());
        }
        return lotRepository.saveAndFlush(lot);
    }

    public void buildStowStockUnitRecord(StockUnit stockUnit, FinishStowDTO finishStowDTO){
        StockUnitRecord stockUnitRecord = new StockUnitRecord();
        stockUnitRecord.setAmount(finishStowDTO.getAmount());
        stockUnitRecord.setItemNo(stockUnit.getItemData().getItemNo());
        stockUnitRecord.setSku(stockUnit.getItemData().getSkuNo());
        if(stockUnit.getItemData().isLotMandatory()){
            stockUnitRecord.setLot(stockUnit.getLot().getLotNo());
        }
        stockUnitRecord.setOperator(parameter.getUserName());
        stockUnitRecord.setToStockUnit(stockUnit.getId());
        stockUnitRecord.setToStorageLocation(stockUnit.getUnitLoad().getStorageLocation().getName());
        stockUnitRecord.setToUnitLoad(stockUnit.getUnitLoad().getLabel());
        stockUnitRecord.setWarehouseId(stockUnit.getWarehouseId());
        stockUnitRecord.setClientId(stockUnit.getClientId());
        stockUnitRecord.setFromState(StockUnitState.INVENTORY.getName());
        stockUnitRecord.setToState(StockUnitState.INVENTORY.getName());
        stockUnitRecord.setRecordCode("M");
        stockUnitRecord.setRecordTool("Receive");
        stockUnitRecord.setRecordType("EACH_RECEIVE_TO_STOW");
        stockUnitRecord.setSerialNo(stockUnit.getSerialNo());
        stockUnitRecord.setCreatedBy(parameter.getUserName());
        stockUnitRecord.setModifiedBy(parameter.getUserName());
        stockUnitRecordRepository.save(stockUnitRecord);
    }

    public UnitLoad checkUnitLoad(StorageLocation storageLocation) {
        UnitLoad unitLoad = unitLoadRepository.getByStorageLocation(parameter.getWarehouse(), storageLocation);
        if (unitLoad == null) {
            unitLoad = new UnitLoad();
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
            unitLoad.setWeightCalculated(0L);
            unitLoad.setWeightMeasure(BigDecimal.ZERO);
            unitLoad.setCarrier(false);
            unitLoad.setClientId(parameter.getClient());
            unitLoad.setWarehouseId(parameter.getWarehouse());
            unitLoad.setCreatedBy(parameter.getUserName());
            unitLoad.setModifiedBy(parameter.getUserName());
            if (storageLocation.getPod() == null) {
                unitLoad.setLocationIndex(0);
            } else {
                unitLoad.setLocationIndex(storageLocation.getPod().getPodIndex());
            }
        }else{
           if(Objects.equals(unitLoad.getEntityLock(), Constant.GENERAL)){
               throw new ApiException("容器被锁定");
           }
        }
        return unitLoadRepository.saveAndFlush(unitLoad);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeSQLToCreateOrder(CustomerOrder order){
        //定义生成订单的数量
        List<CustomerOrder> customerOrders = customerOrderRepository.getByState(CustomerShipmentState.RELEASED);
        log.info("订单的数量" + customerOrders.size() + ".........");
//        if(customerOrders.size()>7){
//            return ;
//        }
        String sql = "INSERT INTO OB_CUSTOMERORDER VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1,order.getId());
        query.setParameter(2,order.getCustomerName());
        query.setParameter(3,order.getCustomerNo());
        query.setParameter(4,order.getDeliveryDate());
        query.setParameter(5,order.getSortCode());
        query.setParameter(6,order.getOrderNo());
        query.setParameter(7,order.getPriority());
        query.setParameter(8,order.getState());
        query.setParameter(9,order.getStrategy().getId());
        query.setParameter(10,null);
        query.setParameter(11,order.getClientId());
        query.setParameter(12,order.getWarehouseId());
        query.setParameter(13,order.getModifiedDate());
        query.setParameter(14,order.getModifiedBy());
        query.setParameter(15,order.getCreatedDate());
        query.setParameter(16,order.getCreatedBy());
        query.setParameter(17,order.getAdditionalContent());
        query.setParameter(18,order.getEntityLock());
        query.setParameter(19,order.getVersion());

        query.executeUpdate();

        excuteSQLTOCreateOrderPosition(order);
    }

    private void excuteSQLTOCreateOrderPosition(CustomerOrder order){
        //获取所有有库存商品的ID
        List<String> itemIds = getListId();
        int length=itemIds.size();
        if(length==0)
            throw new ApiException("没有可下单的商品------");
        String sql = "INSERT INTO OB_CUSTOMERORDERPOSITION VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Query query = entityManager.createNativeQuery(sql);
        Random random=new Random();
        int size = Math.abs(random.nextInt(2)) + 2;
        Date d=new Date();
        for (int j = 0;j < size;j++) {
            query.setParameter(1, UUID.randomUUID().toString());
            query.setParameter(14, order.getCreatedBy());
            query.setParameter(13, order.getCreatedDate());
            query.setParameter(11, order.getModifiedDate());
            query.setParameter(12, order.getModifiedBy());
            query.setParameter(15, order.getAdditionalContent());
            query.setParameter(16, order.getEntityLock());
            query.setParameter(2, order.getVersion());
            //customerOrder明细 数量
            query.setParameter(3, random.nextInt(2) + 1);
            query.setParameter(4, j);
            query.setParameter(5, j);
            query.setParameter(6, order.getState());
            query.setParameter(7, itemIds.get(random.nextInt(length)));
            query.setParameter(8, order.getId());
            query.setParameter(9, order.getClientId());
            query.setParameter(10, order.getWarehouseId());

            query.executeUpdate();
            log.info("成功创建一个订单 " + order.getOrderNo() + "当前时间---------：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d.getTime()));
        }
    }

    private List<String> getListId() {
       // String sql = "select s.itemData.id from StockUnit s group by s.itemData.id having (sum(s.amount) - sum(s.reservedAmount)) > 3";
        String sql="SELECT b.id from (SELECT SUM(A.AMOUNT) amount,A.id " +
                "FROM (SELECT S.AMOUNT, M.id " +
                "FROM INV_STOCKUNIT S, MD_ITEMDATA M " +
                "WHERE S.ITEMDATA_ID = M.Id " +
                "AND M.SERIAL_RECORD_TYPE<>'ALWAYS_RECORD' "+
                "AND S.ENTITY_LOCK = 0 AND S.UNITLOAD_ID IN ( " +
                "SELECT ID FROM INV_UNITLOAD U WHERE " +
                "U.ENTITY_LOCK = 0 AND U.STORAGELOCATION_ID IN ( " +
                "SELECT ID  FROM MD_STORAGELOCATION L "+
                "WHERE L.SECTION_ID='ec229eb7-7e2b-43a8-b1c7-91bd807e91cf' " +
                "AND L.TYPE_ID IN ( " +
                "SELECT id FROM MD_STORAGELOCATIONTYPE e " +
                "WHERE e.STORAGETYPE = 'BIN' ) ) ) ) A " +
                "GROUP BY A. id  HAVING amount>1) b ";
        Query query = entityManager.createNativeQuery(sql);

        List<String> listId = query.getResultList();
        return listId;
    }

    public List<String> getItemDataId() {
        String sql="SELECT b.id from (SELECT SUM(A.AMOUNT) amount,A.id " +
                "FROM (SELECT S.AMOUNT, M.id " +
                "FROM INV_STOCKUNIT S, MD_ITEMDATA M " +
                "WHERE S.ITEMDATA_ID = M.Id " +
                "AND M.SERIAL_RECORD_TYPE<>'ALWAYS_RECORD' " +
                "AND M.CLIENT_ID='SYSTEM' "+
                "AND S.ENTITY_LOCK = 0 AND S.UNITLOAD_ID IN ( " +
                "SELECT ID FROM INV_UNITLOAD U WHERE " +
                "U.ENTITY_LOCK = 0 AND U.STORAGELOCATION_ID IN ( " +
                "SELECT ID  FROM MD_STORAGELOCATION L "+
                "WHERE L.SECTION_ID='ec229eb7-7e2b-43a8-b1c7-91bd807e91cf' " +
                "AND L.TYPE_ID IN ( " +
                "SELECT id FROM MD_STORAGELOCATIONTYPE e " +
                "WHERE e.STORAGETYPE = 'BIN' ) ) ) ) A " +
                "GROUP BY A. id ) b ";
//                "GROUP BY A. id HAVING amount<5) b ";

        Query query = entityManager.createNativeQuery(sql);

        List<String> listId = query.getResultList();
        return listId;
    }

    public List<ItemData> getAll(){
        List<ItemData> itemDatas=itemDataRepository.getAll();
        return itemDatas;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int split(CustomerOrder order,Date d){
        //查询订单中的商品在库存是否能满足
        List<CustomerOrderPosition> orderPositions = order.getPositions();
        boolean match = false;
        for (CustomerOrderPosition position: orderPositions) {
            match = false;
            int result =  getStockUnit(position);
            if(result == 0){
                break;
            }else {
                match = true;
                continue;
            }
        }
        if(match){
            List<CustomerShipment> customerShipments = orderBusiness.splitCustomerOrder(order);
            if (!customerShipments.isEmpty()) {
                for (CustomerShipment shipment : customerShipments) {
                    createShipment(shipment,order);
                    for (CustomerShipmentPosition position:shipment.getPositions()) {
                        createShipmentPosition(shipment,position);
                    }
                }
                updateCustomerOrder(order);
                for (CustomerOrderPosition p:order.getPositions()) {
                    updateOrderPositon(p);
                }
            }
            return 1;
        }
        return 0;
    }

    private void updateCustomerOrder(CustomerOrder order){
        String sql = "UPDATE OB_CUSTOMERORDER SET STATE=?,MODIFIED_DATE=? WHERE ID=?";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, CustomerShipmentState.RELEASED);
        query.setParameter(2,LocalDateTime.now());
        query.setParameter(3,order.getId());
        query.executeUpdate();
    }

    private int getStockUnit(CustomerOrderPosition p) {
        //获取已经拆分过订单的商品的数量总和
        String sql1 = "SELECT (SUM(CP.amount)-sum(CP.amountPicked)) FROM CustomerShipmentPosition CP" +
                " where CP.state < 600 " +
                " AND CP.itemData = :item" +
                " GROUP BY CP.itemData";
        Query q = entityManager.createQuery(sql1);
        q.setParameter("item",p.getItemData());
        List result = q.getResultList();

        BigDecimal amoutShipment = BigDecimal.ZERO;
        if(result.size() > 0){
            amoutShipment = (BigDecimal)result.get(0);
        }

        //获取总库存数量减去订单占得数量的库存
        String sql = "SELECT S.ITEMDATA_ID AS ITEMDATA,SUM(S.AMOUNT) AS TOTAL,SUM(S.RESERVED_AMOUNT) AS LOCKAMOUNT" +
                " FROM INV_STOCKUNIT S " +
                " LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID" +
                " LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID" +
                " LEFT JOIN MD_STORAGELOCATIONTYPE SLT ON SL.TYPE_ID = SLT.ID" +
                " WHERE  S.ITEMDATA_ID = '"+p.getItemData().getId()+"'" +
                " AND S.STATE = 'Inventory'"+
                " AND S.ENTITY_LOCK = 0"+
                " AND SLT.STORAGETYPE = 'BIN'"+
                " GROUP BY S.ITEMDATA_ID" +
                " HAVING (SUM(S.AMOUNT)-" + amoutShipment + ")>= "+p.getAmount();

        Query query = entityManager.createNativeQuery(sql);
        List list = query.getResultList();
        int o = 0;
        if(list.size() > 0){
            o = 1;
        }
        return o;
    }

    private void createShipment(CustomerShipment shipment,CustomerOrder order) {

//        String sql = "INSERT INTO OB_CUSTOMERSHIPMENT VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        Query query = entityManager.createNativeQuery(sql);
//        query.setParameter(1,shipment.getId());
//        query.setParameter(2,shipment.getCreatedDate());
//        query.setParameter(3, SecurityUtils.getCurrentUsername());
//        query.setParameter(4,shipment.getModifiedDate());
//        query.setParameter(5,SecurityUtils.getCurrentUsername());
//        query.setParameter(6,shipment.getAdditionalContent());
//        query.setParameter(7,shipment.getEntityLock());
//        query.setParameter(8,shipment.getVersion());
//        query.setParameter(9,shipment.getCustomerName());
//        query.setParameter(10,shipment.getCustomerNo());
//        query.setParameter(11,shipment.getDeliveryDate());
//        query.setParameter(12,shipment.getSortCode());
//        query.setParameter(13,shipment.getShipmentNo());
//        query.setParameter(14,shipment.getPriority());
//        query.setParameter(15,0);
//        query.setParameter(16,order.getId());
//        if(shipment.getBoxType() != null){
//            query.setParameter(17,shipment.getBoxType().getId());
//        }else {
//            query.setParameter(17,null);
//        }
//        query.setParameter(18,shipment.getPickMode());
//        query.setParameter(19,null);
//        query.setParameter(20,shipment.getPassedOverCount());
//        query.setParameter(21,shipment.isActivated());
//        query.setParameter(22,shipment.getActivationDate());
//        query.setParameter(23,shipment.isSelected());
//        query.setParameter(24,shipment.isCompleted());
//        query.setParameter(25,shipment.getPickPackStationId());
//        query.setParameter(26,shipment.getPickPackCellId());
//        if(shipment.getCustomerOrder().getStrategy() != null){
//            query.setParameter(27,shipment.getCustomerOrder().getStrategy().getId());
//        }else {
//            query.setParameter(27,null);
//        }
//        query.setParameter(28,shipment.getClientId());
//        query.setParameter(29,shipment.getWarehouseId());
//        query.setParameter(30,"1");
//        query.setParameter(31,"1");

        String sql = "INSERT INTO OB_CUSTOMERSHIPMENT(ID,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,ADDITIONAL_CONTENT," +
                "ENTITY_LOCK,VERSION,CUSTOMER_NAME,CUSTOMER_NO,DELIVERY_DATE,SORT_CODE," +
                "SHIPMENT_NO,PRIORITY,STATE,ORDER_ID,BOXTYPE_ID,PICK_MODE," +
                "PICKINGCATEGORY_ID,PASSED_OVER_COUNT,ACTIVATED,ACTIVATION_DATE,SELECTED,COMPLETED," +
                "PICKSTATION_ID,PICKPACKCELL_ID,STRATEGY_ID,CLIENT_ID,WAREHOUSE_ID,RECEIVER_ID," +
                "SENDER_ID,ACCOMPLISH) " +
                "VALUES (?,?,?,?,?,?," +
                "?,?,?,?,?,?," +
                "?,?,?,?,?,?," +
                "?,?,?,?,?,?," +
                "?,?,?,?,?,?," +
                "?,?)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1,shipment.getId());
        query.setParameter(2,shipment.getCreatedDate());
        query.setParameter(3,parameter.getUserName());
        query.setParameter(4,shipment.getModifiedDate());
        query.setParameter(5,parameter.getUserName());
        query.setParameter(6,shipment.getAdditionalContent());

        query.setParameter(7,shipment.getEntityLock());
        query.setParameter(8,shipment.getVersion());
        query.setParameter(9,shipment.getCustomerName());
        query.setParameter(10,shipment.getCustomerNo());
        query.setParameter(11,shipment.getDeliveryDate());
        query.setParameter(12,shipment.getSortCode());

        query.setParameter(13,shipment.getShipmentNo());
        query.setParameter(14,shipment.getPriority());
        query.setParameter(15,0);
        query.setParameter(16,order.getId());
        if(shipment.getBoxType() != null){
            query.setParameter(17,shipment.getBoxType().getId());
        }else {
            query.setParameter(17,null);
        }
        query.setParameter(18,shipment.getPickMode());

        query.setParameter(19,null);
        query.setParameter(20,shipment.getPassedOverCount());
        query.setParameter(21,shipment.isActivated());
        query.setParameter(22,shipment.getActivationDate());
        query.setParameter(23,shipment.isSelected());
        query.setParameter(24,shipment.isCompleted());

        query.setParameter(25,shipment.getPickPackStationId());
        query.setParameter(26,shipment.getPickPackCellId());
        if(shipment.getCustomerOrder().getStrategy() != null){
            query.setParameter(27,shipment.getCustomerOrder().getStrategy().getId());
        }else {
            query.setParameter(27,null);
        }
        query.setParameter(28,shipment.getClientId());
        query.setParameter(29,shipment.getWarehouseId());
        query.setParameter(30,"1");

        query.setParameter(31,"1");
        query.setParameter(32,"0");
        query.executeUpdate();

        log.info("成功拆分一个订单");
    }

    private void createShipmentPosition(CustomerShipment shipment,CustomerShipmentPosition position){
        String sql = "INSERT INTO OB_CUSTOMERSHIPMENTPOSITION VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1,position.getId());
        query.setParameter(2,position.getAmount());
        query.setParameter(3,position.getAmountPicked());
        query.setParameter(4,position.getAmountRebined());
        query.setParameter(5,position.getState());
        query.setParameter(6,position.getItemData().getId());
        query.setParameter(7,shipment.getId());
        query.setParameter(8,position.getPositionNo());
        query.setParameter(9,position.getOrderIndex());
        query.setParameter(10,position.getClientId());
        query.setParameter(11,position.getWarehouseId());
        query.setParameter(12,position.getCreatedDate());
        query.setParameter(13,parameter.getUserName());
        query.setParameter(14,position.getModifiedDate());
        query.setParameter(15,parameter.getUserName());
        query.setParameter(16,position.getAdditionalContent());
        query.setParameter(17,position.getEntityLock());
        query.setParameter(18,position.getVersion());

        query.executeUpdate();
        log.info("获得订单明细---");
    }

    private void updateOrderPositon(CustomerOrderPosition position){
        String sql = "update OB_CUSTOMERORDERPOSITION set STATE = ?,MODIFIED_DATE=? where ID = ?";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, CustomerShipmentState.RELEASED);
        query.setParameter(2,LocalDateTime.now());
        query.setParameter(3,position.getId());
        query.executeUpdate();
    }
}
