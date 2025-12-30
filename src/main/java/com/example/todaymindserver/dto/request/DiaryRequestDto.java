package com.example.todaymindserver.dto.request;

import com.example.todaymindserver.domain.user.EmotionType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Min(value = 1, message = "일기 내용은 1자 이상이어야 합니다.")
    @Max(value = 1_000, message = "일기 내용은 1,000자 이하이어야 합니다.")
    private String content;

    /**
     * 일기 감정 유형
     */
    @NotNull(message = "emotionType은 필수 항목입니다.")
    private EmotionType emotionType;
}