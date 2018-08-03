package com.mushiny.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * Created by Tank.li on 2017/11/22.
 */
@Component
@org.springframework.core.annotation.Order(value = 99)
public class AuthorClient implements CommandLineRunner {
     private final RestTemplateBuilder builder;
    @Value("${com.mushiny.auth.url}")
    private String url;
    public static final String OK = "OK";
    @Autowired
    public AuthorClient(RestTemplateBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void run(String... strings) throws Exception {
        RestTemplate restTemplate = builder.build();
        String ss = null;
        try {
            ss = restTemplate.getForObject(url,String.class);
        } catch (Exception e) {
            //System.exit(-1);
        }
        if(!Objects.equals(ss,OK)){
            //System.exit(-1);
        }
        //System.out.println("验证通过!");
    }
}
