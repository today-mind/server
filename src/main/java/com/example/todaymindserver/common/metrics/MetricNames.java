package com.example.todaymindserver.common.metrics;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MetricNames {

    public static final String AI_RESPONSE_SUCCESS = "ai.response.success";
    public static final String AI_RESPONSE_FAILED = "ai.response.failed";
}
