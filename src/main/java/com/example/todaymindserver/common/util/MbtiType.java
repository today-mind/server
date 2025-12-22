package com.example.todaymindserver.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 모든 MBTI 유형을 정의하는 Enum 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public enum MbtiType {
    // 분석가형
    INTJ("INTJ"), INTP("INTP"), ENTJ("ENTJ"), ENTP("ENTP"),
    // 외교관형
    INFJ("INFJ"), INFP("INFP"), ENFJ("ENFJ"), ENFP("ENFP"),
    // 관리자형
    ISTJ("ISTJ"), ISFJ("ISFJ"), ESTJ("ESTJ"), ESFJ("ESFJ"),
    // 탐험가형
    ISTP("ISTP"), ISFP("ISFP"), ESTP("ESTP"), ESFP("ESFP"),
    // 미설정
    NONE("미설정");

    private final String description;
}