package com.mushiny.wms.application.service;

import com.mushiny.wms.application.domain.InboundInstruct;

/**
 * Created by Administrator on 2018/7/6.
 */
public interface InboundTripService {
    void buildTrip();
    boolean cancelInstruct(InboundInstruct instruct, String status);
}
