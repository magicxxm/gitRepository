package wms.business.order;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.business.ShipmentRecordBusiness;
import wms.common.utils.RandomUtil;
import wms.domain.CustomerOrder;
import wms.domain.CustomerOrderPosition;
import wms.domain.ItemData;
import wms.domain.common.BoxType;
import wms.domain.common.CustomerShipment;
import wms.domain.common.CustomerShipmentPosition;
import wms.domain.common.WdSection;
import wms.repository.CustomerShipmentRepository;
import wms.repository.common.BoxTypeRepository;
import wms.repository.common.SectionRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

@Component
public class SplitOrderBusiness {

    private final BoxTypeRepository boxTypeRepository;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final BoxPackerBusiness boxPackerBusiness;
    private final ShipmentRecordBusiness shipmentRecordBusiness;
    private final SectionRepository sectionRepository;
    private final EntityManager manager;

    @Autowired
    public SplitOrderBusiness(CustomerShipmentRepository customerShipmentRepository,
                              BoxPackerBusiness boxPackerBusiness,
                              BoxTypeRepository boxTypeRepository,
                              ShipmentRecordBusiness shipmentRecordBusiness,
                              SectionRepository sectionRepository,
                              EntityManager manager) {
        this.customerShipmentRepository = customerShipmentRepository;
        this.boxPackerBusiness = boxPackerBusiness;
        this.boxTypeRepository = boxTypeRepository;
        this.shipmentRecordBusiness = shipmentRecordBusiness;
        this.sectionRepository = sectionRepository;
        this.manager = manager;
    }

    public void split(List<CustomerShipment> shipments,
                      CustomerOrder customerOrder,
                      List<CustomerOrderPosition> orderPositions) {
        // 剩余一单一件，是否可以裸发
        if (orderPositions.size() == 1
                && (orderPositions.get(0).getAmount().compareTo(BigDecimal.ONE) == 0)) {
            ItemData itemData = orderPositions.get(0).getItemData();
            if (itemData.isPreferOwnBox()) {
                shipments.add(getShipment(customerOrder, orderPositions, null));
                return;
            }
        }
        // 判断每件商品是否都可以用袋子装
        boolean useBag = true;
        for (CustomerOrderPosition orderPosition : orderPositions) {
            if (!orderPosition.getItemData().isPreferBag()) {
                useBag = false;
                break;
            }
        }
        // 取出所有商品
        List<ItemData> itemDataList = new ArrayList<>();
        for (CustomerOrderPosition orderPosition : orderPositions) {
            // 按顺序添加相同位置的ItemData,并且按数量拆分添加，如果不满1个按一个算
            int amount = orderPosition.getAmount().setScale(0, BigDecimal.ROUND_UP).intValue();
            for (int i = 0; i < amount; i++) {
                itemDataList.add(orderPosition.getItemData());
            }
        }
        String boxTypeId;
        if (useBag) {
            // 拆分袋子
            boxTypeId = boxPackerBusiness.getBag(customerOrder, itemDataList);
            if (boxTypeId == null) {
                // 拆分箱子
                boxTypeId = boxPackerBusiness.getBox(customerOrder, itemDataList);
            }
        } else {
            // 拆分箱子
            boxTypeId = boxPackerBusiness.getBox(customerOrder, itemDataList);
        }
        // 如果没有找到合适的包装，那么就进行拆分
        if (boxTypeId != null) {
            // 包装所有商品
            BoxType boxType = boxTypeRepository.findOne(boxTypeId);
            CustomerShipment shipment = getShipment(customerOrder, orderPositions, boxType);
            shipments.add(shipment);
        } else {
            // 按最小体积的单间商品进行逐一递减进行拆单
            Map<String, CustomerOrderPosition> removePositionMap = new HashMap<>();
            boxTypeId = splitOrderPositionsByMaxBox(customerOrder, orderPositions, removePositionMap);
            BoxType boxType = boxTypeRepository.findOne(boxTypeId);
            shipments.add(getShipment(customerOrder, orderPositions, boxType));
            // 处理拆单后剩余商品
            if (!removePositionMap.isEmpty()) {
                List<CustomerOrderPosition> removeOrderPositions = new ArrayList<>();
                for (String key : removePositionMap.keySet()) {
                    removeOrderPositions.add(removePositionMap.get(key));
                }
                split(shipments, customerOrder, removeOrderPositions);
            }
        }
    }

    private String splitOrderPositionsByMaxBox(CustomerOrder customerOrder,
                                               List<CustomerOrderPosition> orderPositions,
                                               Map<String, CustomerOrderPosition> removePositionMap) {
        String boxId = null;
        boolean removeFlag = true;
        List<ItemData> maxItemDataList = new ArrayList<>();
        while (removeFlag) {
            maxItemDataList.clear();
            // 减去上CustomerOrderPosition中商品数量
            subtractAmount(orderPositions, removePositionMap);
            // 取出剩余所有商品
            for (CustomerOrderPosition orderPosition : orderPositions) {
                int amount = orderPosition.getAmount().setScale(0, BigDecimal.ROUND_UP).intValue();
                for (int i = 0; i < amount; i++) {
                    maxItemDataList.add(orderPosition.getItemData());
                }
            }
            // 获取合适的箱子
            boxId = boxPackerBusiness.getBox(customerOrder, maxItemDataList);
            if (boxId != null) {
                removeFlag = false;
            }
        }
        return boxId;
    }

    private void subtractAmount(List<CustomerOrderPosition> orderPositions,
                                Map<String, CustomerOrderPosition> removePositionMap) {
        // 按单个商品最小体积移除
        orderPositions.sort(Comparator.comparing(c -> c.getItemData().getVolume()));
        for (CustomerOrderPosition orderPosition : orderPositions) {
            // 按顺序添加ItemData,并且按数量拆分添加，如果不满1个按一个算
            int amount = orderPosition.getAmount().setScale(0, BigDecimal.ROUND_UP).intValue();
            amount = amount - 1;
            CustomerOrderPosition removePosition = removePositionMap.get(orderPosition.getId());
            if (amount <= 0) {
                // 当商品数量在移除1件小于或等于0的时候，要把商品对应的CustomerOrderPosition移除
                if (removePosition == null) {
                    removePosition = (CustomerOrderPosition) SerializationUtils.clone(orderPosition);
                    removePosition.setAmount(orderPosition.getAmount());
                } else {
                    removePosition.setAmount(removePosition.getAmount().add(orderPosition.getAmount()));
                }
                orderPositions.remove(orderPosition);
            } else {
                // 当商品数量在移除1件后还大于0，那么直接修改CustomerOrderPosition商品数量
                if (removePosition == null) {
                    removePosition = (CustomerOrderPosition) SerializationUtils.clone(orderPosition);
                    removePosition.setAmount(BigDecimal.ONE);
                } else {
                    removePosition.setAmount(removePosition.getAmount().add(BigDecimal.ONE));
                }
                orderPosition.setAmount(orderPosition.getAmount().subtract(BigDecimal.ONE));
            }
            removePositionMap.put(orderPosition.getId(), removePosition);
        }
    }

    public CustomerShipment getShipment(CustomerOrder customerOrder,
                                        List<CustomerOrderPosition> orderPositions,
                                        BoxType boxType) {
        CustomerShipment shipment = getCustomerShipment(customerOrder);
        shipment.setBoxType(boxType);
        int index = 1;
        // 相同的SKU进行汇总
        Map<String, CustomerOrderPosition> orderPositionMap = new HashMap<>();
        for (CustomerOrderPosition orderPosition : orderPositions) {
            String itemNo = orderPosition.getItemData().getItemNo();
            CustomerOrderPosition itemPosition = orderPositionMap.get(itemNo);
            if (itemPosition != null) {
                itemPosition.setAmount(itemPosition.getAmount().add(orderPosition.getAmount()));
            } else {
                itemPosition = orderPosition;
            }
            orderPositionMap.put(itemNo, itemPosition);
        }
        // 处理CustomerOrderPosition
        for (String key : orderPositionMap.keySet()) {
            CustomerOrderPosition orderPosition = orderPositionMap.get(key);
            CustomerShipmentPosition shipmentPosition = getCustomerShipmentPosition(
                    orderPosition, orderPosition.getAmount(), index - 1, index);
            index++;
            shipment.addPosition(shipmentPosition);
        }

        //根据section为shipment添加PICK_TO_PACK ， PICK_TO_TOTE
        List<WdSection> sections = sectionRepository.getByWarehouseId(shipment.getWarehouseId());
        List<CustomerShipmentPosition> positions = shipment.getPositions();
        boolean matchSection = false;
        boolean hasAllShipmentPosition = false;
        for (WdSection section:sections) {
            for (CustomerShipmentPosition p:positions){
                hasAllShipmentPosition = false;
                int o = getStockUnitBysection(section,p);
                if(o == 0){
                   break;
                }else {
                    hasAllShipmentPosition = true;
                    continue;
                }
            }
            if(hasAllShipmentPosition){
                matchSection = true;
                break;
            }else {
                continue;
            }
        }
        if(matchSection){
            shipment.setPickMode("PICK_TO_PACK");
            return shipment;
        }
        //如果多个section能满足订单所有商品，则是pickToTote
        boolean matchAllShipmentPosition = false;
        for (CustomerShipmentPosition p:positions){
            matchAllShipmentPosition = false;
            int o = getStockUnitByStockUnit(p);
            if(o == 0){
                break;
            }else {
                matchAllShipmentPosition = true;
                continue;
            }
        }
        if(matchAllShipmentPosition){
            shipment.setPickMode("PICK_TO_TOTE");
            return shipment;
        }

        //如果section中的库存不能满足订单中的所有商品，但是整个库存能满足订单的商品，则不设置拣货模式，通过定时查询订单和库存来设置

        return shipment;
    }

    private int getStockUnitByStockUnit(CustomerShipmentPosition p) {
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
                " HAVING (SUM(S.AMOUNT)- SUM(S.RESERVED_AMOUNT)) >= "+p.getAmount();

        Query query = manager.createNativeQuery(sql);
        List list = query.getResultList();
        int o = 0;
        if(list.size() > 0){
            o = 1;
        }
        return o;
    }

    //根据section判断
    private int getStockUnitBysection(WdSection section, CustomerShipmentPosition p) {

        String sql = "SELECT S.ITEMDATA_ID AS ITEMDATA,SUM(S.AMOUNT) AS TOTAL,SUM(S.RESERVED_AMOUNT) AS LOCKAMOUNT" +
                " FROM INV_STOCKUNIT S " +
                " LEFT JOIN INV_UNITLOAD U ON S.UNITLOAD_ID = U.ID" +
                " LEFT JOIN MD_STORAGELOCATION SL ON U.STORAGELOCATION_ID = SL.ID" +
                " LEFT JOIN MD_POD P ON SL.POD_ID = P.ID" +
                " LEFT JOIN WD_SECTION W ON P.SECTION_ID = W.ID" +
                " WHERE P.SECTION_ID = '"+section.getId()+"'" +
                " AND S.ITEMDATA_ID = '"+p.getItemData().getId()+"'" +
                " GROUP BY S.ITEMDATA_ID" +
                " HAVING (SUM(S.AMOUNT)- SUM(S.RESERVED_AMOUNT)) > "+p.getAmount();

        Query query = manager.createNativeQuery(sql);
        List list = query.getResultList();
        int o = 0;
        if(list.size() > 0){
            o = 1;
        }
        return o;
    }


    public CustomerShipment getCustomerShipment(CustomerOrder customerOrder) {
        CustomerShipment shipment = new CustomerShipment();
        String shipmentNo;
        boolean randomFlag = true;
        while (randomFlag) {
            shipmentNo = RandomUtil.getShipmentNo();
            if (customerShipmentRepository.getByShipmentNo(shipmentNo) == null) {
                shipment.setShipmentNo(shipmentNo);
                randomFlag = false;
            }
        }
        shipment.setAdditionalContent(customerOrder.getAdditionalContent());
        shipment.setCustomerName(customerOrder.getCustomerName());
        shipment.setCustomerNo(customerOrder.getCustomerNo());
        shipment.setDeliveryDate(customerOrder.getDeliveryDate());
        shipment.setSortCode(customerOrder.getSortCode());
        shipment.setPriority(customerOrder.getPriority());
        shipment.setState(customerOrder.getState());

        shipment.setClientId(customerOrder.getClientId());
        shipment.setWarehouseId(customerOrder.getWarehouseId());
        shipment.setOrderStrategy(customerOrder.getStrategy());
        shipment.setCustomerOrder(customerOrder);

//        manager.persist(shipment);

        return shipment;
    }

    public CustomerShipmentPosition getCustomerShipmentPosition(
            CustomerOrderPosition orderPosition, BigDecimal amount,
            int orderIndex, int positionNo) {
        CustomerShipmentPosition shipmentPosition = new CustomerShipmentPosition();

        shipmentPosition.setAdditionalContent(orderPosition.getAdditionalContent());
        shipmentPosition.setAmount(amount);
//        shipmentPosition.setAmountPicked(orderPosition.getAmount());
        shipmentPosition.setAmountPicked(BigDecimal.ZERO);
        shipmentPosition.setOrderIndex(orderIndex);
        shipmentPosition.setPositionNo(positionNo);
        shipmentPosition.setState(orderPosition.getState());

        shipmentPosition.setClientId(orderPosition.getClientId());
        shipmentPosition.setWarehouseId(orderPosition.getWarehouseId());
        shipmentPosition.setItemData(orderPosition.getItemData());

        return shipmentPosition;
    }
}
