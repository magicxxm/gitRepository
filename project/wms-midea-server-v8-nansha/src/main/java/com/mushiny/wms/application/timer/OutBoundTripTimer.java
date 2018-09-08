package com.mushiny.wms.application.timer;

import com.mushiny.wms.application.service.OutboundTripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/7/12.
 */
@Component
public class OutBoundTripTimer {
    private static final Logger LOG = LoggerFactory.getLogger(OutBoundTripTimer.class);
    private final OutboundTripService outboundTripService;
    @Autowired
    public OutBoundTripTimer(OutboundTripService outboundTripService) {
        this.outboundTripService = outboundTripService;
    }
    @Scheduled(fixedDelay=4000,initialDelay = 10000)
   // @Async
    public void build() {
        try {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("开始运行出库线程");
            }
            outboundTripService.buildTrip();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

    }
}
