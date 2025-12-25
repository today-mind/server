package com.example.todaymindserver.common.event.dto;

import com.example.todaymindserver.domain.user.EmotionType;
import com.example.todaymindserver.domain.user.MbtiType;
import com.example.todaymindserver.domain.user.ToneType;

public record EmpatheticResponseEvent(
    Long diaryId,
    String content,
    EmotionType emotionType,
    String nickName,
    MbtiType mbtiType,
    ToneType toneType
) {
}
