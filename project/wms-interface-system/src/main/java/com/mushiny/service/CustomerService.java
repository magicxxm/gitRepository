package com.mushiny.service;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.web.dto.CustomerOrderDTO;
import com.mushiny.web.dto.CustomerShipmentDTO;
import com.mushiny.web.dto.Priority;

/**
 * Created by 123 on 2018/2/1.
 */
public interface CustomerService {

    AccessDTO createCustomerShipment(CustomerShipmentDTO dto);

    AccessDTO updateDeliveryTime(Priority dto);
}
