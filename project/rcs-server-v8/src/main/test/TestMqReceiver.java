import com.rabbitmq.client.*;
import mq.RCSMainServer;
import mq.SubjectManager;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Laptop-6 on 2017/10/23.
 */
public class TestMqReceiver {



    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setPassword("mushiny");
        factory.setUsername("mushiny");

        Connection publishConnection = factory.newConnection();
        Channel channel = publishConnection.createChannel();

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, "section4", "TEST_MQ");

        Consumer consumer = new DefaultConsumer(channel)
        {
            private int count = 0;
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {

                count = count +1;
                System.out.println(count);


            }
        };
        channel.basicConsume(queueName, true, consumer);





    }




}
