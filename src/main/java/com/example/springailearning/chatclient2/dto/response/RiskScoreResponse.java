package com.example.springailearning.chatclient2.dto.response;

public record RiskScoreResponse(
    String technology,
    int riskScore,
    String riskLevel,
    String reasoning
) {
}