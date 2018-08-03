package com.mushiny.test.mq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Created by Laptop-6 on 2017/11/23.
 */
public class TestMQReconnect {
    // 队列名称
    private final static String QUEUE_NAME = "hello";
    // 创建对像
    ConnectionFactory factory;
    // 联接信息
    Connection connection;
    // 频道
    Channel channel = null;
    // 获取数据队列
    QueueingConsumer consumer = null;

    /**
     * InitMQ 初始化MQ信息
     *
     * */
    public void InitMQ() {

        // 打开连接和创建频道，与发送端一样
        factory = new ConnectionFactory();
        factory.setHost("192.168.1.71");
        factory.setUsername("mushiny");
        factory.setPassword("mushiny");
        factory.setVirtualHost("/");
        factory.setPort(5672);
        // 关键所在，指定线程池
        ExecutorService service = Executors.newFixedThreadPool(10);
        factory.setSharedExecutor(service);
        // 设置自动恢复
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(1000);// 设置 没10s ，重试一次
        factory.setTopologyRecoveryEnabled(false);// 设置不重新声明交换器，队列等信息。
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            // 声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            // 设置一条条的应答
            channel.basicQos(0, 1, false);
            // 创建队列消费者
            consumer = new QueueingConsumer(channel);
            // 指定消费队列 ,第二个参数false表示不自动应答
            channel.basicConsume(QUEUE_NAME, false, consumer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TestMQReconnect mqReconnect = new TestMQReconnect();
        mqReconnect.InitMQ();
        mqReconnect.ReceiveMQ();
        mqReconnect.CycleSend();

    }


    /**
     * CycleSend 调用线程池循环发送数据<br/>
     *
     *
     * */
    public void CycleSend() {
        // 使用线程池
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        // 定时执行任务
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // 发送的消息
                String message = "hello world!" + UUID.randomUUID();
                // TODO Auto-generated method stub
                try {
                    if (!channel.isOpen()) {
                        // 初始化恢复
                        // InitMQ();
                        // System.out.println(" [x] 启动发送重联"+channel.isOpen());
                    }
                    if (channel.isOpen()) {
                        channel.basicPublish("", QUEUE_NAME, null,
                                message.getBytes());
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }

            }

        }, 1, 3, TimeUnit.SECONDS);

    }

    /**
     * ReceiveMQ 从MQ中收取数据 <br/>
     * 并且可以收获MQ信息数据
     *
     * */
    public void ReceiveMQ() {

        Thread thr = new Thread(new Runnable() {

            @Override
            public void run() {

                Cycle();
            }
        });

        thr.start();

    }

    // 是否断开
    boolean isBroken = false;

    // 循环读取数据

    protected void Cycle() {
        while (true) {

            // nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）
            QueueingConsumer.Delivery delivery;
            try {

                if (isBroken) {
                    //channel.basicRecover();

                    System.out.println(" [x] 开始接收数据可能会卡 ----"+ channel.isOpen());

                }
                if (channel.isOpen()) {
                    delivery = consumer.nextDelivery();
                    String message = new String(delivery.getBody());
                    //应答
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);

                    System.out.println(" [x] Received '" + message + "'");
                }
            } catch (ShutdownSignalException e) {
                try {

                    if (channel.isOpen()) {
                        // 异常情况恢复
                        consumer = new QueueingConsumer(channel);
                        channel.basicConsume(QUEUE_NAME, false, consumer);
                        isBroken = false;
                        System.out.println(" [x] 恢复成功 ----");
                    }


                } catch (Exception e1) {

                    isBroken = true;
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    System.out.println(" [x] 接收线程--- ShutdownSignalException-Exception");

                }

                // TODO Auto-generated catch block
                // e.printStackTrace();
            } catch (ConsumerCancelledException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println(" [x] 接收线程跳----ConsumerCancelledException");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println(" [x] 接收线程跳出 ----Exception");

                break;
            }

        }

    }


}
