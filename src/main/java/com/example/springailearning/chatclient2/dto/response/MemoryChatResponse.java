package com.example.springailearning.chatclient2.dto.response;

public record MemoryChatResponse(String conversationId, String answer, String provider, String model, String profile, Long latencyMs) {

}