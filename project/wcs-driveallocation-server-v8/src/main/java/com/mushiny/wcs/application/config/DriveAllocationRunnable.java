package com.mushiny.wcs.application.config;

import com.mushiny.wcs.application.service.DriveAllocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/10/18.
 */
//@Component
public class DriveAllocationRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriveAllocationRunnable.class);
    @Autowired
    private DriveAllocationService driveAllocationService;

    @Override
    public void run() {
        try {
            driveAllocationService.runDriveAllocation();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }
}
