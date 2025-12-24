package com.example.todaymindserver.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * MBTI 유형을 정의하는 Enum 클래스입니다.
 * [롤백 반영]
 * 1. 한글 상수(티, 에프) 및 정책 외 코드(NONE 등) 제거
 * 2. 자바 표준 컨벤션에 따른 영문 대문자 상수 사용
 * 3. 16가지 표준 MBTI 타입만 유지
 */
@Getter
@RequiredArgsConstructor
public enum MbtiType {
    // 분석가형 (T)
    INTJ("INTJ"),
    INTP("INTP"),
    ENTJ("ENTJ"),
    ENTP("ENTP"),

    // 외교관형 (F)
    INFJ("INFJ"),
    INFP("INFP"),
    ENFJ("ENFJ"),
    ENFP("ENFP"),

    // 관리자형 (S/J)
    ISTJ("ISTJ"),
    ISFJ("ISFJ"),
    ESTJ("ESTJ"),
    ESFJ("ESFJ"),

    // 탐험가형 (S/P)
    ISTP("ISTP"),
    ISFP("ISFP"),
    ESTP("ESTP"),
    ESFP("ESFP");

    private final String description;

    /**
     * 디자인 확정안(T/F 구분)을 지원하기 위한 유틸리티 메서드
     * 상수를 따로 만들지 않고 이름(name)에서 T 포함 여부를 체크합니다.
     */
    public boolean isLogicType() {
        return this.name().contains("T");
    }

    /**
     * MBTI 문자열을 통해 Enum 상수를 반환합니다.
     */
    public static MbtiType fromString(String mbti) {
        if (mbti == null || mbti.isBlank()) {
            return null;
        }
        try {
            return MbtiType.valueOf(mbti.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}