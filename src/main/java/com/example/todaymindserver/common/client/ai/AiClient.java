package com.example.todaymindserver.common.client.ai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.example.todaymindserver.common.client.ai.dto.AiResponse;
import com.example.todaymindserver.common.client.util.SafeBody;
import com.example.todaymindserver.dto.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiClient {

    private final RestClient aiRestClient ;

    @Value("${clova.completion-url}")
    private String completionUrl;

    @Value("${clova.api-key}")
    private String apiKey;

    public static final double TEMPERATURE = 0.5;
    public static final int TOP_K = 0;
    public static final double TOP_P = 0.8;
    public static final double REPEAT_PENALTY = 1.1;
    public static final int MAX_TOKENS = 256;

    public AiResponse getAiResponse(List<Message> messages)  {

        Map<String, Object> body = new HashMap<>();
        body.put("messages", messages);
        body.put("temperature", TEMPERATURE);
        body.put("topK", TOP_K);
        body.put("topP", TOP_P);
        body.put("repeatPenalty", REPEAT_PENALTY);
        body.put("maxTokens", MAX_TOKENS);

        return aiRestClient.post()
            .uri(completionUrl)
            .header("Authorization", "Bearer " + apiKey)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                log.warn(
                    "[AI][CALL_FAIL][4xx] status={}, body={}",
                    res.getStatusCode(),
                    SafeBody.read(res)
                );
                throw new HttpClientErrorException(res.getStatusCode());
            })
            .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                log.error(
                    "[AI][CALL_FAIL][5xx] status={}, body={}",
                    res.getStatusCode(),
                    SafeBody.read(res)
                );
                throw new HttpServerErrorException(res.getStatusCode());
            })
            .body(AiResponse.class);
    }
}
