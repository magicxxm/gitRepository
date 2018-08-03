package com.mushiny.wcs.application.business.rabbitMq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Tank.li on 2017/7/3.
 */
@Component
public class RabbitMessageSender implements RabbitTemplate.ConfirmCallback {
    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMessageSender.class);
    private String EXCHANGE = "exchange_other";
    private String routingKey = "WMS.ICQA.POD.QUEUE";
    private RabbitTemplate rabbitTemplate;


    @Autowired
    public RabbitMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    //    @Scheduled(fixedRateString="10000")
    public void sendMessage(String msg){

        LOGGER.debug("发送pod信息{}",msg);
        try{
            rabbitTemplate.convertAndSend(routingKey,msg);
        }catch (Exception e){
            LOGGER.error("发送pod信息{}失败,异常信息为{}",msg,e.getMessage());
        }


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
