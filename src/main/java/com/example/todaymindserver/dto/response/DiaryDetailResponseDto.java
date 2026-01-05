package com.example.todaymindserver.dto.response;

import com.example.todaymindserver.domain.user.EmotionType;
import com.example.todaymindserver.domain.diary.ResponseStatusType;
import com.example.todaymindserver.domain.diary.Diary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 특정 일기 상세 조회 응답 DTO
 * <p>why: 일기 원문 내용과 AI 답변을 포함하여 클라이언트에게 상세 정보를 제공합니다.</p>
 */
@Getter
@Builder
public class DiaryDetailResponseDto {

    private final Long diaryId;
    private final String content; // 일기 원문
    private final EmotionType emotionType;
    private final String empatheticResponse;
    private final ResponseStatusType responseStatusType;
    private final LocalDateTime createdAt;


    public static DiaryDetailResponseDto from(Diary diary) {
        return DiaryDetailResponseDto.builder()
                .diaryId(diary.getDiaryId())
                .content(diary.getContent())
                .emotionType(diary.getEmotionType())
                .empatheticResponse(diary.getEmpatheticResponse())
                .responseStatusType(diary.getResponseStatus())
                .createdAt(diary.getCreatedAt())
                .build();
    }
}