package com.mushiny.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;


/**
 * Created by Tank.li on 2017/7/3.
 */
@Configuration
public class MqConfig {
    private static Logger LOGGER = LoggerFactory.getLogger(MqConfig.class);
    private static final String[] EXCHANGEKEYS = new String[]{"exchange_other"};
    private static final String[] QUEUES = new String[]{
            "WEBSOCKET.RECEIVE.QUEUE", "WMS.ICQA.POD.QUEUE",
            "WEBSOCKET.SEND.QUEUE", "WEBSOCKET.RCS.RECEIVE.QUEUE"};


    @Autowired
    private Environment environment;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        String ip=environment.getProperty("ip");
        if(StringUtils.isEmpty(ip))
        {
            ip=environment.getProperty("spring.rabbitmq.host");
        }
        connectionFactory.setAddresses(ip);
        connectionFactory.setPort(Integer.parseInt(environment.getProperty("spring.rabbitmq.port")));
        connectionFactory.setUsername(environment.getProperty("spring.rabbitmq.username"));
        connectionFactory.setPassword(environment.getProperty("spring.rabbitmq.password"));
        connectionFactory.setVirtualHost(environment.getProperty("spring.rabbitmq.virtualhost"));
        connectionFactory.setPublisherConfirms(true); //必须要设置
        return connectionFactory;
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型

    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }


  /*  @Bean
    public List<Declarable> ds() {
        // 初始化exchange
        List<Declarable> result=new ArrayList<>();
        List<Exchange> exchangesTemp=new ArrayList<>();
        for(String exchange:EXCHANGEKEYS)
        {
            exchangesTemp.add( new DirectExchange(exchange,false, false, null));
        }
        //初始化队列
        List<Queue> queuesTemp=new ArrayList<>();
        for (String queue:QUEUES)
        {
            queuesTemp.add(new Queue(queue, false, false,false ));
        }
        //绑定队列
        List<Binding> bindings=new ArrayList<>();
        for(Exchange exchange:exchangesTemp)
        {
            for(Queue queue:queuesTemp)
            {
                bindings.add(new Binding(queue.getName(),Binding.DestinationType.QUEUE,exchange.getName(),queue.getName(),null));

            }

        }

        result.addAll(exchangesTemp);
        result.addAll(queuesTemp);
        result.addAll(bindings);
        LOGGER.info("初始化队列成功 {} ", JSONUtil.toJSon(result));
        return result;

    }*/


}
