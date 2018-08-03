
package com.mushiny.websocket.config;

import com.mushiny.comm.JSONUtil;
import com.mushiny.jdbc.service.JdbcService;
import com.mushiny.websocket.TaskSchedul.TaskSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;



/**
 * @author:
 * @Description: Created by wangjianwei on 2017/9/22.
 **/




@Component
@Order(1)
public class ShiftmentConfig implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShiftmentConfig.class);
    @Autowired
    private JdbcService jdbcservice;

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("初始化捡货订单");
        List<Map<String, String>> shiftMents = jdbcservice.getShipment();
        if (!CollectionUtils.isEmpty(shiftMents)) {
            LOGGER.info("有未处理的捡货订单\n{}", JSONUtil.toJSon(shiftMents));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (Map<String, String> task : shiftMents) {
                        TaskSchedule.schedule(task);
                    }
                }
            }, TimeUnit.SECONDS.toSeconds(5));


        } else {
            LOGGER.info("无未处理的捡货订单");
        }

    }


}


