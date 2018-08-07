package com.mushiny.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Tank.li on 2017/7/3.
 */
@Component
public class RabbitMessageSender implements RabbitTemplate.ConfirmCallback {
    private final static Logger logger = LoggerFactory.getLogger(RabbitMessageSender.class);
    private String EXCHANGE = "exchange_other";
    private RabbitTemplate rabbitTemplate;


    @Autowired
    public RabbitMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String msg) {

    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println(" 回调id:" + correlationData);
        if (ack) {
            System.out.println("消息成功消费");
        } else {
            System.out.println("消息消费失败:" + cause);
        }
    }

}
