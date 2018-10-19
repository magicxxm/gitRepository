package com.mushiny.wms.test.web;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 * Created by Laptop-9 on 2018/10/18.
 */
public class RabbitMQ {
    private static final String QUEUE_NAME = "131756110817281593 RCS_WCS_ROBOT_RT_NET";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPort(5672);
        factory.setPassword("guest");
        factory.setHost("192.168.10.212");
        factory.setVirtualHost("/");
        //创建一个新的连接
        Connection connection = factory.newConnection();
        final Channel channel =  connection.createChannel();
        //消费者也需要定义队列 有可能消费者先于生产者启动
        channel.queueDeclare(QUEUE_NAME,true, false, true, null);
        Consumer consumer= new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                System.out.println(new String(body,"UTF-8"));
                System.out.println("消费消息成功---");
            }
        };
        //参数2 表示手动确认
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}

