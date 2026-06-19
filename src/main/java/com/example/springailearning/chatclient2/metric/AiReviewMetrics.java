package com.example.springailearning.chatclient2.metric;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AiReviewMetrics {

    private final MeterRegistry meterRegistry;

    public AiReviewMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordSuccess(
        String endpoint,
        String provider,
        String model,
        long latencyMs,
        Integer totalTokens) {

        Counter.builder("ai.review.requests")
            .tag("endpoint", endpoint)
            .tag("provider", provider)
            .tag("model", model)
            .tag("result", "success")
            .register(meterRegistry)
            .increment();

        Timer.builder("ai.review.latency")
            .tag("endpoint", endpoint)
            .tag("provider", provider)
            .tag("model", model)
            .register(meterRegistry)
            .record(latencyMs, TimeUnit.MILLISECONDS);

        if (totalTokens != null) {
            meterRegistry.counter(
                "ai.review.tokens",
                "endpoint", endpoint,
                "provider", provider,
                "model", model
            ).increment(totalTokens);
        }
    }

    public void recordFailure(
        String endpoint,
        String provider,
        String model,
        long latencyMs,
        String errorType) {

        Counter.builder("ai.review.requests")
            .tag("endpoint", endpoint)
            .tag("provider", provider)
            .tag("model", model)
            .tag("result", "failure")
            .tag("errorType", errorType)
            .register(meterRegistry)
            .increment();

        Timer.builder("ai.review.latency")
            .tag("endpoint", endpoint)
            .tag("provider", provider)
            .tag("model", model)
            .register(meterRegistry)
            .record(latencyMs, TimeUnit.MILLISECONDS);
    }
}
