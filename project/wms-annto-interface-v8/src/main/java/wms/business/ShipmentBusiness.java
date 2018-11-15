package wms.business;

import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.business.dto.ShipmentConfirmItemsDTO;
import wms.common.crud.AccessDTO;
import wms.constants.State;
import wms.crud.dto.*;
import wms.domain.*;
import wms.domain.common.*;
import wms.repository.*;
import wms.repository.common.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/8/15.
 */
@Component
public class ShipmentBusiness {

    private final Logger log = LoggerFactory.getLogger(ShipmentBusiness.class);

    private final EntityManager manager;
    private final ClientRepository clientRepository;
    private final CustomerOrderPositionRepository customerOrderPositionRepository;
    private final WarehouseRepository warehouseRepository;
    private final ItemDataRepository itemDataRepository;
    private final AnntoBusiness anntoBusiness;
    private final AnntoCustomerOrderRepository anntoCustomerOrderRepository;
    private final SectionRepository sectionRepository;
    private final CustomerShipmentRepository shipmentRepository;
    private final UserRepository userRepository;
    private final CustomerOrderRepository orderRepository;
    private final DeliveryTimeRepository deliveryTimeRepository;
    private final DeliverSortCodeRepository deliverSortCodeRepository;
    private final CarrierRepository carrierRepository;
    private final DeliveryPointRepository deliveryPointRepository;

    @Autowired
    public ShipmentBusiness(EntityManager manager,
                            ClientRepository clientRepository,
                            CustomerOrderPositionRepository customerOrderPositionRepository,
                            WarehouseRepository warehouseRepository,
                            CustomerShipmentRepository shipmentRepository,
                            ItemDataRepository itemDataRepository,
                            SectionRepository sectionRepository,
                            UserRepository userRepository,
                            AnntoBusiness anntoBusiness,
                            CustomerOrderRepository orderRepository,
                            DeliveryTimeRepository deliveryTimeRepository,
                            DeliverSortCodeRepository deliverSortCodeRepository,
                            CarrierRepository carrierRepository,
                            DeliveryPointRepository deliveryPointRepository,
                            AnntoCustomerOrderRepository anntoCustomerOrderRepository) {
        this.manager = manager;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.sectionRepository = sectionRepository;
        this.shipmentRepository = shipmentRepository;
        this.customerOrderPositionRepository = customerOrderPositionRepository;
        this.anntoCustomerOrderRepository = anntoCustomerOrderRepository;
        this.warehouseRepository = warehouseRepository;
        this.itemDataRepository = itemDataRepository;
        this.anntoBusiness = anntoBusiness;
        this.deliveryTimeRepository = deliveryTimeRepository;
        this.deliverSortCodeRepository = deliverSortCodeRepository;
        this.carrierRepository = carrierRepository;
        this.deliveryPointRepository = deliveryPointRepository;
    }

    public AccessDTO checkStockUnit(ShipmentUpdateDTO dto) {
        AccessDTO accessDTO = new AccessDTO();
        String warehouseNo = dto.getWarehouseCode();
        String clientNo = dto.getCompanyCode();
        Warehouse warehouse = warehouseRepository.getByWarehouseNo(warehouseNo);
        Client client = clientRepository.findByClientNo(clientNo);

        //查询仓库中库存是否满足订单中的商品
        List<ShipmentOrderItem> orderItemsDTOS = dto.getOrderItems();
        for(int i = 0; i < orderItemsDTOS.size(); i++) {
            String itemNo = orderItemsDTOS.get(i).getItemCode();
            ItemData item = itemDataRepository.getByItemCode(warehouse.getId(),client.getId(),itemNo);
            if(item == null){
                log.error("出库单："+dto.getCode()+" 商品:" + item.getName()+" 在牧星库中不存在。。。");
                accessDTO.setMsg("库中没有商品" + orderItemsDTOS.get(i).getItemName());
                accessDTO.setCode("1");
                return accessDTO;
            }
            BigDecimal amount = new BigDecimal(orderItemsDTOS.get(i).getPlanQty());
            boolean hasStockUnit = getStockUnit(item,amount,client.getId(),warehouse.getId());
            if(!hasStockUnit){
                log.error("牧星仓库的库存不能满足 出库单："+dto.getCode()+" 中的商品: " + item.getName()+" 。。。");
                accessDTO.setMsg("商品 "+orderItemsDTOS.get(i).getItemName()+" 库存不够");
                accessDTO.setCode("1");
                return accessDTO;
            }
        }
        return accessDTO;
    }

    private boolean getStockUnit(ItemData item, BigDecimal amount,String clientId,String warehouseId) {
        int amoutShipment = 0;
        String positionStr = "SELECT SUM(CP.AMOUNT) FROM  OB_CUSTOMERSHIPMENTPOSITION CP WHERE CP.ITEMDATA_ID = '"+item.getId()+"' AND CP.STATE < 600";
        Query pq = manager.createNativeQuery(positionStr);
        BigDecimal amountPosition = (BigDecimal)pq.getSingleResult();
        if(amountPosition != null){
            //获取已被订单占据并且还在原库位的数量
            String sql1 = "SELECT (SUM(CP.AMOUNT)-SUM(CP.AMOUNT_PICKED)) FROM OB_CUSTOMERSHIPMENTPOSITION CP" +
                    " WHERE CP.STATE < 600 " +
                    " AND CP.ITEMDATA_ID = '"+item.getId()+"'" +
                    " GROUP BY CP.ITEMDATA_ID";
            Query q = manager.createNativeQuery(sql1);
            BigDecimal result = (BigDecimal)q.getSingleResult();
            if(result != null){
                amoutShipment = result.intValue();
            }
        }else {
            log.debug("Itemdata : " + item.getName()+" is empty in CustomerShipmentPosition ..");
        }

        //获取已被订单占据并且在容器中的数量（比如订单商品在PickToTote时商品都在正品货框中）
        String sql2 = "SELECT SUM(s.AMOUNT) FROM INV_STOCKUNIT s,INV_UNITLOAD u" +
                " WHERE s.UNITLOAD_ID = u.ID" +
                " AND u.ID IN ( SELECT us.UNITLOAD_ID FROM INV_UNITLOAD_SHIPMENT us )" +
                " AND s.ITEMDATA_ID = '"+item.getId()+"'";
        Query q2 = manager.createNativeQuery(sql2);
        BigDecimal r = (BigDecimal)q2.getSingleResult();
        int amountPicked = 0;
        if(r != null){
            amountPicked = r.intValue();
        }
        amoutShipment = amoutShipment + amountPicked;
        log.info("商品："+item.getName()+" 已被订单占据的数量为 ：" + amoutShipment);

        String sql = "SELECT (SUM(S.AMOUNT)-"+amoutShipment+")"+
                " FROM INV_STOCKUNIT S " +
                " WHERE  S.ITEMDATA_ID = '"+item.getId()+"'" +
                " AND S.STATE = 'Inventory'"+
                " GROUP BY S.ITEMDATA_ID" +
                " HAVING (SUM(S.AMOUNT)-" + amoutShipment + ") >= "+amount;

        Query query = manager.createNativeQuery(sql);
        BigDecimal amoutStock = (BigDecimal) query.getSingleResult();
        log.info("商品："+item.getName()+" 的可用库存数量为 ：" + amoutStock+",,订单需要商品数量为 ："+ amount);
        boolean hasStockUnit = false;
        if(amoutStock != null){
            hasStockUnit = true;
        }
        return hasStockUnit;
    }

    public AccessDTO createAnntoCustomerOrder(ShipmentUpdateDTO dto) {

        AccessDTO accessDTO = new AccessDTO();
        AnntoCustomerOrder anntoCustomerOrder = new AnntoCustomerOrder();
        anntoCustomerOrder.setCode(dto.getCode());
        anntoCustomerOrder.setCompanyCode(dto.getCompanyCode());
        anntoCustomerOrder.setWarehouseCode(dto.getWarehouseCode());
        anntoCustomerOrder.setShipmentType(dto.getShipmentType());
        anntoCustomerOrder.setRequesteddeliverydate(dto.getRequestedDeliveryDate());
        anntoCustomerOrder.setCarrierCode(dto.getCarrierCode());
        anntoCustomerOrder.setCarrierService(dto.getCarrierService());
        anntoCustomerOrder.setPrimaryWaybillCode(dto.getPrimaryWaybillCode());
        anntoCustomerOrder.setBusinessType(dto.getBusinessType());
        anntoCustomerOrder.setRemark(dto.getRemark());

        JSONArray orderArray = JSONArray.fromObject(dto.getOrderItems());
        List<ShipmentOrderItem> orderItemsDTOS = (List<ShipmentOrderItem>)JSONArray.toCollection(orderArray,ShipmentOrderItem.class);
        if(orderItemsDTOS.size() > 0) {
                for (ShipmentOrderItem o:orderItemsDTOS) {
                    AnntoCustomerOrderItems anntoCustomerOrderItems = new AnntoCustomerOrderItems();
                    anntoCustomerOrderItems.setLineNo(o.getLineNo());
                    anntoCustomerOrderItems.setItemCode(o.getItemCode());
                    anntoCustomerOrderItems.setItemName(o.getItemName());
                    anntoCustomerOrderItems.setInventoryType(o.getInventorySts());
                    anntoCustomerOrderItems.setKitFlag(o.getKitFlag());
                    anntoCustomerOrderItems.setPlanQty(new BigDecimal(o.getPlanQty()));
                    anntoCustomerOrderItems.setRemark(o.getRemark());
                    anntoCustomerOrderItems.setAnntoCustomerOrder(anntoCustomerOrder);

                    anntoCustomerOrder.addOrderItem(anntoCustomerOrderItems);
//                    manager.persist(anntoCustomerOrderItems);
                }
        }
        manager.persist(anntoCustomerOrder);
        return accessDTO;
    }

    public CustomerOrder createCustomerOrder(ShipmentUpdateDTO dto) {
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCustomerName(dto.getCode());
        customerOrder.setCustomerNo(dto.getCode());
        customerOrder.setDeliveryDate(dto.getRequestedDeliveryDate());
        customerOrder.setSortCode(dto.getCarrierCode());
        customerOrder.setOrderNo(dto.getCode());
        customerOrder.setPriority(1);
        customerOrder.setState(0);
        customerOrder.setStrategy(null);
        customerOrder.setClientId(clientRepository.findByClientNo(dto.getCompanyCode()).getId());
        customerOrder.setWarehouseId(warehouseRepository.getByWarehouseNo(dto.getWarehouseCode()).getId());

        JSONArray orderArray = JSONArray.fromObject(dto.getOrderItems());
        List<ShipmentOrderItem> orderItems = (List<ShipmentOrderItem>)JSONArray.toCollection(orderArray,ShipmentOrderItem.class);
        for(int i = 0; i < orderItems.size(); i++) {
            CustomerOrderPosition customerOrderPosition = new CustomerOrderPosition();
            customerOrderPosition.setOrderIndex(i + 1);
            customerOrderPosition.setPositionNo(i + 1);
            customerOrderPosition.setState(0);
                /**
                 * 商品信息需要确认
                 */
            customerOrderPosition.setAmount(new BigDecimal(orderItems.get(i).getPlanQty()));
            customerOrderPosition.setItemData(itemDataRepository.getBySkuNo(
                    warehouseRepository.getByWarehouseNo(dto.getWarehouseCode()).getId(),
                    orderItems.get(i).getItemCode()));
            customerOrderPosition.setCustomerOrder(customerOrder);
            customerOrderPosition.setClientId(clientRepository.findByClientNo(dto.getCompanyCode()).getId());
            customerOrderPosition.setWarehouseId(warehouseRepository.getByWarehouseNo(dto.getWarehouseCode()).getId());

            customerOrder.getPositions().add(customerOrderPosition);
        }
        manager.persist(customerOrder);
        return customerOrder;
    }

   /* public CustomerShipment createCustomerShipment(ShipmentUpdateDTO dto) {
        CustomerShipment shipment = new CustomerShipment();
        shipment.setWarehouseId(warehouseRepository.getByWarehouseNo(dto.getWarehouseCode()).getId());
        shipment.setClientId(clientRepository.findByClientNo(dto.getCompanyCode()).getId());

        shipment.setSortCode(dto.getCarrierCode());
        shipment.setDeliveryDate(dto.getRequestedDeliveryDate());
        shipment.setShipmentNo(dto.getCode());
        shipment.setState(State.RAW);

        //出库单同步时的箱型确定
        shipment.setBoxType(null);
        shipment.setPriority(0);//优先等级

        List<ShipmentOrderItem> orderItems = dto.getOrderItems();
        for (int i = 0;i < orderItems.size();i++){
            CustomerShipmentPosition position = new CustomerShipmentPosition();
            position.setPositionNo(i);
            position.setState(State.RAW);
            position.setOrderIndex(i);
            position.setItemData(itemDataRepository.getOneByItemNoAndClientId(orderItems.get(i).getItemCode(),
                    clientRepository.findByClientNo(dto.getCompanyCode()).getId()));
            position.setAmount(new BigDecimal(orderItems.get(i).getPlanQty()));

            position.setClientId(clientRepository.findByClientNo(dto.getCompanyCode()).getId());
            position.setWarehouseId(warehouseRepository.getByWarehouseNo(dto.getWarehouseCode()).getId());

            shipment.addPosition(position);
        }

        manager.persist(shipment);
        return shipment;
    }*/

    /**
     * 出库单确认,向美的发送出库单结果
     */
    /*public void shipmentConfirm(String shipmentNo,String operatorNo, String operatorTime){
        ShipmentConfirmDTO shipmentConfirmDTO = new ShipmentConfirmDTO();

//        AnntoCustomerOrder anntoCustomerOrder = anntoCustomerOrderRepository.getByCode(shipmentNo);
        CustomerShipment shipment = shipmentRepository.getByShipmentNo(shipmentNo);

        shipmentConfirmDTO.setCode(shipmentNo);
        shipmentConfirmDTO.setCompanyCode(clientRepository.findOne(shipment.getClientId()).getClientNo());//货主编码  notnull
        shipmentConfirmDTO.setWarehouseCode(warehouseRepository.findOne(shipment.getId()).getWarehouseNo());//出库仓编码  notNull
        shipmentConfirmDTO.setShipmentType("PO");
        shipmentConfirmDTO.setOperateTime(operatorTime);//出库完结时间  notnull
        shipmentConfirmDTO.setOperatorName(userRepository.findByUsername(operatorNo).getName());//操作员姓名
        shipmentConfirmDTO.setOperatorCode(operatorNo);//操作员编码

        List<ShipmentConfirmItemsDTO> orderItems = new ArrayList<>();
        List<CustomerShipmentPosition> positions = shipment.getPositions();

        //获取annto传入数据，获取lineNo
        AnntoCustomerOrder order = anntoCustomerOrderRepository.getByCode(shipmentNo);
        List<AnntoCustomerOrderItems> items = order.getOrderItems();

        for (CustomerShipmentPosition p:positions) {
            ShipmentConfirmItemsDTO dto = new ShipmentConfirmItemsDTO();
            //获取商品行号
            for (AnntoCustomerOrderItems a:items) {
                if(p.getItemData().getItemNo().equals(a.getItemCode())){
                    dto.setLineNo(a.getLineNo());
                    dto.setUnit(a.getUnit());
                }
            }
            dto.setItemCode(p.getItemData().getItemNo());
            dto.setPlanQty(p.getAmount().intValue());
            dto.setQuantity(p.getAmountPicked().intValue());

            orderItems.add(dto);
        }

        shipmentConfirmDTO.setOrderItems(orderItems);

        anntoBusiness.confirmShipmet(shipmentConfirmDTO);
    }*/

    public String checkCustomerOrder(ShipmentUpdateDTO dto) {
        Warehouse warehouse = warehouseRepository.getByWarehouseNo(dto.getWarehouseCode());
        CustomerOrder order = orderRepository.getByOrderNoAAndWarehouseId(dto.getCode(),warehouse.getId());
        if(order != null){
            return "yes";
        }
        return "no";
    }

    public void saveSortCode(ShipmentUpdateDTO dto) {
        String warehouseId = warehouseRepository.getByWarehouseNo(dto.getWarehouseCode()).getId();

        //保存EXsd时间点
        String exsd = dto.getRequestedDeliveryDate();
        DeliveryTime deliveryTime = deliveryTimeRepository.getByTimeAndWarehouseId(exsd,warehouseId);
        if(deliveryTime == null){
            deliveryTime = saveExsd(exsd,warehouseId);
        }

        // 保存sortCode
        String sortCode = dto.getCarrierCode();
        DeliverySortCode ds = deliverSortCodeRepository.getByCode(sortCode,warehouseId);
        if(ds == null){
            ds = saveDeliverySortCode(sortCode,warehouseId);
        }

        //保存carrier 承运商
        String carrierNo = dto.getCarrierCode();
        String carrierName = carrierNo;
        if(null != dto.getCarrierService() && !"".equals(dto.getCarrierService())){
            carrierName = dto.getCarrierService();
        }
        Carrier carrier = carrierRepository.getByCarreierNoAndWarehouseId(carrierNo,warehouseId);
        if(carrier == null){
            carrier = saveCarrier(carrierNo,carrierName,warehouseId);
        }

        //保存到DeliveryPoint
        DeliveryPoint deliveryPoint = deliveryPointRepository.getBySortCodeAndTime(ds,deliveryTime,warehouseId);
        if(deliveryPoint == null){
            saveDeliveryPoint(deliveryTime,ds,carrier,warehouseId);
        }


    }

    private void saveDeliveryPoint(DeliveryTime deliveryTime, DeliverySortCode ds, Carrier carrier, String warehouseId) {
        DeliveryPoint deliveryPoint = new DeliveryPoint();
        deliveryPoint.setCarrier(carrier);
        deliveryPoint.setDeliveryTime(deliveryTime);
        deliveryPoint.setSortCode(ds);
        deliveryPoint.setWarehouseId(warehouseId);
        deliveryPointRepository.save(deliveryPoint);
    }

    private Carrier saveCarrier(String carrierNo, String carrierName, String warehouseId) {
        Carrier carrier = new Carrier();
        carrier.setCarrierNo(carrierNo);
        carrier.setName(carrierName);
        carrier.setWarehouseId(warehouseId);
        carrier = carrierRepository.save(carrier);
        return carrier;
    }

    private DeliverySortCode saveDeliverySortCode(String sortCode, String warehouseId) {
        DeliverySortCode ds = new DeliverySortCode();
        ds.setCode(sortCode);
        ds.setWarehouseId(warehouseId);
        ds = deliverSortCodeRepository.save(ds);
        return ds;
    }

    private DeliveryTime saveExsd(String exsd, String warehouseId) {
        DeliveryTime deliveryTime = new DeliveryTime();
        deliveryTime.setDeliveryTime(exsd);
        deliveryTime.setWarehouseId(warehouseId);
        deliveryTime = deliveryTimeRepository.save(deliveryTime);
        return deliveryTime;
    }
}
