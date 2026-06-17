package com.example.springailearning.chatclient2.service;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.springailearning.chatclient2.advisor.AiAuditAdvisor;
import com.example.springailearning.chatclient2.catalogue.PromptCatalogue;
import com.example.springailearning.chatclient2.dto.ArchitectureReviewAiOutput;
import com.example.springailearning.chatclient2.dto.CompareRequest;
import com.example.springailearning.chatclient2.dto.CompareResponse;
import com.example.springailearning.chatclient2.dto.ReviewRequest;
import com.example.springailearning.chatclient2.dto.ReviewResponse;
import com.example.springailearning.chatclient2.dto.TechnologyComparisonAiOutput;

import reactor.core.publisher.Flux;

@Service
public class ArchitectureReviewService {

    private static final Logger log = LoggerFactory.getLogger(ArchitectureReviewService.class);

    private final ChatClient chatClient;

    private final String model;

    public Flux<String> streamReview(ReviewRequest reviewRequest) {
        return chatClient.prompt()
            .user(user -> user.text(PromptCatalogue.ARCHITECT_REVIEW_STREAM_V1)
                .param("technology", reviewRequest.technology()))
            .stream()
            .content()
            .doOnSubscribe(subscription -> log.info("AI Stream started, technology: {}", reviewRequest.technology()))
            .doOnComplete(() -> log.info("AI stream completed. technology={}", reviewRequest.technology()))
            .doOnCancel(() -> log.info("AI stream cancelled. technology={}", reviewRequest.technology()))
            .doOnError(ex -> log.warn("AI stream failed. technology={}", reviewRequest.technology(), ex));
    }

    public ReviewResponse review(ReviewRequest reviewRequest) {

        long startTime = System.currentTimeMillis();
        ArchitectureReviewAiOutput aiOutput = chatClient.prompt()
            .user(user -> user.text(PromptCatalogue.ARCHITECT_REVIEW_V1)
                .param("technology", reviewRequest.technology()))
            .call()
            .entity(ArchitectureReviewAiOutput.class);
        long latencyMs = System.currentTimeMillis() - startTime;

        return new ReviewResponse(aiOutput.summary(), aiOutput.strengths(), aiOutput.weaknesses(), aiOutput.recommendations(), model, latencyMs);
    }

    public ArchitectureReviewService(ChatClient chatClient, @Value("${spring.ai.google.genai.chat.model:unknown}") String model) {
        this.chatClient = chatClient;
        this.model = model;
    }

    public CompareResponse compare(CompareRequest compareRequest) {
        long startTime = System.currentTimeMillis();
        TechnologyComparisonAiOutput aiOutput = chatClient.prompt()
            .user(user -> user.text(PromptCatalogue.TECHNOLOGY_COMPARISON_V1)
                .param("technology1", compareRequest.technology1())
                .param("technology2", compareRequest.technology2()))
            .call()
            .entity(TechnologyComparisonAiOutput.class);
        long latencyMs = System.currentTimeMillis() - startTime;

        return new CompareResponse(aiOutput.summary(), aiOutput.keyDifferences(), aiOutput.technology1Strengths(), aiOutput.technology2Strengths(),
            aiOutput.recommendations(), model, latencyMs);
    }

    public Flux<String> streamCompare(@Valid CompareRequest compareRequest) {
        return chatClient.prompt()
            .user(user -> user.text(PromptCatalogue.ARCHITECT_COMPARE_STREAM_V1)
                .param("technology1", compareRequest.technology1())
                .param("technology2", compareRequest.technology2()))
            .stream()
            .content()
            .doOnSubscribe(
                subscription -> log.info("AI Stream started, technology1 {} and technology2: {}", compareRequest.technology1(), compareRequest.technology2()))
            .doOnComplete(() -> log.info("AI stream completed. technology1 {} and technology2: {}", compareRequest.technology1(), compareRequest.technology2()))
            .doOnCancel(() -> log.info("AI stream cancelled. technology1 {} and technology2: {}", compareRequest.technology1(), compareRequest.technology2()))
            .doOnError(ex -> log.warn("AI stream failed. technology1 {} and technology2: {}", compareRequest.technology1(), compareRequest.technology2(), ex));
    }
}
