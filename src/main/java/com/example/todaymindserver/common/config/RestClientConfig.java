package com.example.todaymindserver.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient aiRestClient() {
        SimpleClientHttpRequestFactory factory =
            new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(3_000);
        factory.setReadTimeout(6_000);

        return RestClient.builder()
            .requestFactory(factory)
            .build();
    }

    @Bean
    public RestClient oauthRestClient() {
        SimpleClientHttpRequestFactory factory =
            new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(3_000);
        factory.setReadTimeout(4_000);

        return RestClient.builder()
            .requestFactory(factory)
            .build();
    }
}
