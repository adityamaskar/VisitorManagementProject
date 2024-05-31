package com.visitorproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin // comment this if using gateway.
public class VisitorProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(VisitorProjectApplication.class, args);
    }

}
