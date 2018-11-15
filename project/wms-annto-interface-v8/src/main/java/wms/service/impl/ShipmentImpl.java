package wms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.business.AnntoBusiness;
import wms.business.ScheduleBusiness;
import wms.business.dto.CydhDTO;
import wms.business.dto.ShipmentConfirmItemsDTO;
import wms.common.Constant;
import wms.crud.dto.ShipmentConfirmDTO;
import wms.crud.dto.ShipmentToAnntoDTO;
import wms.domain.AnntoCustomerOrder;
import wms.domain.AnntoCustomerOrderItems;
import wms.domain.common.CustomerShipmentPosition;
import wms.domain.common.Warehouse;
import wms.repository.AnntoCustomerOrderRepository;
import wms.repository.common.UserRepository;
import wms.repository.common.WarehouseRepository;
import wms.service.Shipment;
import wms.business.OrderBusiness;
import wms.business.ShipmentBusiness;
import wms.common.crud.AccessDTO;
import wms.common.exception.ApiException;
import wms.constants.State;
import wms.crud.dto.ShipmentCancelDTO;
import wms.crud.dto.ShipmentUpdateDTO;
import wms.domain.CustomerOrder;
import wms.domain.common.CustomerShipment;
import wms.exception.ITFException;
import wms.repository.CustomerOrderRepository;
import wms.repository.CustomerShipmentRepository;
import wms.repository.common.ClientRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ShipmentImpl implements Shipment{
    private final Logger log = LoggerFactory.getLogger(ShipmentImpl.class);

    private final ShipmentBusiness shipmentBusiness;
    private final OrderBusiness orderBusiness;
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerShipmentRepository shipmentRepository;
    private final ClientRepository clientRepository;
    private final AnntoBusiness anntoBusiness;
    private final ScheduleBusiness scheduleBusiness;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final AnntoCustomerOrderRepository anntoCustomerOrderRepository;

    @Autowired
    public ShipmentImpl(ShipmentBusiness shipmentBusiness,
                        OrderBusiness orderBusiness,
                        ClientRepository clientRepository,
                        CustomerOrderRepository customerOrderRepository,
                        CustomerShipmentRepository shipmentRepository,
                        AnntoBusiness anntoBusiness,
                        ScheduleBusiness scheduleBusiness,
                        UserRepository userRepository,
                        AnntoCustomerOrderRepository anntoCustomerOrderRepository,
                        WarehouseRepository warehouseRepository) {
        this.shipmentBusiness = shipmentBusiness;
        this.orderBusiness = orderBusiness;
        this.clientRepository = clientRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.shipmentRepository = shipmentRepository;
        this.anntoBusiness = anntoBusiness;
        this.userRepository = userRepository;
        this.scheduleBusiness = scheduleBusiness;
        this.warehouseRepository = warehouseRepository;
        this.anntoCustomerOrderRepository = anntoCustomerOrderRepository;
    }

    @Override
    public AccessDTO update(ShipmentUpdateDTO dto) {

        AccessDTO accessDTO = new AccessDTO();
        /**
         * 查询出库单号在mushiny中是否存在
         */
        String result = shipmentBusiness.checkCustomerOrder(dto);
        if(result.equalsIgnoreCase(Constant.ORDER_NUMBER_EXISTS)){
            log.error("安得发送的出库单号: " + dto.getCode() + " 在牧星中已存在");
            accessDTO.setMsg("出库单号已存在");
            accessDTO.setCode("1");
            return accessDTO;
        }
        /**
         * 先查询订单中商品在仓库中是否存在库存，数量是否够，如果不够，则不能进入系统
         */

        /*accessDTO = shipmentBusiness.checkStockUnit(dto);
        if(accessDTO.getCode().equals("1")){
            return accessDTO;
        }*/

        /**
         *   将数据存至ANNTO信息表；
         */
        accessDTO = shipmentBusiness.createAnntoCustomerOrder(dto);

        /**
         * 将信息存入wms库中
         */
        CustomerOrder customerOrder = shipmentBusiness.createCustomerOrder(dto);

        /**
         * 将发货点和sortCode保存
         */
        shipmentBusiness.saveSortCode(dto);
        /**
         * 出库单对应的是shipment，将信息存入shipment
         */
//        CustomerShipment shipment = shipmentBusiness.createCustomerShipment(dto);

        return accessDTO;
    }

    @Override
    public void confirm(String shipmentNo,String operatorNo, String operatorTime) {
//        shipmentBusiness.shipmentConfirm(shipmentNo,operatorNo,operatorTime);
    }

    @Override
    public AccessDTO cancel(ShipmentCancelDTO dto) {
        AccessDTO accessDTO = new AccessDTO();
        String clientNo = dto.getCompanyCode();
        String clientId = clientRepository.findByClientNo(clientNo).getId();

        Warehouse warehouse = warehouseRepository.getByWarehouseNo(dto.getWarehouseCode());
        /**
         * 需要确定货主编码跟之前的货主编码是否一样？？
         */
        String orderNo = dto.getCode();

        //如果取消出库单
        if(!"SO".equals(dto.getOrderType())) {
            log.error("要取消的单号：" + orderNo +" 不是出库单...");
           accessDTO.setCode("1");
           accessDTO.setMsg("该订单不是出库单！");
           return accessDTO;
        }

        CustomerOrder order = customerOrderRepository.getByOrderNoAAndWarehouseId(orderNo,warehouse.getId());
        if(order == null){
            log.error("要取消的单号：" + orderNo +" 在牧星库中不存在。。");
            accessDTO.setCode("1");
            accessDTO.setMsg("该出库单在牧星中不存在！");
            return accessDTO;
        }

        //获取订单对应的所有shipment
        List<CustomerShipment> shipments = shipmentRepository.getByCustomer(order);
        if(shipments == null || shipments.isEmpty()){
            order.setState(State.CANCELED);
            log.info("该出库单已取消成功。。。");
            accessDTO.setMsg("订单取消成功。");
            accessDTO.setCode("0");
            return accessDTO;
        }
        boolean allCancel = true;
        for (CustomerShipment shipment:shipments) {
            if (shipment.getState() < State.PROCESSABLE) {
                continue;
            } else {
                log.error("该出库单已开始拣货，不能取消。。。");
                accessDTO.setMsg("不能取消订单。。。");
                accessDTO.setCode("1");
                allCancel = false;
                break;
            }
        }
        if(allCancel){
            for (CustomerShipment s:shipments) {
                s.setState(State.CANCELED);
                log.info("该出库单已取消成功。。。");
                accessDTO.setMsg("订单取消成功。");
                accessDTO.setCode("0");
            }
            log.info("该出库单已取消成功。。。");
            accessDTO.setMsg("订单取消成功。");
            accessDTO.setCode("0");
        }

        return accessDTO;

    }

    @Override
    public void confirm(ShipmentConfirmDTO shipmentConfirmDTO) {
        CustomerShipment shipment=shipmentRepository.getByShipmentNo(shipmentConfirmDTO.getShipmentCode());
        anntoBusiness.confirmShipmet(shipmentConfirmDTO,shipment);
    }

    @Override
    public void confirmShipment(CustomerShipment customerShipment) {
        ShipmentConfirmDTO shipmentConfirmDTO = new ShipmentConfirmDTO();

//        AnntoCustomerOrder anntoCustomerOrder = anntoCustomerOrderRepository.getByCode(shipmentNo);
//        CustomerShipment shipment = shipmentRepository.getByShipmentNo(customerShipment.getShipmentNo());

        shipmentConfirmDTO.setCode(customerShipment.getShipmentNo());
        shipmentConfirmDTO.setCompanyCode(clientRepository.findOne(customerShipment.getClientId()).getClientNo());//货主编码  notnull
        shipmentConfirmDTO.setWarehouseCode(warehouseRepository.findOne(customerShipment.getId()).getWarehouseNo());//出库仓编码  notNull
        shipmentConfirmDTO.setShipmentType("PO");
        shipmentConfirmDTO.setOperateTime(customerShipment.getModifiedDate().toString());//出库完结时间  notnull
        shipmentConfirmDTO.setOperatorName(userRepository.findByUsername(customerShipment.getModifiedBy()).getName());//操作员姓名
        shipmentConfirmDTO.setOperatorCode(customerShipment.getModifiedBy());//操作员编码

        List<ShipmentConfirmItemsDTO> orderItems = new ArrayList<>();
        List<CustomerShipmentPosition> positions = customerShipment.getPositions();

        //获取annto传入数据，获取lineNo
        AnntoCustomerOrder order = anntoCustomerOrderRepository.getByCode(customerShipment.getCustomerOrder().getOrderNo());
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

        anntoBusiness.confirmShipmet(shipmentConfirmDTO,customerShipment);
    }

    public void checkOrderCode(String warehouseId, String orderCode){
        CustomerOrder customerOrder = customerOrderRepository.getByOrderNo(warehouseId,orderCode);
        if(customerOrder != null){
            throw new ApiException(ITFException.格式错误.toString(), "1");
        }
    }
}
