package com.mycompany.tradecapture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * author: Sudhir Kanala
 * Starting point of the Spring boot application
 */
@SpringBootApplication
@EnableScheduling
public class TradecaptureApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradecaptureApplication.class, args);
    }
}