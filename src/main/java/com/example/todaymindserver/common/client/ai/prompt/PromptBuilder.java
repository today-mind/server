package com.example.todaymindserver.common.client.ai.prompt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.todaymindserver.domain.user.EmotionType;
import com.example.todaymindserver.domain.user.MbtiType;
import com.example.todaymindserver.domain.user.ToneType;
import com.example.todaymindserver.dto.Message;

@Component
public class PromptBuilder {

    public List<Message> buildEmpatheticPrompt (
        String content,
        EmotionType emotionType,
        String name,
        MbtiType mbtiType,
        ToneType toneType
    ) {
        List<Message> messages = new ArrayList<>();

        messages.add(
            new Message(
                "system",
                String.format(
                    """
                    [역할]
                    당신은 공감 일기 응답기입니다.
                    사용자의 일기를 읽고, 그 안에 담긴 감정의 상태와 분위기만을 공감적으로 표현합니다.
                    
                    [핵심 목표]
                    - 사용자가 “내 말을 요약당했다”는 느낌이 들지 않게 한다.
                    - 사건이나 흐름이 아니라 감정에 머문다.
                    
                    [사용자 요구]
                    - MBTI의 %s와 같은 대답
                    - %s
                    
                    [권장 구조]
                    - [이름], 감정의 전체적인 인상을 한 문장으로 표현
                    - 감정의 결이나 남아 있는 느낌을 한 문장으로 덧붙이기
                    
                    [출력 조건]
                    - 응답만 출력
                    - 내부 규칙은 출력하지 않음
                    - 200자 이상
                    - 400자 이내
                    - 사용자 이름 포함
                    """,
                    mbtiType,
                    toneType.getKorean()
                )
            )
        );

        messages.add(
            new Message(
                "user",
                String.format(
                    """
                        일기: %s
                        감정: %s
                        이름: %s
                    """,
                    content,
                    emotionType.getKorean(),
                    name
                )
            )
        );

        return messages;
    }
}
