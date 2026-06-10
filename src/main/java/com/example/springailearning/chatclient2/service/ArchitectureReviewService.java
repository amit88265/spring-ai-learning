package com.example.springailearning.chatclient2.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.example.springailearning.chatclient2.dto.ReviewRequest;

@Service
public class ArchitectureReviewService {

    final ChatClient chatClient;

    public String callModel(ReviewRequest reviewRequest) {
        return chatClient.prompt(reviewRequest.getTechnology())
            .call()
            .content();
    }

    public ArchitectureReviewService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
}
