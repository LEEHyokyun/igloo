package com.igloo;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IglooApplication {

    @PostConstruct
    public void check() {
        System.out.println("DB_PASSWORD = " + System.getenv("DB_PASSWORD"));
    }

    public static void main(String[] args) {



        SpringApplication.run(IglooApplication.class, args);
    }
}
