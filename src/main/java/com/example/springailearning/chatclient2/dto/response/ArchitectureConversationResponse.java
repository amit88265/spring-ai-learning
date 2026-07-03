package com.example.springailearning.chatclient2.dto.response;

import java.util.List;

import com.example.springailearning.chatclient2.dto.ConversationState;
import com.example.springailearning.chatclient2.enums.ArchitectureConversationStep;

public record ArchitectureConversationResponse(String conversationId, String answer, ArchitectureConversationStep step, List<String> missingFields,
                                               ConversationState state, String provider, String model, String profile, Long latencyMs) {

}
