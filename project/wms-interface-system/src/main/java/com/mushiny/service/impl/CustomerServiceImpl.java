package com.mushiny.service.impl;

import com.mushiny.business.CustomerOrderBusiness;
import com.mushiny.common.crud.AccessDTO;
import com.mushiny.model.CustomerOrder;
import com.mushiny.model.CustomerShipment;
import com.mushiny.model.Warehouse;
import com.mushiny.repository.CustomerShipmentRepository;
import com.mushiny.repository.ShipmentPriorityRepository;
import com.mushiny.repository.WarehouseRepository;
import com.mushiny.service.CustomerService;
import com.mushiny.utils.DateUtil;
import com.mushiny.utils.StringUtil;
import com.mushiny.web.dto.CustomerShipmentDTO;
import com.mushiny.web.dto.Priority;
import com.mushiny.web.dto.PriorityPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Created by 123 on 2018/2/1.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerOrderBusiness customerOrderBusiness;
    private final CustomerShipmentRepository shipmentRepository;
    private final WarehouseRepository warehouseRepository;
    private final ShipmentPriorityRepository shipmentPriorityRepository;

    @Autowired
    public CustomerServiceImpl(CustomerOrderBusiness customerOrderBusiness,
                               CustomerShipmentRepository shipmentRepository,
                               ShipmentPriorityRepository shipmentPriorityRepository,
                               WarehouseRepository warehouseRepository) {
        this.customerOrderBusiness = customerOrderBusiness;
        this.shipmentRepository = shipmentRepository;
        this.warehouseRepository = warehouseRepository;
        this.shipmentPriorityRepository = shipmentPriorityRepository;
    }

    @Override
    public AccessDTO createCustomerShipment(CustomerShipmentDTO dto) {
        log.info("开始同步出库单信息到CustomerShipment表中。。。");

        AccessDTO accessDTO = new AccessDTO();

        if (dto.getWarehouseNo() == null || "".equalsIgnoreCase(dto.getWarehouseNo())) {
            log.info("出库单 ORDER ： " + dto.getOrderNo() + " 仓库编号为空。。。");
            accessDTO.setMsg("出库单的仓库编号不能为空");
            return accessDTO;
        }
        Warehouse warehouse = warehouseRepository.getByWarehouseNo(dto.getWarehouseNo());

        if (warehouse == null) {
            log.info("出库单 ORDER ： " + dto.getOrderNo() + " 仓库为空,需新增仓库信息。。。");
            accessDTO.setMsg("出库单的仓库编号在系统中不存在");
            accessDTO.setCode("2");
            return accessDTO;
        }

        //如果订单的优先级不是普通的，根据订单优先级改变订单发货时间点
        String ztbpri = dto.getZtbpri();
        LocalDateTime deliveryTime = null;
        deliveryTime = DateUtil.getLocalDateTime(dto.getDeliveryDate());
        int priproty = StringUtil.stringToint(ztbpri);

        /**
         * 未合并订单行 修改优先级后
         */
        /*accessDTO = customerOrderBusiness.createShipment(dto, warehouse, priproty,deliveryTime);
        if("0".equalsIgnoreCase(accessDTO.getCode())){
            log.info("出库单： " +dto.getOrderNo() +" 同步成功");
        }*/
        /**
         * 合并订单行版本
         */
        CustomerOrder order = customerOrderBusiness.createOrder(dto, warehouse, priproty,deliveryTime);

        //合并订单中相同商品的不同订单行
        CustomerShipment shipment = customerOrderBusiness.createShipment(order);

        if(order == null || shipment == null){
            log.info("出库单： " + dto.getOrderNo() + " 同步失败");
            accessDTO.setMsg("出库单： " + dto.getOrderNo() + " 同步失败");
            accessDTO.setCode("1");
        }else {
            log.info("出库单： " + dto.getOrderNo() + " 同步成功");
        }

        accessDTO.setOrderNo(dto.getOrderNo());

        return accessDTO;
    }

    @Override
    public AccessDTO updateDeliveryTime(Priority dto) {
        AccessDTO accessDTO = new AccessDTO();

        for (PriorityPosition position : dto.getPositions()) {
            Warehouse warehouse = warehouseRepository.getByWarehouseNo(position.getWarehouseNo());

            if (warehouse == null) {
                log.info("出库单 ORDER ： " + position.getOrderNo() + " 仓库为空,需新增仓库信息。。。");
                accessDTO.setMsg("出库单的仓库编号在系统中不存在");
                accessDTO.setCode("1");
                return accessDTO;
            }
            //获取需要修改优先级的订单
            CustomerShipment shipment = shipmentRepository.getByShipmentNoAndWarehouse(position.getOrderNo(), warehouse.getId());

            if(shipment == null){
                log.info("批次 ：" +position.getOrderNo() +" 在系统中不存在。。。");
                continue;
            }

            //修改订单的优先级
            int priproty = StringUtil.stringToint(position.getPriority());

            log.info("订单： " + position.getOrderNo() + " 原始优先级是 ：" + shipment.getPriority());
            shipment.setPriority(priproty);
            log.info("订单： " + position.getOrderNo() + " 修改后的优先级是 ：" + shipment.getPriority());

            log.info("订单： " + position.getOrderNo() + " 优先级修改成功。。");
        }

        return accessDTO;
    }

}
