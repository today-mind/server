package com.example.todaymindserver.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todaymindserver.common.client.ai.dto.AiResponse;
import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.diary.Diary;
import com.example.todaymindserver.domain.diary.event.DiaryAiResponseMetricsEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final DiaryService diaryService;
    private final DiaryAiResponseMetricsEventPublisher diaryAiResponseMetricsEventPublisher;

    @Transactional
    public void saveAiResponse(Long userId, Long diaryId, AiResponse response) {
        Diary diary = diaryService.getDiary(userId, diaryId);

        try {
            String aiResponse = response.result().message().content();
            diary.markResponseCompleted(aiResponse);

            // 매트릭 수집
            diaryAiResponseMetricsEventPublisher.publishCompleted();
        } catch (BusinessException e) {
            log.warn("일기에 대한 Ai 응답이 이미 존재합니다. userId={}, diaryId={}", userId, diaryId);
            diary.markResponseFailed();

            // 매트릭 수집
            diaryAiResponseMetricsEventPublisher.publishFailed();
        } catch (Exception e) {
            log.warn("AI 응답 처리 중 시스템 오류. userId={}, diaryId={}", userId, diaryId, e);
            diary.markResponseFailed();

            // 매트릭 수집
            diaryAiResponseMetricsEventPublisher.publishFailed();
        }
    }

    @Transactional
    public void markFailed(Long userId, Long diaryId) {
        Diary diary = diaryService.getDiary(userId, diaryId);
        diary.markResponseFailed();
    }
}
