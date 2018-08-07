package com.mushiny.business;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.constants.State;
import com.mushiny.model.*;
import com.mushiny.repository.*;
import com.mushiny.utils.ContainerTypeUtil;
import com.mushiny.utils.DateUtil;
import com.mushiny.utils.StockStateUtil;
import com.mushiny.web.dto.CustomerShipmentDTO;
import com.mushiny.web.dto.CustomerShipmentPositionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 123 on 2018/2/2.
 */
@Component
public class CustomerOrderBusiness {
    private final Logger log = LoggerFactory.getLogger(CustomerOrderBusiness.class);

    private final EntityManager manager;
    private final ItemDataRepository itemDataRepository;
    private final ClientRepository clientRepository;
    private final BoxRepository boxRepository;
    private final DeliveryTimeRepository deliveryTimeRepository;


    @Autowired
    public CustomerOrderBusiness(EntityManager manager,
                                 ItemDataRepository itemDataRepository,
                                 BoxRepository boxRepository,
                                 DeliveryTimeRepository deliveryTimeRepository,
                                 ClientRepository clientRepository){
        this.manager = manager;
        this.itemDataRepository = itemDataRepository;
        this.boxRepository = boxRepository;
        this.clientRepository = clientRepository;
        this.deliveryTimeRepository = deliveryTimeRepository;
    }

    /**
     *  未合并订单行  根据优先级修改发货时间点
     * @param orderDTO
     * @param warehouse
     * @param exsdTime
     * @param deliveryTimefs
     * @return
     */
    public AccessDTO createShipment(CustomerShipmentDTO orderDTO,Warehouse warehouse,LocalDateTime exsdTime,LocalDateTime deliveryTimefs) {
        AccessDTO accessDTO = new AccessDTO();

        CustomerShipment shipment = new CustomerShipment();
        shipment.setCustomerName(orderDTO.getOrderNo());
        shipment.setCustomerNo(orderDTO.getOrderNo());
        shipment.setShipmentNo(orderDTO.getOrderNo());

        //创建发货时间点
//        DeliveryTime deliveryTime = deliveryTimeRepository.getByDeliveryTimeAndWarehouse(DateUtil.getLocalDateTime(orderDTO.getDeliveryDate()),warehouse);
        List<DeliveryTime> deliveryTimeList = deliveryTimeRepository.getByDeliveryTimeAndWarehouse(deliveryTimefs,warehouse);
        DeliveryTime deliveryTime = null;
        if(deliveryTimeList.isEmpty()){
            log.info("创建发货时间点 ：" + deliveryTimefs.toString());
            deliveryTime = generateDeliveryTime(deliveryTimefs,warehouse);
        }else {
            deliveryTime = deliveryTimeList.get(0);
        }

        shipment.setDeliveryDate(deliveryTime.getDeliveryTime());

        shipment.setExsdTime(exsdTime);

        shipment.setSortCode(orderDTO.getSortCode());
        shipment.setState(State.RAW);
//        shipment.setType(OrderTypeUtil.revertOrderType(orderDTO.getType()));
        if("1".equalsIgnoreCase(orderDTO.getFsType())){
            shipment.setType(orderDTO.getFsType());
        }else {
            shipment.setType("0");
        }

        shipment.setContainerType(ContainerTypeUtil.revertType(orderDTO.getContainerType()));

        //设置订单的箱型用于分配PP
        BoxType boxType = boxRepository.getByName(ContainerTypeUtil.getBox(orderDTO.getContainerType()));
        shipment.setBoxType(boxType);

        shipment.setWarehouseId(warehouse.getId());

        List<CustomerShipmentPositionDTO> positionDTOS = orderDTO.getPositions();
        for (int i = 0;i < positionDTOS.size();i++){
            CustomerShipmentPosition position = new CustomerShipmentPosition();
            position.setLineNo(positionDTOS.get(i).getItem());
            position.setAmount(positionDTOS.get(i).getAmount());
            position.setPositionNo(i);
            position.setOrderIndex(i);
            position.setEndDate(DateUtil.toStringDate(positionDTOS.get(i).getEndDate()));
            position.setStockState(StockStateUtil.toWmsState(positionDTOS.get(i).getStockState()));
            position.setState(State.RAW);

            Client client = clientRepository.getByClientNo(positionDTOS.get(i).getClientNo());
//            ItemData itemData = itemDataRepository.getByItemNoAndClientId(positionDTOS.get(i).getItemNo(),client.getId());
            ItemData itemData = itemDataRepository.getByItemNo(positionDTOS.get(i).getItemNo());
            if(itemData == null){
                log.info("货主：" + client.getClientNo() +" 的商品：" + positionDTOS.get(i).getItemNo() +" 在系统中不存在。。。");
                accessDTO.setMsg("货主：" + client.getClientNo() +" 的商品：" + positionDTOS.get(i).getItemNo() +" 在系统中不存在");
                accessDTO.setCode("1");
                return accessDTO;
            }

            position.setItemDataId(itemData.getId());
            position.setClientId(client.getId());
            position.setWarehouseId(warehouse.getId());

            shipment.addPosition(position);
        }

        manager.persist(shipment);

        return accessDTO;
    }

    /**
     *  未合并订单行 直接使用优先级
     * @param orderDTO
     * @param warehouse
     * @param priproty
     * @param deliveryTimefs
     * @return
     */
    public AccessDTO createShipment(CustomerShipmentDTO orderDTO,Warehouse warehouse,int priproty,LocalDateTime deliveryTimefs) {
        AccessDTO accessDTO = new AccessDTO();

        CustomerShipment shipment = new CustomerShipment();
        shipment.setCustomerName(orderDTO.getOrderNo());
        shipment.setCustomerNo(orderDTO.getOrderNo());
        shipment.setShipmentNo(orderDTO.getOrderNo());

        //创建发货时间点
        List<DeliveryTime> deliveryTimeList = deliveryTimeRepository.getByDeliveryTimeAndWarehouse(deliveryTimefs,warehouse);
        DeliveryTime deliveryTime = null;
        if(deliveryTimeList.isEmpty()){
            log.info("创建发货时间点 ：" + deliveryTimefs.toString());
            deliveryTime = generateDeliveryTime(deliveryTimefs,warehouse);
        }else {
            deliveryTime = deliveryTimeList.get(0);
        }

        shipment.setDeliveryDate(deliveryTime.getDeliveryTime());

        shipment.setSortCode(orderDTO.getSortCode());
        shipment.setState(State.RAW);

        if("1".equalsIgnoreCase(orderDTO.getFsType())){
            shipment.setType(orderDTO.getFsType());
        }else {
            shipment.setType("0");
        }

        shipment.setPriority(priproty);
        shipment.setContainerType(ContainerTypeUtil.revertType(orderDTO.getContainerType()));

        //设置订单的箱型用于分配PP
        BoxType boxType = boxRepository.getByName(ContainerTypeUtil.getBox(orderDTO.getContainerType()));
        shipment.setBoxType(boxType);

        shipment.setWarehouseId(warehouse.getId());

        List<CustomerShipmentPositionDTO> positionDTOS = orderDTO.getPositions();
        for (int i = 0;i < positionDTOS.size();i++){
            CustomerShipmentPosition position = new CustomerShipmentPosition();
            position.setLineNo(positionDTOS.get(i).getItem());
            position.setAmount(positionDTOS.get(i).getAmount());
            position.setPositionNo(i);
            position.setOrderIndex(i);
            position.setEndDate(DateUtil.toStringDate(positionDTOS.get(i).getEndDate()));
            position.setStockState(StockStateUtil.toWmsState(positionDTOS.get(i).getStockState()));
            position.setState(State.RAW);

            Client client = clientRepository.getByClientNo(positionDTOS.get(i).getClientNo());
//            ItemData itemData = itemDataRepository.getByItemNoAndClientId(positionDTOS.get(i).getItemNo(),client.getId());
            ItemData itemData = itemDataRepository.getByItemNo(positionDTOS.get(i).getItemNo());
            if(itemData == null){
                log.info("货主：" + client.getClientNo() +" 的商品：" + positionDTOS.get(i).getItemNo() +" 在系统中不存在。。。");
                accessDTO.setMsg("货主：" + client.getClientNo() +" 的商品：" + positionDTOS.get(i).getItemNo() +" 在系统中不存在");
                accessDTO.setCode("1");
                return accessDTO;
            }

            position.setItemDataId(itemData.getId());
            position.setClientId(client.getId());
            position.setWarehouseId(warehouse.getId());

            shipment.addPosition(position);
        }

        manager.persist(shipment);

        return accessDTO;
    }

    /**
     * 合并订单行
     * @param orderDTO
     * @param warehouse
     * @param priproty
     * @param deliveryTimefs
     * @return
     */
    public CustomerOrder createOrder(CustomerShipmentDTO orderDTO,Warehouse warehouse,int priproty,LocalDateTime deliveryTimefs){
        CustomerOrder order = new CustomerOrder();
        order.setCustomerName(orderDTO.getOrderNo());
        order.setCustomerNo(orderDTO.getOrderNo());
        order.setOrderNo(orderDTO.getOrderNo());

        //创建发货时间点
//        DeliveryTime deliveryTime = deliveryTimeRepository.getByDeliveryTimeAndWarehouse(DateUtil.getLocalDateTime(orderDTO.getDeliveryDate()),warehouse);
        List<DeliveryTime> deliveryTimeList = deliveryTimeRepository.getByDeliveryTimeAndWarehouse(deliveryTimefs,warehouse);
        DeliveryTime deliveryTime = null;
        if(deliveryTimeList.isEmpty()){
            log.info("创建发货时间点 ：" + deliveryTimefs.toString());
            deliveryTime = generateDeliveryTime(deliveryTimefs,warehouse);
        }else {
            deliveryTime = deliveryTimeList.get(0);
        }

        order.setDeliveryDate(deliveryTime.getDeliveryTime());

        order.setPriority(priproty);
        order.setSortCode(orderDTO.getSortCode());
        order.setState(State.RAW);
        if("1".equalsIgnoreCase(orderDTO.getFsType())){
            order.setType(orderDTO.getFsType());
        }else {
            order.setType("0");
        }

        order.setContainerType(ContainerTypeUtil.revertType(orderDTO.getContainerType()));

        //设置订单的箱型用于分配PP
        BoxType boxType = boxRepository.getByName(ContainerTypeUtil.getBox(orderDTO.getContainerType()));
        order.setBoxType(boxType);

        order.setWarehouseId(warehouse.getId());

        List<CustomerShipmentPositionDTO> positionDTOS = orderDTO.getPositions();
        for (int i = 0;i < positionDTOS.size();i++){
            CustomerOrderPosition position = new CustomerOrderPosition();
            position.setLineNo(positionDTOS.get(i).getItem());
            position.setAmount(positionDTOS.get(i).getAmount());
            position.setPositionNo(i);
            position.setOrderIndex(i);
            position.setEndDate(DateUtil.toStringDate(positionDTOS.get(i).getEndDate()));
            position.setStockState(StockStateUtil.toWmsState(positionDTOS.get(i).getStockState()));
            position.setState(State.RAW);

            Client client = clientRepository.getByClientNo(positionDTOS.get(i).getClientNo());
            ItemData itemData = itemDataRepository.getByItemNo(positionDTOS.get(i).getItemNo());
            /*if(itemData == null){
                log.info("货主：" + client.getClientNo() +" 的商品：" + positionDTOS.get(i).getItemNo() +" 在系统中不存在。。。");
                accessDTO.setMsg("货主：" + client.getClientNo() +" 的商品：" + positionDTOS.get(i).getItemNo() +" 在系统中不存在");
                accessDTO.setCode("1");
                return accessDTO;
            }*/

            position.setItemDataId(itemData.getId());
            position.setClientId(client.getId());
            position.setWarehouseId(warehouse.getId());

            order.addPosition(position);
        }

        manager.persist(order);

        return order;
    }

    public CustomerShipment createShipment(CustomerOrder order) {
        if(order == null){
            return null;
        }

        CustomerShipment shipment = new CustomerShipment();
        shipment.setDeliveryDate(order.getDeliveryDate());
        shipment.setPriority(order.getPriority());
        shipment.setContainerType(order.getContainerType());
        shipment.setType(order.getType());
        shipment.setBoxType(order.getBoxType());
        shipment.setWarehouseId(order.getWarehouseId());
        shipment.setState(State.RAW);
        shipment.setSortCode(order.getSortCode());
        shipment.setShipmentNo(order.getCustomerNo());
        shipment.setCustomerName(order.getCustomerName());
        shipment.setCustomerNo(order.getCustomerNo());

        Map<ItemClient,BigDecimal> shipmentPositionMap = new ConcurrentHashMap<>();
        for (CustomerOrderPosition p: order.getPositions()) {
            boolean containItem = true;
            for (Map.Entry<ItemClient,BigDecimal> positionMap:shipmentPositionMap.entrySet()) {
                ItemClient item = positionMap.getKey();
                BigDecimal amount = positionMap.getValue();
                if(p.getEndDate() == null || "".equals(p.getEndDate())){
                    if(item.getClientId().equals(p.getClientId()) &&
                            item.getEndDate() == null &&
                            item.getItemDataId().equals(p.getItemDataId()) &&
                            item.getStockState().equals(p.getStockState())){

                        amount = amount.add(p.getAmount());

                        shipmentPositionMap.put(item,amount);
                        containItem = false;
                        break;
                    }
                }else {
                    if(item.getClientId().equals(p.getClientId()) &&
                            p.getEndDate().equals(item.getEndDate()) &&
                            item.getItemDataId().equals(p.getItemDataId()) &&
                            item.getStockState().equals(p.getStockState())){

                        amount = amount.add(p.getAmount());

                        shipmentPositionMap.put(item,amount);
                        containItem = false;
                        break;
                    }
                }
            }
            if(!containItem){
                continue;
            }else {
                //获取当前行信息
                ItemClient itemClient = new ItemClient();
                itemClient.setClientId(p.getClientId());
                itemClient.setEndDate(p.getEndDate());
                itemClient.setItemDataId(p.getItemDataId());
                itemClient.setStockState(p.getStockState());

                shipmentPositionMap.put(itemClient,p.getAmount());
            }

        }

        //生成订单明细
        int i = 1;
        for (Map.Entry<ItemClient,BigDecimal> positionMap:shipmentPositionMap.entrySet()) {
            ItemClient itemClient = positionMap.getKey();
            BigDecimal amount = positionMap.getValue();
            CustomerShipmentPosition position = new CustomerShipmentPosition();
            if(i < 10){
                position.setLineNo("00000000000" + String.valueOf(i));
            }else {
                position.setLineNo("0000000000" + String.valueOf(i));
            }
            position.setItemDataId(itemClient.getItemDataId());
            position.setClientId(itemClient.getClientId());
            position.setStockState(itemClient.getStockState());
            position.setEndDate(itemClient.getEndDate());
            position.setAmount(amount);
            position.setOrderIndex(i);
            position.setPositionNo(i);
            position.setState(State.RAW);
            position.setWarehouseId(order.getWarehouseId());

            shipment.addPosition(position);
            i++;
        }
        manager.persist(shipment);

        return shipment;
    }

    private DeliveryTime generateDeliveryTime(LocalDateTime localDateTime,Warehouse warehouse) {
        DeliveryTime deliveryTime = new DeliveryTime();
        deliveryTime.setDeliveryTime(localDateTime);
        deliveryTime.setWarehouse(warehouse);
        manager.persist(deliveryTime);
        return deliveryTime;
    }


}
