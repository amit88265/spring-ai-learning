package com.example.springailearning.chatclient2.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import com.example.springailearning.chatclient2.catalogue.PromptCatalogue;
import com.example.springailearning.chatclient2.dto.ReviewRequest;
import com.example.springailearning.chatclient2.dto.ReviewResponse;

@Service
public class ArchitectureReviewService {

    final ChatClient chatClient;

    public ReviewResponse callModel(ReviewRequest reviewRequest) {

        long startTime = System.currentTimeMillis();
        ChatResponse chatResponse = chatClient.prompt()
            .user(user -> user.text(PromptCatalogue.SENIOR_ARCHITECT_REVIEW_PROMPT)
                .param("technology", reviewRequest.getTechnology()))
            .call()
            .chatResponse();
        long latencyMs = System.currentTimeMillis() - startTime;

        return new ReviewResponse(
            chatResponse.getResult().getOutput().getText(), chatResponse.getMetadata()
            .getModel(),
            latencyMs
        );
    }

    public ArchitectureReviewService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
}
