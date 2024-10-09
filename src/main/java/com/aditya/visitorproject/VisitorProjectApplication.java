package com.aditya.visitorproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VisitorProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(VisitorProjectApplication.class, args);
    }

}
