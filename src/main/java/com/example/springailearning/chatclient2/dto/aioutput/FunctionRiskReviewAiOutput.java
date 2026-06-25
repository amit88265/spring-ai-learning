package com.example.springailearning.chatclient2.dto.aioutput;

import java.util.List;

public record FunctionRiskReviewAiOutput(
    String summary,
    boolean functionUsed,
    int riskScore,
    String riskLevel,
    String riskReasoning,
    List<String> risks,
    List<String> recommendations
) {
}