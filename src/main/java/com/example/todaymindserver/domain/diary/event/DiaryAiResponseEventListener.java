package com.example.todaymindserver.domain.diary.event;

import java.util.List;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.example.todaymindserver.common.client.ai.AiClient;
import com.example.todaymindserver.domain.ai.prompt.PromptBuilder;
import com.example.todaymindserver.common.client.ai.dto.AiResponse;
import com.example.todaymindserver.domain.ai.prompt.PromptBuilderFactory;
import com.example.todaymindserver.domain.diary.event.dto.DiaryAiResponseRequestedEvent;
import com.example.todaymindserver.dto.Message;
import com.example.todaymindserver.service.AiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiaryAiResponseEventListener {

    private final AiService aiService;
    private final AiClient aiClient;
    private final PromptBuilderFactory promptBuilderFactory;
    ObjectMapper objectMapper = new ObjectMapper();

    @Async
    @Retryable(
        retryFor = {
            ResourceAccessException.class, // timeout, network
            HttpServerErrorException.class, // 5xx
            JsonProcessingException.class
        },
        maxAttempts = 2,
        backoff = @Backoff(delay = 1000)
    )
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(DiaryAiResponseRequestedEvent event) {

        try {
            PromptBuilder promptBuilder = promptBuilderFactory.getMBTIType(event.mbtiType());
            List<Message> messages = promptBuilder.buildEmpatheticResponsePrompt(
                event.content(),
                event.emotionType(),
                event.toneType()
            );

            // 외부 ai api 호출
            AiResponse aiResponse = aiClient.getAiResponse(messages);
            String jsonFormatResponse = aiResponse.result().message().content();

            // Json으로 파싱
            JsonNode jsonNode = objectMapper.readTree(jsonFormatResponse);
            String response = jsonNode.get("response").asText();
            double sentimentScore = jsonNode.get("sentiment_score").asDouble();

            aiService.saveAiResponse(
                event.userId(),
                event.diaryId(),
                event.mbtiType(),
                response,
                sentimentScore
            );

        } catch (ResourceAccessException | HttpServerErrorException e) {
            // retry / recover가 처리
            throw e;

        } catch (Exception e) {
            // 🔥 여기서 모든 구멍을 막는다
            log.error(
                "[AI][Unhandled] diaryId={}, userId={}, reason={}",
                event.diaryId(),
                event.userId(),
                e.getClass().getSimpleName()
            );
            aiService.markFailed(
                event.userId(),
                event.diaryId()
            );
        }
    }

    @Recover
    public void recover(ResourceAccessException e, DiaryAiResponseRequestedEvent event) {
        log.warn("[AI][RetryFail][Network] diaryId={}, userId={}, reason={}",
            event.diaryId(),
            event.userId(),
            e.getClass().getSimpleName()
        );
        aiService.markFailed(event.userId(), event.diaryId());
    }

    @Recover
    public void recover(HttpServerErrorException e, DiaryAiResponseRequestedEvent event) {
        log.error(
            "[AI][RetryFail][5xx] diaryId={}, userId={}, reason={}",
            event.diaryId(),
            event.userId(),
            e.getClass().getSimpleName()
        );
        aiService.markFailed(event.userId(), event.diaryId());
    }
}
