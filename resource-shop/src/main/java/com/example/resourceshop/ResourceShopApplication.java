package com.example.resourceshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ResourceShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceShopApplication.class, args);
    }

}
