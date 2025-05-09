package com.example.sfmproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication (exclude = {SecurityAutoConfiguration.class})
public class AuthMicroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthMicroServiceApplication.class, args);
    }

}
