package com.example.todaymindserver.domain.ai.post_processor;

import org.springframework.stereotype.Component;

import com.example.todaymindserver.domain.user.MbtiType;

@Component
public class TuMaTonePostProcessor implements PostProcessor {

    @Override
    public String process(String rawText, double sentimentScore) {
        if (rawText == null || rawText.isBlank()) {
            return rawText;
        }

        String text = rawText.trim();

        if (sentimentScore > 0 && sentimentScore < 1) {
            text = "오...! " + text;
        } else if (sentimentScore > -1 && sentimentScore < 0) {
            text = "우웅... " + text;
        }

        text = text.replaceAll("겠[다어지](?=[.!?…]|$)", "겠둥");
        text = text.replaceAll("했[다어지](?=[.!?…]|$)", "했둥");
        text = text.replaceAll("이[다야네](?=[.!?…]|$)", "이둥");
        text = text.replaceAll("있[어다](?=[.!?…]|$)", "있둥");

        return text;
    }

    @Override
    public MbtiType getMbtiType() {
        return MbtiType.F;
    }
}
