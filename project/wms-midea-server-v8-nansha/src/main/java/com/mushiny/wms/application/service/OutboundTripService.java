package com.mushiny.wms.application.service;

import com.mushiny.wms.application.domain.OutboundInstruct;

/**
 * Created by Administrator on 2018/7/6.
 */
public interface OutboundTripService {
    void buildTrip();
    boolean cancelInstruct(OutboundInstruct instruct, String status);
}
