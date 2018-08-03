package com.mushiny.wcs.application.timer;

import com.mushiny.wcs.application.service.DriveAllocationService;
import com.mushiny.wcs.application.service.impl.DriveAllocationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class DriveAllocationTimer {
    private static final Logger LOG = LoggerFactory.getLogger(DriveAllocationTimer.class);

    private final DriveAllocationService driveAllocationService;

    public DriveAllocationTimer(DriveAllocationService driveAllocationService) {
        this.driveAllocationService = driveAllocationService;
    }

   @Scheduled(fixedDelay=4000,initialDelay = 10000)
    public void runDriveAllocation() {
        try {
            driveAllocationService.runDriveAllocation();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
