package com.mushiny.websocket;

import com.mushiny.comm.JSONUtil;
import com.mushiny.websocket.TaskSchedul.DigitallabelMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 工作站与硬件映射关系
 * Created by Tank.li on 2017/6/19.
 */
@Component
@Deprecated
public class WorkStationHardwareMapper implements ApplicationListener<ContextRefreshedEvent> {
    private Map mapper = Collections.synchronizedMap(new HashMap<>());
    private final static Logger logger = LoggerFactory.getLogger(WorkStationHardwareMapper.class);

    public Map getMapper() {
        return mapper;
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*@Scheduled(fixedDelay = 1000*3)*/
    public void load() {

        Map data = new HashMap();
        data.put("sectionId", "ec229eb7-7e2b-43a8-b1c7-91bd807e91cf");
        data.put("pod", "P0000141A");
        data.put("workstation", "200cce84-c8c8-436e-b602-a17658a0897e");

        Map data2 = new HashMap();
        data2.put("sectionId", "37a2200c-dfba-4ec7-b4dd-743886db8fe2");
        data2.put("pod", "P0000029A");
        data2.put("workstation", "37a2200c-dfba-4ec7-b4dd-743886db8fe2");
        rabbitTemplate.convertAndSend("WEBSOCKET.TEST.RECEIVE2.QUEUE",  DigitallabelMessage.getDigitallabelMess("09ae0bc5-0468-4d7b-bd68-15718fe29761","wjw"));
        // rabbitTemplate.convertAndSend("e4","k4", JSONUtil.mapToJSon(data ));


    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //load();
    }
}
