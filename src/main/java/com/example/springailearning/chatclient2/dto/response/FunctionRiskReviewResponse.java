package com.example.springailearning.chatclient2.dto.response;

import java.util.List;

import com.example.springailearning.chatclient2.dto.TokenUsage;

public record FunctionRiskReviewResponse(
    String summary,
    boolean functionUsed,
    int riskScore,
    String riskLevel,
    String riskReasoning,
    List<String> risks,
    List<String> recommendations,
    String provider,
    String model,
    String profile,
    String promptVersion,
    TokenUsage tokenUsage,
    Long latencyMs
) {
}