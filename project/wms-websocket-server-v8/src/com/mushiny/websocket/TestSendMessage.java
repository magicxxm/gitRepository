package com.mushiny.websocket;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by Tank.li on 2017/6/15.
 */

/**
 * @deprecated only for test
 */
public class TestSendMessage {
    public static void main(String[] args) {
        int i = 0;
        while (true) {
            try {
                sendMessage("新消息" + i + "黎庆剑测试!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(2000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    }

    private static void sendMessage(String s) throws Exception {
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // 消费者，消息接收者
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, "tcp://192.168.1.201:61616");

        // 构造从工厂得到连接对象
        connection = connectionFactory.createConnection();
        // 启动
        connection.start();
        // 获取操作连接
        session = connection.createSession(Boolean.FALSE,
                Session.AUTO_ACKNOWLEDGE);
        // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
        destination = session.createQueue("WEBSOCKET.RECEIVE.QUEUE");
        MessageProducer producer = session.createProducer(destination);
        producer.send(session.createTextMessage(s));
        System.out.println("发送成功" + s);
    }
}
