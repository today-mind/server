package com.example.todaymindserver.domain.ai.post_processor;

import com.example.todaymindserver.domain.user.MbtiType;

public interface PostProcessor {

    String process(String text, double sentimentScore);
    MbtiType getMbtiType();
}
