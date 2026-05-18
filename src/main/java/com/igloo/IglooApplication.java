package com.igloo;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IglooApplication {
    public static void main(String[] args) {

        System.out.println("DB_PASSWORD = " + System.getenv("DB_PASSWORD"));

        SpringApplication.run(IglooApplication.class, args);
    }
}
