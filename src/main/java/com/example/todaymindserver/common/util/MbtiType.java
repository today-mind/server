package com.example.todaymindserver.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * [Branch 4 최종 롤백]
 * 정책에 따라 16종 MBTI가 아닌 AI 응답 성향 결정을 위한 T(사고)와 F(감정)만 관리합니다.
 */
@Getter
@RequiredArgsConstructor
public enum MbtiType {
    T("사고형"),
    F("감정형");

    private final String description;

    public static MbtiType fromString(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return MbtiType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}