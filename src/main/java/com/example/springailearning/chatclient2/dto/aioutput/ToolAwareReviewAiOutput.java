package com.example.springailearning.chatclient2.dto.aioutput;

import java.util.List;

public record ToolAwareReviewAiOutput(
    String summary,
    boolean toolDataAvailable,
    List<String> toolFactsUsed,
    List<String> strengths,
    List<String> risks,
    List<String> recommendations
) {
}
