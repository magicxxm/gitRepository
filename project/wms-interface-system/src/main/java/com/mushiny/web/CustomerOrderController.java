package com.mushiny.web;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.service.CustomerService;
import com.mushiny.service.OutBoundService;
import com.mushiny.web.dto.CustomerOrderDTO;
import com.mushiny.web.dto.CustomerShipmentDTO;
import com.mushiny.web.dto.Priority;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 123 on 2018/2/1.
 */
@RestController
@RequestMapping("/wms/mushiny/")
public class CustomerOrderController {
    private final Logger log = LoggerFactory.getLogger(AdviceReceiptController.class);

    private final CustomerService customerService;
    private final OutBoundService outBoundService;

    @Autowired
    public CustomerOrderController(CustomerService customerService,OutBoundService outBoundService){
        this.customerService = customerService;
        this.outBoundService = outBoundService;
    }

    @RequestMapping(value = "/shipment/create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> create(@RequestBody CustomerShipmentDTO dto) {
        JSONObject jsonObject = JSONObject.fromObject(dto);
        log.info("出库单同步  接收到的信息是：==> " + jsonObject);
        //创建outBound
        outBoundService.createOutbound(dto);

        AccessDTO accessDTO = customerService.createCustomerShipment(dto);

        return ResponseEntity.ok(accessDTO);
    }

    @RequestMapping(value = "/priority/update",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> update(@RequestBody Priority dto) {
        JSONObject jsonObject = JSONObject.fromObject(dto);
        log.info("修改订单优先级  接收到的信息是：==> " + jsonObject);
        AccessDTO accessDTO = customerService.updateDeliveryTime(dto);

        return ResponseEntity.ok(accessDTO);
    }
}
