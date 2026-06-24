package com.example.springailearning.chatclient2.dto;

public record RiskScoreResponse(
    String technology,
    int riskScore,
    String riskLevel,
    String reasoning
) {
}