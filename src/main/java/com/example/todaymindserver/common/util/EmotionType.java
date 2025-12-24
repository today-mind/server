package com.example.todaymindserver.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 일기 작성 시 사용자가 선택하는 감정 유형 (5단계)
 */
@Getter
@RequiredArgsConstructor
public enum EmotionType {
    JOY("기쁨"),
    SADNESS("슬픔"),
    ANGER("분노"),
    CALM("평온함"),
    NEUTRAL("보통");

    private final String korean;
}