package com.mushiny.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

/**
 * On 2017/6/6.
 *
 * @author wangdong
 *         <p>
 *         TODO : 订阅MQ发布的硬件信息
 */
public class MQReceiver extends MQContainer {
    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);
    private Destination destination = null;
    private String queueName;

    public MQReceiver(String queueName) {
        super(queueName);
        this.queueName = queueName;
    }

    @Override
    protected void init(String queueName) {
        logger.debug("queueName:" + queueName);
        mqSession = pool.getSession();
        /*try {
            destination = mqSession.createQueue(queueName);
        } catch (JMSException e) {
            e.printStackTrace();
        }*/
    }

    //接收MQ消息
    public void receiveMapMessage() {
        logger.debug("从MQ接收消息....");
        try {
            //这里destination会莫名消失 TODO
            destination = mqSession.createQueue(queueName);
            MessageConsumer consumer = mqSession.createConsumer(destination);
            consumer.setMessageListener(messageListener);
            //mqSession.commit();
            //consumer.close();
        } catch (JMSException e) {
            logger.error("创建消息消费会话失败!", e);
            e.printStackTrace();
            try {
                mqSession.rollback();
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        }
    }

    private MessageListener messageListener;

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }
}
