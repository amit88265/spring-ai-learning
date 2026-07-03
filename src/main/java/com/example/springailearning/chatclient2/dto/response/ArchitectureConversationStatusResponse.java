package com.example.springailearning.chatclient2.dto.response;

import java.util.List;

import com.example.springailearning.chatclient2.enums.ArchitectureConversationStep;

public record ArchitectureConversationStatusResponse(String conversationId, ArchitectureConversationStep step, List<String> missingFields,
                                                     boolean readyForReview) {

}
