package com.example.springailearning.chatclient2.dto.response;

import java.util.List;

import com.example.springailearning.chatclient2.dto.TokenUsage;

public record ToolAwareReviewResponse(
    String summary,
    boolean toolDataAvailable,
    List<String> toolFactsUsed,
    List<String> strengths,
    List<String> risks,
    List<String> recommendations,
    String provider,
    String model,
    String profile,
    String promptVersion,
    TokenUsage tokenUsage,
    Long latencyMs
) {}