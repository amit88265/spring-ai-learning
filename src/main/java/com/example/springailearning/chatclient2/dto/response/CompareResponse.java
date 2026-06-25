package com.example.springailearning.chatclient2.dto.response;

import java.util.List;

import com.example.springailearning.chatclient2.dto.TokenUsage;

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
