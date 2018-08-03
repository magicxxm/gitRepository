package com.mushiny;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/*@EnableJpaRepositories(basePackages = {"com.mushiny.wcs"},
        repositoryFactoryBeanClass = SubRepositoryFactoryBean.class//指定自己的工厂类
)*/
public class ServerApplication  {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
