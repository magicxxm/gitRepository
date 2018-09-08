package com.mushiny.wms.application.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;


/**
 * Created by Tank.li on 2017/7/3.
 */

@Configuration
@ConditionalOnBean(Environment.class)
public class RabbitConfig {
    private static Logger LOGGER = LoggerFactory.getLogger(RabbitConfig.class);
    @Autowired
    private Environment environment;
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setAddresses(environment.getProperty("spring.rabbitmq.host"));
        connectionFactory.setPort(Integer.parseInt(environment.getProperty("spring.rabbitmq.port")));
        connectionFactory.setUsername(environment.getProperty("spring.rabbitmq.username"));
        connectionFactory.setPassword(environment.getProperty("spring.rabbitmq.password"));
        connectionFactory.setVirtualHost(environment.getProperty("spring.rabbitmq.virtualhost"));

        connectionFactory.setPublisherConfirms(true); //必须要设置
        StringBuilder sb=new StringBuilder();
        sb.append("地址:"+environment.getProperty("spring.rabbitmq.host"));
        sb.append("端口:"+environment.getProperty("spring.rabbitmq.port"));
        sb.append("用户:"+environment.getProperty("spring.rabbitmq.username"));
        sb.append("virtualhost:"+environment.getProperty("spring.rabbitmq.virtualhost"));
        LOGGER.info("rabbit mq 的连接信息为\n"+sb.toString());
        return connectionFactory;
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型

    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }






}
