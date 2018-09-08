package com.mushiny.wms.application.timer;

import com.mushiny.wms.application.service.DriveAllocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component

public class DriveAllocationTimer {
    private static final Logger LOG = LoggerFactory.getLogger(DriveAllocationTimer.class);

    private final DriveAllocationService driveAllocationService;

    public DriveAllocationTimer(DriveAllocationService driveAllocationService) {
        this.driveAllocationService = driveAllocationService;
    }
     @Scheduled(fixedDelay=4000,initialDelay = 10000)
     @Async
    public void runDriveAllocation() {
        try {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("开始运行分车线程");
            }
            driveAllocationService.runDriveAllocation();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
