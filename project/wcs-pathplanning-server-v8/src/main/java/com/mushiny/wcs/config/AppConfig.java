package com.mushiny.wcs.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/9/6.
 */
@Configuration
public class AppConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(30); //线程池活跃的线程数
        pool.setMaxPoolSize(45); //线程池最大活跃的线程数
        pool.setQueueCapacity(50); // 队列的最大容量
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }
}
