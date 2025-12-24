package com.example.todaymindserver.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * [Branch 4 최종] AI 답장 톤 타입
 * PromptBuilder에서 getKorean()을 호출하므로 필드명을 korean으로 유지합니다.
 */
@Getter
@RequiredArgsConstructor
public enum ToneType {
    HONORIFIC("존댓말"),
    INFORMAL("반말");

    private final String korean; // description에서 korean으로 변경

    public static ToneType fromString(String tone) {
        if (tone == null || tone.isBlank()) return HONORIFIC;
        String upperTone = tone.toUpperCase();
        try {
            return ToneType.valueOf(upperTone);
        } catch (IllegalArgumentException e) {
            if ("BANMAL".equals(upperTone)) return INFORMAL;
            return HONORIFIC;
        }
    }
}