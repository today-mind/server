package com.example.todaymindserver.dto.request;

import com.example.todaymindserver.domain.user.EmotionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 일기 작성 요청 (Request) DTO
 */
@Getter
public class DiaryRequestDto {

    /**
     * 일기 본문 내용
     */
    @NotBlank(message = "content는 필수 항목입니다.")
    @Size(max = 1_000, message = "일기 내용은 1,000자 이하이어야 합니다.")
    private String content;

    /**
     * 일기 감정 유형
     */
    @NotNull(message = "emotionType은 필수 항목입니다.")
    private EmotionType emotionType;
}