package com.example.todaymindserver.domain.diary.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.todaymindserver.domain.diary.event.dto.DiaryAiResponseCompletedMetricsEvent;
import com.example.todaymindserver.service.DiaryAiMetrics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiaryAiResponseMetricsListener {

    private final DiaryAiMetrics diaryAiMetrics;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCompleted(DiaryAiResponseCompletedMetricsEvent event) {
        try {
            if (event.success()) {
                diaryAiMetrics.success();
            } else {
                diaryAiMetrics.failed();
            }
        } catch (Exception e) {
            log.warn("ai 응답 메트릭 이벤트 실패 cause={}", e.getCause(), e);
        }
    }

}
