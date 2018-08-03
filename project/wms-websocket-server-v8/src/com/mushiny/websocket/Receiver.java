package com.mushiny.websocket;

/**
 * @deprecated only for test
 */

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @deprecated only for test
 */
public class Receiver {

    public static void receive(Set sessions) {
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // 消费者，消息接收者
        MessageConsumer consumer;
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, "tcp://192.168.1.201:61616");
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createQueue("WEBSOCKET.RECEIVE.QUEUE");

            consumer = session.createConsumer(destination);
            while (true) {
                // 设置接收者接收消息的时间，为了便于测试，这里谁定为100s
                TextMessage message = (TextMessage) consumer.receive(500000);
                if (message == null) {
                    continue;
                }
                System.out.println("收到消息" + message.getText());
                //basic.sendText(message.getText());
                Iterator iterator = sessions.iterator();
                System.out.println("客户端数量:" + sessions.size());
                while (iterator.hasNext()) {
                    javax.websocket.Session ss = (javax.websocket.Session) iterator.next();
                    boolean needClose = false;
                    if (ss.isOpen()) {
                        try {
                            ss.getBasicRemote().sendText(message.getText());
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(ss.getId() + "发生错误!");
                            needClose = true;
                            sessions.remove(ss);
                        }
                    } else {
                        needClose = true;
                        sessions.remove(ss);
                    }
                    if (needClose) {
                        try {
                            ss.close();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }
}