package com.example.todaymindserver.domain.ai.prompt;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.example.todaymindserver.domain.user.MbtiType;

@Component
public class PromptBuilderFactory {

    private final Map<MbtiType, PromptBuilder> promptBuilders = new EnumMap<>(MbtiType.class);

    public PromptBuilderFactory(List<PromptBuilder> builders) {
        for (PromptBuilder builder : builders) {
            promptBuilders.put(builder.getToneType(), builder);
        }
    }

    public PromptBuilder getMBTIType(MbtiType type) {
        return promptBuilders.get(type);
    }
}
