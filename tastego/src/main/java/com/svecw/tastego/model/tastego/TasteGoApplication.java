package com.svecw.tastego;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TasteGoApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                TasteGoApplication.class,
                args
        );

    }

}