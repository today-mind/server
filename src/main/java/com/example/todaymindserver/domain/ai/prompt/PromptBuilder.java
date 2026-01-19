package com.example.todaymindserver.domain.ai.prompt;

import java.util.List;

import com.example.todaymindserver.domain.user.EmotionType;
import com.example.todaymindserver.domain.user.MbtiType;
import com.example.todaymindserver.domain.user.ToneType;
import com.example.todaymindserver.dto.Message;

public interface PromptBuilder {

    List<Message> buildEmpatheticResponsePrompt(String content, EmotionType emotionType, ToneType tonetype);
    MbtiType getToneType();
}
