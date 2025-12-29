package com.example.todaymindserver.common.client.slack;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackNotifierClient {

    private final RestClient restClient = RestClient.create();

    @Value("${slack.webhook.url}")
    private String webhookUrl;

    public void sendNotification(String message) {
        try {
            restClient.post()
                .uri(webhookUrl)
                .body(Map.of("text", message))
                .retrieve()
                .toBodilessEntity();
        } catch (Exception e) {
            log.error("Slack 알림 전송 실패", e);
        }
    }
}
