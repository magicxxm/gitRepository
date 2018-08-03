package com.mushiny.wcs.application.rabbitMq;

import com.mushiny.wcs.application.event.MapNodeReflushEvent;
import com.mushiny.wcs.common.context.ApplicationBeanContextAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/11/14.
 */
@Component
public class mapRabbitMqListener {
    private static Logger LOGGER = LoggerFactory.getLogger(mapRabbitMqListener.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "WEBSOCKET.TEST.RECEIVE.QUEUE")
    public void podObBoundMapMessage(String message) {

        try {
            ApplicationContext context = ApplicationBeanContextAware.getApplicationContext();
            context.publishEvent(new MapNodeReflushEvent(context, message));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    /*  @Scheduled(fixedDelay = 1000*50)*/
    public void test() {
        rabbitTemplate.convertAndSend("WEBSOCKET.TEST.RECEIVE.QUEUE", "test----");
    }
}
