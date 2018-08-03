/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.mq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

/**
 * @author aricochen
 */
public class MQSender extends MQContainer {
    private Destination queue = null;
    private String queueName;

    public MQSender(String queueName) {
        super(queueName);
        this.queueName = queueName;
    }

    @Override
    protected void init(String queueName) {
        mqSession = pool.getSession();
        /*try {
            queue = mqSession.createQueue(queueName);
        } catch (JMSException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 发送json串到queue
     *
     * @param jsonData
     * @throws JMSException
     */
    public void sendMessage(String jsonData) throws JMSException {
        queue = mqSession.createQueue(queueName);
        MessageProducer producer = mqSession.createProducer(queue);
        TextMessage rtMQMessage = mqSession.createTextMessage();
        rtMQMessage.setText(jsonData);
        producer.send(rtMQMessage);
        pool.returnSession(mqSession);
    }
}
