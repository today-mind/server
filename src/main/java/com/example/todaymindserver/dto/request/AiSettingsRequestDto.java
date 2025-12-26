package com.example.todaymindserver.dto.request;

import com.example.todaymindserver.domain.user.MbtiType;
import com.example.todaymindserver.domain.user.ToneType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [Branch 5 수정] AI 설정 변경 요청 DTO
 * PATCH 메서드 특성에 맞게 두 필드 모두 선택적으로 받을 수 있도록 @NotNull을 제거했습니다.
 */
@Getter
@NoArgsConstructor
public class AiSettingsRequestDto {

    @JsonProperty("personalityType")
    private MbtiType personalityType; // null 허용

    @JsonProperty("speechStyle")
    private ToneType speechStyle; // null 허용
}