package com.mushiny.service;

import com.mushiny.web.dto.CustomerShipmentDTO;

/**
 * Created by 123 on 2018/5/2.
 */
public interface OutBoundService {

    void createOutbound(CustomerShipmentDTO dto);
}
