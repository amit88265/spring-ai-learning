package com.example.springailearning.chatclient2.dto;

import java.util.List;

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