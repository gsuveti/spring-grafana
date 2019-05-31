package com.example.resourceblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ResourceBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceBlogApplication.class, args);
    }

}
