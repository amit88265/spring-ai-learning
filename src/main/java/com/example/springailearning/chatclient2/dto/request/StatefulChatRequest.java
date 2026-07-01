package com.example.springailearning.chatclient2.dto.request;

import jakarta.validation.constraints.NotBlank;

public record StatefulChatRequest(@NotBlank String conversationId, @NotBlank String message) {

}
