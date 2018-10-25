package com.mushiny.wms.application.timer;

import com.mushiny.wms.application.service.impl.EmptyPodCarryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class EmptyPodCarryTimer {
    private static final Logger LOG = LoggerFactory.getLogger(EmptyPodCarryTimer.class);

    private final EmptyPodCarryServiceImpl emptyPodCarryServiceImpl;

    @Autowired
    public EmptyPodCarryTimer(EmptyPodCarryServiceImpl emptyPodCarryServiceImpl) {
        this.emptyPodCarryServiceImpl = emptyPodCarryServiceImpl;
    }

   @Scheduled(fixedDelay=20000,initialDelay = 10000)

    public void runDriveAllocation() {
        try {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("开始运行运输pod入库线程");
            }
            emptyPodCarryServiceImpl.runEmptyPodCarry();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
