package com.example.springzuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableZuulProxy
public class SpringZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringZuulApplication.class, args);
    }

}
