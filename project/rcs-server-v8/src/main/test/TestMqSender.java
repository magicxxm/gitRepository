import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import mq.SubjectManager;
import org.apache.commons.lang.SerializationUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Laptop-6 on 2017/10/23.
 */
public class TestMqSender {



    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setPassword("mushiny");
        factory.setUsername("mushiny");

        Connection publishConnection = factory.newConnection();
        Channel channel = publishConnection.createChannel();


        for(int i = 0; i < 100001; i++){
            String temp = "中国人民万岁+"+i;
            channel.basicPublish("section4", "TEST_MQ", null, temp.getBytes());
            System.out.println("发送数目：count=" + (i+1));
        }




    }




}
