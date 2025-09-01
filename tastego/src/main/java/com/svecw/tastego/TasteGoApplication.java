package com.svecw.tastego;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.svecw.tastego.repository")
public class TasteGoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TasteGoApplication.class, args);
    }
}
