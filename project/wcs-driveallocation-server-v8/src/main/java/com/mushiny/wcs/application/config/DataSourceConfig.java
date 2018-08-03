package com.mushiny.wcs.application.config;

import com.mushiny.wcs.application.service.impl.DriveAllocationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/1/18.
 */
@Configuration
public class DataSourceConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    private Environment env;
    @Bean
    public DataSource createDataSource(){

        String ip=env.getProperty("ip");
        String url="";
        if(!StringUtils.isEmpty(ip))
        {
            url="jdbc:mysql://"+ip+":3306/wms_v8_staging?useUnicode=true&characterEncoding=utf8&useSSL=false";

        }else{
            url=env.getProperty("spring.datasource.url");
        }
        DataSourceBuilder db= DataSourceBuilder.create();
        db.url(url);
        db.username(env.getProperty("spring.datasource.username"));
        db.password(env.getProperty("spring.datasource.password"));
        db.driverClassName(env.getProperty("spring.datasource.driverClassName"));
        DataSource ds=db.build();
        return ds;
    }

}
