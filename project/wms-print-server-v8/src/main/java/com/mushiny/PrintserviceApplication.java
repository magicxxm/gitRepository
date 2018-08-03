package com.mushiny;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PrintserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrintserviceApplication.class, args);
    }
}
