package com.example.springauthenticationui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
public class SpringAuthenticationUiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAuthenticationUiApplication.class, args);
    }

}
