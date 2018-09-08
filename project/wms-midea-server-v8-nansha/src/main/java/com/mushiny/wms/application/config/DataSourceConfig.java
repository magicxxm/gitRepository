package com.mushiny.wms.application.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/1/18.
 */
@Configuration
@ConditionalOnBean(Environment.class)
public class DataSourceConfig{
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);
    @Autowired
    private Environment env;
    @Bean
    public DataSource createDataSource(){

        String url=env.getProperty("spring.datasource.url");

        DataSourceBuilder db= DataSourceBuilder.create();
        db.url(url);
        db.username(env.getProperty("spring.datasource.username"));
        db.password(env.getProperty("spring.datasource.password"));
        db.driverClassName(env.getProperty("spring.datasource.driverClassName"));
        DataSource ds=db.build();
        StringBuilder sb=new StringBuilder();
        sb.append("地址:"+url);

        LOGGER.info("数据库" +
                "的连接信息为\n"+sb.toString());
        return ds;
    }


}
