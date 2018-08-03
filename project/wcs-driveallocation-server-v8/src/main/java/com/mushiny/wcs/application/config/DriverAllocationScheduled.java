/*
package com.mushiny.wcs.application.config;

import com.mushiny.wcs.common.context.ApplicationBeanContextAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

*/
/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/10/17.
 *//*

@Component
@Order(2)
public class DriverAllocationScheduled implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverAllocationScheduled.class);
    @Autowired
    private DriveAllocationRunnable driveAllocationRunnable;
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void run(String... args) {
        LOGGER.debug("开始启动线程分配小车");
        try{
            service.scheduleAtFixedRate(driveAllocationRunnable, 5, 7, TimeUnit.SECONDS);
        }catch (Exception e)
        {
            driveAllocationRunnable= ApplicationBeanContextAware.getBean(DriveAllocationRunnable.class);
            service.scheduleAtFixedRate(driveAllocationRunnable, 5, 7, TimeUnit.SECONDS);
            LOGGER.error(e.getMessage(),e);
        }

    }


}
*/
