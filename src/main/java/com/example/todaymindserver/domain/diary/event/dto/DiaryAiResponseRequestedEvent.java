package com.example.todaymindserver.domain.diary.event.dto;

import com.example.todaymindserver.domain.user.EmotionType;
import com.example.todaymindserver.domain.user.MbtiType;
import com.example.todaymindserver.domain.user.ToneType;

public record DiaryAiResponseRequestedEvent(
    Long diaryId,
    String content,
    EmotionType emotionType,
    Long userId,
    String nickName,
    MbtiType mbtiType,
    ToneType toneType
) {
}
