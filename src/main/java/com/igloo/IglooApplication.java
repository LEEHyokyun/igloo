package com.igloo;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class IglooApplication {
    public static void main(String[] args) {

        SpringApplication.run(IglooApplication.class, args);
    }
}
