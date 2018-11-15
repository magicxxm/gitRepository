package com.mushiny.wms.schedule.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.schedule.domin.*;
import com.mushiny.wms.schedule.repository.BoxTypeRepository;
import com.mushiny.wms.schedule.repository.CustomerShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class OrderBusiness {

    private final ApplicationContext applicationContext;
    private final BoxTypeRepository boxTypeRepository;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final BoxPackerBusiness boxPackerBusiness;
    private final SplitOrderBusiness splitOrderBusiness;
    private final EntityManager manager;

    @Autowired
    public OrderBusiness(BoxTypeRepository boxTypeRepository,
                         BoxPackerBusiness boxPackerBusiness,
                         SplitOrderBusiness splitOrderBusiness,
                         EntityManager manager,
                         CustomerShipmentRepository customerShipmentRepository,
                         ApplicationContext applicationContext) {
        this.boxTypeRepository = boxTypeRepository;
        this.boxPackerBusiness = boxPackerBusiness;
        this.splitOrderBusiness = splitOrderBusiness;
        this.customerShipmentRepository = customerShipmentRepository;
        this.applicationContext = applicationContext;
        this.manager = manager;
    }

    public List<CustomerShipment> splitCustomerOrder(CustomerOrder customerOrder) {
        List<CustomerOrderPosition> orderPositions = customerOrder.getPositions();
        if (orderPositions == null || orderPositions.isEmpty()) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        // 检查所有商品是否都有体积
        for (CustomerOrderPosition orderPosition : orderPositions) {
            if (orderPosition.getItemData().getVolume() == null) {
                throw new ApiException("", orderPosition.getItemData().getItemNo());
            }
        }
        List<CustomerShipment> customerShipments = new ArrayList<>();
        // 一单一件
        if (orderPositions.size() == 1
                && (orderPositions.get(0).getAmount().compareTo(BigDecimal.ONE) == 0)) {
            CustomerShipment customerShipment = getOneToOneShipment(
                    customerOrder, orderPositions.iterator().next());
            customerShipments.add(customerShipment);
        } else {// 一单多件
            List<CustomerOrderPosition> oneToManyPositions = new ArrayList<>();
            for (CustomerOrderPosition orderPosition : orderPositions) {
                // 检查是否有箱子可以存放这种商品
                String boxTypeId = boxPackerBusiness.getBox(customerOrder, orderPosition.getItemData());
                if (boxTypeId == null) {// 一单一件
                    int amount = orderPosition.getAmount().setScale(0, BigDecimal.ROUND_UP).intValue();
                    for (int i = 0; i < amount; i++) {
                        CustomerShipment customerShipment = getOneToOneShipment(customerOrder, orderPosition);
                        customerShipments.add(customerShipment);
                    }
                } else {// 一单多件
                    oneToManyPositions.add(orderPosition);
                }
            }
            if (!oneToManyPositions.isEmpty()) {
                // 一单多件
                customerShipments.addAll(getOneToManyShipment(customerOrder, oneToManyPositions));
            }
        }
        /*if (!customerShipments.isEmpty()) {
            for (CustomerShipment shipment : customerShipments) {
                createShipment(shipment);
                for (CustomerShipmentPosition position:shipment.getPositions()) {
                    createShipmentPosition(shipment,position);
                }
            }
        }*/
        return customerShipments;
    }

    private CustomerShipment getOneToOneShipment(
            CustomerOrder customerOrder, CustomerOrderPosition orderPosition) {
        // 一单一件
        ItemData itemData = orderPosition.getItemData();
        BoxType boxType = null;
//        if (!itemData.isPreferOwnBox()) {
            String boxTypeId;
            if (itemData.isPreferBag()) {
                // 优先使用袋子发货，如果没有合适的袋子，则用箱子发货
                boxTypeId = boxPackerBusiness.getBag(customerOrder, itemData);
                if(boxTypeId == null){
                    // 用箱子发货
                    boxTypeId = boxPackerBusiness.getBox(customerOrder, itemData);
                }
            } else {
                 // 用箱子发货
                boxTypeId = boxPackerBusiness.getBox(customerOrder, itemData);
            }
            if (boxTypeId != null) {
                boxType = boxTypeRepository.findOne(boxTypeId);
            }
//        }
//        if (boxType == null) {
//            boxType = boxTypeRepository.getOwnBox(applicationContext.getCurrentWarehouse());
//        }
        List<CustomerOrderPosition> orderPositions = new ArrayList<>();
        orderPosition.setAmount(BigDecimal.ONE);
        orderPositions.add(orderPosition);
        return splitOrderBusiness.getShipment(customerOrder, orderPositions, boxType);
    }

    private List<CustomerShipment> getOneToManyShipment(
            CustomerOrder customerOrder, List<CustomerOrderPosition> oneToManyPositions) {
        List<CustomerShipment> shipments = new ArrayList<>();
        // 根据CustomerOrderPosition的ItemData的Volume进行升序排序
        oneToManyPositions.sort(Comparator.comparing(c -> c.getItemData().getVolume()));
        // 拆分
        splitOrderBusiness.split(shipments, customerOrder, oneToManyPositions);
        return shipments;
    }
}
