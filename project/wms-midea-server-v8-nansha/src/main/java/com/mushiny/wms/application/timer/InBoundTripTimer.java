package com.mushiny.wms.application.timer;

import com.mushiny.wms.application.service.InboundTripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component

public class InBoundTripTimer {
    private static final Logger LOG = LoggerFactory.getLogger(InBoundTripTimer.class);

    private final InboundTripService inboundTripService;
@Autowired
    public InBoundTripTimer(InboundTripService inboundTripService) {
        this.inboundTripService = inboundTripService;
    }

    @Scheduled(fixedDelay=4000,initialDelay = 10000)
    public void build() {
        try {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("开始运行入库线程");
            }

            inboundTripService.buildTrip();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
