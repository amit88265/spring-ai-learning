package com.example.springailearning.chatclient2.dto.response;

public record ConversationStateSummaryResponse(
    String conversationId,
    boolean hasTechnology,
    boolean hasDeploymentScale,
    boolean hasRegulatedDataFlag,
    String currentStep,
    String pendingAction
) {
}
