package com.mushiny.rabbitmq;

import com.mushiny.comm.CommonUtils;
import com.mushiny.mq.MQCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
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
    private String routingKey = MQCommon.RABBITMQSERVER_SENDQUEUE;
    private RabbitTemplate rabbitTemplate;


    @Autowired
    public RabbitMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * @param msg     接收到的byte数据
     * @param routing rabbitMq队列名称
     * @return
     * @description
     * @date: 2017-07-25 19:41:28
     * @author:wangjianwei
     */
    private void sendJsonMessage(byte[] msg, String routing) {
        byte[] body = msg;
        Map data = (Map) CommonUtils.toObject(body);
        String result = CommonUtils.mapToJSon(data);
        rabbitTemplate.convertAndSend(routing, result);
    }

    public void RCS_WCS_AGV_STATUS_NET(Message message) {
        sendJsonMessage(message.getBody(), MQCommon.RCS_WCS_AGV_STATUS_NET);

    }

    public void RCS_WCS_ROBOT_STATUS_NET(Message message) {
        sendJsonMessage(message.getBody(), MQCommon.RCS_WCS_ROBOT_STATUS_NET);

    }

    public void RCS_WCS_ROBOT_RT_NET(Message message) {
        sendJsonMessage(message.getBody(), MQCommon.RCS_WCS_ROBOT_RT_NET);

    }

    public void sendMessage(String msg) {
//        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        // SimpleMessageListenerContainer simpleMessageListenerContainer
        LOGGER.debug("控制灯信息{}", msg);
        try {
            rabbitTemplate.convertAndSend(routingKey, msg);
        } catch (Exception e) {
            LOGGER.error("发送信息{}失败,异常信息为{}", msg, e.getMessage());
        }

        // rabbitTemplate.convertAndSend(EXCHANGE, routingKey, msg, correlationId);
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
