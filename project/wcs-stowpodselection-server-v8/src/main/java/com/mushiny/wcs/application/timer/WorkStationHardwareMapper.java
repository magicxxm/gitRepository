/*
package com.mushiny.wcs.application.timer;

import com.mushiny.wcs.application.business.rabbitMq.RabbitMessageSender;
import com.mushiny.wcs.common.utils.JSONUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

*/
/**
 * 工作站与硬件映射关系
 * Created by Tank.li on 2017/6/19.
 *//*

@Component

public class WorkStationHardwareMapper  {
    private Map mapper = Collections.synchronizedMap(new HashMap<>());
    private final static Logger logger = LoggerFactory.getLogger(WorkStationHardwareMapper.class);

    public Map getMapper() {
        return mapper;
    }

    @Autowired
    private RabbitMessageSender rabbitMessageSender;


    @Scheduled(fixedDelay = 1000*20)
    public void load() {

        Map data = new HashMap();
        data.put("times", 1);
        data.put("stocktakingId", "88d906ff-e6ed-439a-8da2-5864945a6eca");
        List  pods=new ArrayList();
        Map tt=new HashMap();

        tt.put("face","A");
        tt.put("podId","0312ee24-22e6-437c-af2b-e568771888ae");

            Map tt2=new HashMap();

            tt2.put("face","A");

            tt2.put("podId","04b64872-57c4-4325-b8fb-0357e20d9e86");

            Map tt3=new HashMap();

            tt3.put("face","A");

            tt3.put("podId","0945ec45-46ce-4981-ac2a-8a147355ba77");


            Map tt4=new HashMap();

            tt4.put("face","A");

            tt4.put("podId","0b4f5c9d-826a-400a-9bd9-b02f2a291540");

            pods.add(tt);
            pods.add(tt2);
            pods.add(tt3);
            pods.add(tt4);



            data.put("workStationId", "88d906ff-e6ed-439a-8da2-5864945a6eca");
            data.put("pods",pods);

        rabbitMessageSender.sendMessage(JSONUtil.mapToJSon(data ));


    }



}
*/
