package com.example.todaymindserver.common.event;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.todaymindserver.common.client.clova.ClovaClient;
import com.example.todaymindserver.common.client.clova.prompt.PromptBuilder;
import com.example.todaymindserver.common.client.dto.ClovaResponse;
import com.example.todaymindserver.common.event.dto.EmpatheticResponseEvent;
import com.example.todaymindserver.dto.Message;
import com.example.todaymindserver.service.ClovaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClovaResponseEventListner {

    private final ClovaService clovaService;
    private final ClovaClient clovaClient;
    private final PromptBuilder promptBuilder;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(EmpatheticResponseEvent event) {

        List<Message> messages = promptBuilder.buildEmpatheticPrompt(
            event.content(),
            event.emotionType(),
            event.nickName(),
            event.mbtiType(),
            event.toneType()
        );
        ClovaResponse response = clovaClient.getClovaAnswer(messages);

        clovaService.saveClovaResponse(event.diaryId(), response);
    }
}
