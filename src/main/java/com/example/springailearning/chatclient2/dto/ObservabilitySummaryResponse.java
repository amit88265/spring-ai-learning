package com.example.springailearning.chatclient2.dto;

import java.util.List;

public record ObservabilitySummaryResponse(
    String provider,
    String model,
    String profile,
    List<String> enabledSignals,
    List<String> customMetrics
) {
}
