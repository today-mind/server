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
import com.example.todaymindserver.common.client.ai.prompt.PromptBuilder;
import com.example.todaymindserver.common.client.ai.dto.AiResponse;
import com.example.todaymindserver.domain.diary.event.dto.EmpatheticResponseEvent;
import com.example.todaymindserver.dto.Message;
import com.example.todaymindserver.service.AiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiResponseEventListener {

    private final AiService aiService;
    private final AiClient aiClient;
    private final PromptBuilder promptBuilder;

    @Async
    @Retryable(
        retryFor = {
            ResourceAccessException.class, // timeout, network
            HttpServerErrorException.class // 5xx
        },
        maxAttempts = 2,
        backoff = @Backoff(delay = 1000)
    )
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(EmpatheticResponseEvent event) {

        try {
            List<Message> messages = promptBuilder.buildEmpatheticPrompt(
                event.content(),
                event.emotionType(),
                event.nickName(),
                event.mbtiType(),
                event.toneType()
            );

            AiResponse response = aiClient.getAiResponse(messages);

            aiService.saveAiResponse(
                event.userId(),
                event.diaryId(),
                response
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
    public void recover(ResourceAccessException e, EmpatheticResponseEvent event) {
        log.warn("[AI][RetryFail][Network] diaryId={}, userId={}, reason={}",
            event.diaryId(),
            event.userId(),
            e.getClass().getSimpleName()
        );
        aiService.markFailed(event.userId(), event.diaryId());
    }

    @Recover
    public void recover(HttpServerErrorException e, EmpatheticResponseEvent event) {
        log.error(
            "[AI][RetryFail][5xx] diaryId={}, userId={}, reason={}",
            event.diaryId(),
            event.userId(),
            e.getClass().getSimpleName()
        );
        aiService.markFailed(event.userId(), event.diaryId());
    }
}
