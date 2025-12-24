package com.example.todaymindserver.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * [충돌 해결] AI 답장 톤앤매너 타입
 * feature/lock-setting의 정책 반영과 dev 브랜치의 구조를 통합했습니다.
 */
@Getter
@RequiredArgsConstructor
public enum ToneType {
    HONORIFIC("존댓말"),
    INFORMAL("반말"); // dev의 BANMAL 대신 정책 표준인 INFORMAL을 유지합니다.

    private final String description;

    /**
     * 문자열로부터 안전하게 Enum 상수를 반환합니다.
     * [리뷰 반영] 정책 외의 값이 들어올 경우 기본값(HONORIFIC)을 반환하여 500 에러를 방지합니다.
     */
    public static ToneType fromString(String tone) {
        if (tone == null || tone.isBlank()) {
            return HONORIFIC;
        }
        try {
            return ToneType.valueOf(tone.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 정책에 없는 BANMAL 등이 들어올 경우를 대비한 세이프티 로직
            if ("BANMAL".equals(tone.toUpperCase())) {
                return INFORMAL;
            }
            return HONORIFIC;
        }
    }
}