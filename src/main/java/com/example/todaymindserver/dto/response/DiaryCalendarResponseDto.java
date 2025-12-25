package com.example.todaymindserver.dto.response;

import com.example.todaymindserver.domain.user.EmotionType;
import com.example.todaymindserver.domain.diary.Diary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * 일기 월별 캘린더 조회 응답 DTO
 * <p>why: 특정 월의 일기 목록을 캘린더 뷰에 필요한 정보(날짜, 감정)만 담아 클라이언트에게 전달합니다.</p>
 */
@Getter
@Builder
public class DiaryCalendarResponseDto {

    private final List<DiaryCalendarItem> diaries; // 해당 월의 일기 목록

    /**
     * 일기 캘린더 항목 DTO
     * <p>why: 캘린더 뷰는 작성 날짜와 감정 유형만 필요하므로 필요한 필드만 포함합니다.</p>
     */
    @Getter
    @Builder
    public static class DiaryCalendarItem {
        private final Long diaryId;
        private final LocalDate date; // 일기 작성 날짜
        private final EmotionType emotionType; // 그 날의 감정 스티커

        public static DiaryCalendarItem from(Diary diary) {
            // BaseTimeEntity를 상속받았으므로 getCreatedAt() 사용 가능
            return DiaryCalendarItem.builder()
                    .diaryId(diary.getDiaryId())
                    .date(diary.getCreatedAt().toLocalDate())
                    .emotionType(diary.getEmotionType())
                    .build();
        }
    }
}