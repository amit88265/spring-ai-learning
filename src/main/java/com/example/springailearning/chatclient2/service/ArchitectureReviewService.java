package com.example.springailearning.chatclient2.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.example.springailearning.chatclient2.catalogue.PromptCatalogue;
import com.example.springailearning.chatclient2.dto.CompareRequest;
import com.example.springailearning.chatclient2.dto.CompareResponse;
import com.example.springailearning.chatclient2.dto.ReviewRequest;
import com.example.springailearning.chatclient2.dto.ReviewResponse;

@Service
public class ArchitectureReviewService {

    final ChatClient chatClient;

    public ReviewResponse review(ReviewRequest reviewRequest) {

        return chatClient.prompt()
            .system(PromptCatalogue.SYSTEM_MESSAGE)
            .user(user -> user.text(PromptCatalogue.ARCHITECT_REVIEW_V1)
                .param("technology", reviewRequest.technology()))
            .call()
            .entity(ReviewResponse.class);

    }

    public ArchitectureReviewService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public CompareResponse compare(CompareRequest compareRequest) {
        return chatClient.prompt()
            .system(PromptCatalogue.SYSTEM_MESSAGE)
            .user(user -> user.text(PromptCatalogue.TECHNOLOGY_COMPARISON_V1)
                .param("technology1", compareRequest.technology1())
                .param("technology2", compareRequest.technology2()))
            .call()
            .entity(CompareResponse.class);

    }
}
