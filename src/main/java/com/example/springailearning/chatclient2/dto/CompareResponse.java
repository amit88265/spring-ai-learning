package com.example.springailearning.chatclient2.dto;

import java.util.List;

public record CompareResponse(

    String summary,

    List<String> keyDifferences,

    List<String> technology1Strengths,

    List<String> technology2Strengths,

    List<String> recommendations,

    String provider,

    String model,

    String profile,

    Long latencyMs,

    String promptVersion,

    TokenUsage tokenUsage) {
}
