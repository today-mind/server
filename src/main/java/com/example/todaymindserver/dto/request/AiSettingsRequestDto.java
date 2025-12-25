package com.example.todaymindserver.dto.request;

import com.example.todaymindserver.domain.user.MbtiType;
import com.example.todaymindserver.domain.user.ToneType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [Branch 5] AI 설정 변경 요청 DTO
 * 명세서의 필드명(personalityType, speechStyle)을 준수합니다.
 */
@Getter
@NoArgsConstructor
public class AiSettingsRequestDto {

    @NotNull(message = "성향 타입(T/F)은 필수입니다.")
    @JsonProperty("personalityType") // JSON 필드명 매핑
    private MbtiType personalityType;

    @NotNull(message = "말투 스타일(HONORIFIC/INFORMAL)은 필수입니다.")
    @JsonProperty("speechStyle") // JSON 필드명 매핑
    private ToneType speechStyle;
}