package com.example.todaymindserver.dto.response;

import com.example.todaymindserver.common.util.EmotionType;
import com.example.todaymindserver.entity.Diary;
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
    private final LocalDateTime createdAt;

    // [TODO] AiFeedback 엔티티 구현 전까지는 String으로 처리
    private final String aiReply; // AI 답변 내용

    public static DiaryDetailResponseDto from(Diary diary, String aiReply) {
        return DiaryDetailResponseDto.builder()
                .diaryId(diary.getDiaryId())
                .content(diary.getContent())
                .emotionType(diary.getEmotionType())
                .createdAt(diary.getCreatedAt()) // BaseTimeEntity에서 상속받은 createdAt 사용
                .aiReply(aiReply)
                .build();
    }
}