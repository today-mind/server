package com.example.todaymindserver.common.event.dto;

import com.example.todaymindserver.common.util.EmotionType;
import com.example.todaymindserver.common.util.MbtiType;
import com.example.todaymindserver.common.util.ToneType;

public record EmpatheticResponseEvent(
    Long diaryId,
    String content,
    EmotionType emotionType,
    String nickName,
    MbtiType mbtiType,
    ToneType toneType
) {
}
