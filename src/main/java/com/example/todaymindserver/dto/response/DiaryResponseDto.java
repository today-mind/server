package com.example.todaymindserver.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 일기 작성 성공 시 응답 DTO
 */
@Getter
@Builder
public class DiaryResponseDto {

    private final Long diaryId; // 생성된 일기의 고유 ID
}