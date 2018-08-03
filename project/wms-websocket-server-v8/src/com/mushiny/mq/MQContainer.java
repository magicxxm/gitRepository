/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.mq;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @author aricochen
 */
public abstract class MQContainer {
    private static Logger logger = LoggerFactory.getLogger(MQContainer.class);
    protected Session mqSession;
    private Boolean isConnectionSucess = false;
    protected MQSessionPool pool;

    public MQContainer(String queueName) {
        connectMQServer();
        init(queueName);
    }

    protected abstract void init(String queueName);

    /*
     连接MQ SERVER
     */
    public void connectMQServer() {
        pool = new MQSessionPool();
        try {
            while (!isConnectionSucess) {
                pool.getConn().setExceptionListener(jmse -> {
                    isConnectionSucess = false;
                    logger.warn("无法连接到MQ SERVER,正在自动重新连接...");
                });
                logger.warn("MQ已连接...");
                isConnectionSucess = true;
            }
        } catch (JMSException jmsexception) {
            jmsexception.printStackTrace();
        }
    }

    public boolean isConnection() {
        return isConnectionSucess;
    }
}
