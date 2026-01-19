package com.example.todaymindserver.domain.ai.post_processor;

import org.springframework.stereotype.Component;

import com.example.todaymindserver.domain.user.MbtiType;

@Component
public class PengTuTonePostProcessor implements PostProcessor
{
    @Override
    public String process(String rawText, double sentimentScore) {
        if (rawText == null || rawText.isBlank()) {
            return rawText;
        }

        String text = rawText.trim();

        if (sentimentScore > 0 && sentimentScore < 1) {
            text = "칙...! " + text;
        } else if (sentimentScore > -1 && sentimentScore < 0) {
            text = "칙!!!!! " + text;
        }

        text = text.replaceAll("겠[다어지](?=[.!?…]|$)", "겠펭");
        text = text.replaceAll("했[다어지](?=[.!?…]|$)", "했펭");
        text = text.replaceAll("이[다야네](?=[.!?…]|$)", "이펭");
        text = text.replaceAll("있[어다](?=[.!?…]|$)", "있펭");

        return text;
    }

    @Override
    public MbtiType getMbtiType() {
        return MbtiType.T;
    }
}
