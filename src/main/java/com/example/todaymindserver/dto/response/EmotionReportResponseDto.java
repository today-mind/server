package com.example.todaymindserver.dto.response;

import com.example.todaymindserver.domain.user.EmotionType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * 감정 통계 리포트 응답 DTO
 * <p>특정 월의 총 일기 개수와 감정별 통계 데이터를 담습니다.</p>
 */
@Getter
@Builder
public class EmotionReportResponseDto {
    private final int year;
    private final int month;
    private final long totalCount;
    private final Map<EmotionType, Long> emotionCounts; // 감정별 개수 매핑
}