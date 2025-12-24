package com.example.todaymindserver.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * AI 답장 톤앤매너 타입
 * [롤백 반영]
 * 1. 프로젝트 정책(Policy)에 없는 FRIENDLY, CASUAL 삭제
 * 2. 표준 명칭인 HONORIFIC(존댓말)과 INFORMAL(반말)로 단일화
 */
@Getter
@RequiredArgsConstructor
public enum ToneType {
    HONORIFIC("존댓말"),
    INFORMAL("반말");

    private final String description;

    /**
     * 문자열로부터 안전하게 Enum 상수를 반환합니다.
     */
    public static ToneType fromString(String tone) {
        if (tone == null || tone.isBlank()) {
            return HONORIFIC; // 기본값 설정 (정책에 따라 변경 가능)
        }
        try {
            return ToneType.valueOf(tone.toUpperCase());
        } catch (IllegalArgumentException e) {
            return HONORIFIC;
        }
    }
}