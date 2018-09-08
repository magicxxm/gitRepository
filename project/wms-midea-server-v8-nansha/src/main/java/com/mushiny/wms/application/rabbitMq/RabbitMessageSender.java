package com.mushiny.wms.application.rabbitMq;

import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * Created by Tank.li on 2017/7/3.
 */
@Component
public class RabbitMessageSender implements RabbitTemplate.ConfirmCallback {
    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMessageSender.class);

    private String EXCHANGE = "exchange_other";
    private String routingKey = "WMS_WCS_POD_ADD_REMOVE";
    private RabbitTemplate rabbitTemplate;


    @Autowired
    public RabbitMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }



    public void sendMessage(Map msg, String exchange,String rout) {

        LOGGER.debug("开始发送pod更新{}", JSONUtil.toJSon(msg));
        try {
            rabbitTemplate.convertAndSend(exchange,rout, msg);
        } catch (Exception e) {
            LOGGER.error("发送pod更新失败,异常信息为{}", msg, e.getMessage());
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
