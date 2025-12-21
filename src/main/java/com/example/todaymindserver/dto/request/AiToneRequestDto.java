package com.example.todaymindserver.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * AI 톤앤매너 설정 요청 DTO
 */
@Getter
@NoArgsConstructor
public class AiToneRequestDto {
    @NotBlank(message = "톤앤매너 설정값은 필수입니다.")
    private String toneType; // "FRIENDLY", "HONORIFIC", "CASUAL" 등
}