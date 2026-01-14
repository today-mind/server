package com.example.todaymindserver.service;

import org.springframework.stereotype.Component;

import com.example.todaymindserver.common.metrics.MetricNames;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class DiaryAiMetrics {

    private final Counter success;
    private final Counter failed;

    public DiaryAiMetrics(MeterRegistry registry) {
        this.success = Counter.builder(MetricNames.AI_RESPONSE_SUCCESS)
            .register(registry);
        this.failed = Counter.builder(MetricNames.AI_RESPONSE_FAILED)
            .register(registry);
    }

    public void success() {
        success.increment();
    }

    public void failed() {
        failed.increment();
    }
}
