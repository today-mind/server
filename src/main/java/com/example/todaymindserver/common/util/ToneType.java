package com.example.todaymindserver.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * [빌드 에러 해결] AI 답장 톤앤매너 타입
 * PromptBuilder에서 사용하는 getKorean() 메서드와 호환되도록 필드명을 수정했습니다.
 */
@Getter
@RequiredArgsConstructor
public enum ToneType {
    HONORIFIC("존댓말"),
    INFORMAL("반말"); // 용준님의 정책에 따라 명칭은 INFORMAL 유지

    // PromptBuilder의 getKorean()과 연결하기 위해 필드명을 korean으로 변경
    private final String korean;

    /**
     * 문자열로부터 안전하게 Enum 상수를 반환합니다.
     */
    public static ToneType fromString(String tone) {
        if (tone == null || tone.isBlank()) {
            return HONORIFIC;
        }

        String upperTone = tone.toUpperCase();

        try {
            return ToneType.valueOf(upperTone);
        } catch (IllegalArgumentException e) {
            // dev 브랜치와의 호환성을 위해 BANMAL 처리
            if ("BANMAL".equals(upperTone)) {
                return INFORMAL;
            }
            return HONORIFIC;
        }
    }
}