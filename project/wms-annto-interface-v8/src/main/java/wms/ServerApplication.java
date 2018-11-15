package wms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ServerApplication.class, args);
    }
}