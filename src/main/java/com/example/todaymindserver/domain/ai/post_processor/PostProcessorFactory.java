package com.example.todaymindserver.domain.ai.post_processor;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.todaymindserver.domain.user.MbtiType;

@Component
public class PostProcessorFactory {
    private final Map<MbtiType, PostProcessor> postProcessors = new EnumMap<>(MbtiType.class);

    public PostProcessorFactory(List<PostProcessor> processors) {
        for (PostProcessor processor : processors) {
            postProcessors.put(processor.getMbtiType(), processor);
        }
    }

    public PostProcessor getMBTIType(MbtiType type) {
        return postProcessors.get(type);
    }
}
