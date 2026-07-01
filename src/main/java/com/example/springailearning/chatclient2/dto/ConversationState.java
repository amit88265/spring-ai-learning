package com.example.springailearning.chatclient2.dto;

import java.time.Instant;

public record ConversationState(String conversationId, String technology, String deploymentScale, Boolean regulatedData, String currentStep,
                                String pendingAction, Instant updatedAt) {

    public static ConversationState empty(String conversationId) {
        return new ConversationState(conversationId, null, null, null, "initial", null, Instant.now());
    }
}