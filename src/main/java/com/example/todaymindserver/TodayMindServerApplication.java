package com.example.todaymindserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // CreatedAt, UpdatedAt 자동 관리를 위해 유지
public class TodayMindServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodayMindServerApplication.class, args);
    }

}