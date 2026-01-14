package com.example.todaymindserver.domain.diary.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.todaymindserver.domain.diary.event.dto.DiaryAiResponseCompletedMetricsEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiaryAiResponseMetricsEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishCompleted() {
        publisher.publishEvent(new DiaryAiResponseCompletedMetricsEvent(true));
    }

    public void publishFailed() {
        publisher.publishEvent(new DiaryAiResponseCompletedMetricsEvent(false));
    }
}
