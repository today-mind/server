package com.example.todaymindserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing; // <--- 1. 이 줄을 추가

@EnableJpaAuditing
@SpringBootApplication
public class TodayMindServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodayMindServerApplication.class, args);
    }

}