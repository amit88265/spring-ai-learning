package com.example.springailearning.chatclient2.dto.response;

import com.example.springailearning.chatclient2.dto.ConversationState;

public record StatefulChatResponse(String conversationId, String answer, ConversationState state, String provider, String model, String profile,
                                   Long latencyMs) {

}
