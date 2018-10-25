package com.mushiny.wms.application.timer;

import com.mushiny.wms.application.service.impl.PutBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class PutBackPodTimer {
    private static final Logger LOG = LoggerFactory.getLogger(PutBackPodTimer.class);

    private final PutBackService putBackService;
   @Autowired
    public PutBackPodTimer(PutBackService putBackService) {
        this.putBackService = putBackService;
    }

    @Scheduled(fixedDelay=30000,initialDelay = 10000)

    public void runDriveAllocation() {
        try {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("开始运行pod入库线程");
            }
            putBackService.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
