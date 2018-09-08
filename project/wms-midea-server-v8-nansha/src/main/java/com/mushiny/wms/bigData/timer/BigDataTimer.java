package com.mushiny.wms.bigData.timer;

import com.mushiny.wms.bigData.service.WareHousePositionService;
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
public class BigDataTimer {
    private static final Logger LOG = LoggerFactory.getLogger(BigDataTimer.class);
    private final WareHousePositionService wareHousePositionService;

    @Autowired
    public BigDataTimer(WareHousePositionService wareHousePositionService) {
        this.wareHousePositionService = wareHousePositionService;
    }


    @Scheduled(fixedDelay=4000,initialDelay = 10000)
    @Async
    public void execute() {
        try {
            wareHousePositionService.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

    }
}
