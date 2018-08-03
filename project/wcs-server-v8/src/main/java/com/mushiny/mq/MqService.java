package com.mushiny.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by Tank.li on 2017/7/3.
 */
@Service
public class MqService {
    @Autowired
    private MessageSender messageSender;
    @Scheduled(fixedDelay = 10000l)
    public void sendMessage(){
        //messageSender.WCS_RCS_AGV_PARKING_NEAREST("黎庆剑爱温妍妍");
    }
}
