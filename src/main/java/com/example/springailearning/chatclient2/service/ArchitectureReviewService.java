package com.example.springailearning.chatclient2.service;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

import com.example.springailearning.chatclient2.catalogue.PromptCatalogue;
import com.example.springailearning.chatclient2.config.AiProviderProperties;
import com.example.springailearning.chatclient2.dto.aioutput.ArchitectureReviewAiOutput;
import com.example.springailearning.chatclient2.dto.request.CompareRequest;
import com.example.springailearning.chatclient2.dto.response.CompareResponse;
import com.example.springailearning.chatclient2.dto.aioutput.FunctionRiskReviewAiOutput;
import com.example.springailearning.chatclient2.dto.response.FunctionRiskReviewResponse;
import com.example.springailearning.chatclient2.dto.response.ObservabilitySummaryResponse;
import com.example.springailearning.chatclient2.dto.response.ProviderInfoResponse;
import com.example.springailearning.chatclient2.dto.request.ReviewRequest;
import com.example.springailearning.chatclient2.dto.response.ReviewResponse;
import com.example.springailearning.chatclient2.dto.aioutput.TechnologyComparisonAiOutput;
import com.example.springailearning.chatclient2.dto.TokenUsage;
import com.example.springailearning.chatclient2.dto.aioutput.ToolAwareReviewAiOutput;
import com.example.springailearning.chatclient2.dto.response.ToolAwareReviewResponse;
import com.example.springailearning.chatclient2.metric.AiReviewMetrics;
import com.example.springailearning.chatclient2.tool.TechnologyKnowledgeTools;

import reactor.core.publisher.Flux;

@Service
public class ArchitectureReviewService {

    private static final Logger log = LoggerFactory.getLogger(ArchitectureReviewService.class);

    private final ChatClient chatClient;

    private final AiProviderProperties aiProviderProperties;
    private final AiReviewMetrics aiReviewMetrics;
    private final TechnologyKnowledgeTools technologyKnowledgeTools;
    private final ToolCallback technologyRiskScoreTool;

    public Flux<String> streamReview(ReviewRequest reviewRequest) {
        long startTime = System.currentTimeMillis();
        return chatClient.prompt()
            .user(user -> user.text(PromptCatalogue.ARCHITECT_REVIEW_STREAM_V1)
                .param("technology", reviewRequest.technology()))
            .stream()
            .content()
            .doOnSubscribe(subscription -> log.info("AI Stream started, technology: {}", reviewRequest.technology()))
            .doOnComplete(() -> log.info("AI stream completed. technology={}", reviewRequest.technology()))
            .doOnCancel(() -> log.info("AI stream cancelled. technology={}", reviewRequest.technology()))
            .doOnError(ex -> {
                long latencyMs = System.currentTimeMillis() - startTime;
                aiReviewMetrics.recordFailure("architecture-stream", aiProviderProperties.provider(), aiProviderProperties.model(), latencyMs, ex.getClass()
                    .getSimpleName());
                log.warn("AI stream failed. technology={}", reviewRequest.technology(), ex);
            });
    }

    public ReviewResponse review(ReviewRequest reviewRequest) {

        long startTime = System.currentTimeMillis();
        ReviewResponse reviewResponse = null;
        try {
            ResponseEntity<ChatResponse, ArchitectureReviewAiOutput> response = chatClient.prompt()
                .user(user -> user.text(PromptCatalogue.ARCHITECT_REVIEW_V1)
                    .param("technology", reviewRequest.technology()))
                .call()
                .responseEntity(ArchitectureReviewAiOutput.class);
            long latencyMs = System.currentTimeMillis() - startTime;
            ArchitectureReviewAiOutput aiOutput = response.getEntity();

            TokenUsage tokenUsage = extractTokenUsage(response.getResponse());
            reviewResponse = new ReviewResponse(aiOutput.summary(), aiOutput.strengths(), aiOutput.weaknesses(), aiOutput.recommendations(),
                aiProviderProperties.provider(), aiProviderProperties.model(), aiProviderProperties.profile(), latencyMs,
                PromptCatalogue.ARCHITECT_REVIEW_PROMPT_VERSION, tokenUsage);
            aiReviewMetrics.recordSuccess("architecture", aiProviderProperties.provider(), aiProviderProperties.model(), latencyMs, tokenUsage.totalTokens());
        } catch (RuntimeException ex) {

            long latencyMs = System.currentTimeMillis() - startTime;

            aiReviewMetrics.recordFailure(

                "architecture",

                aiProviderProperties.provider(),

                aiProviderProperties.model(),

                latencyMs,

                ex.getClass()
                    .getSimpleName()

            );

            throw ex;

        }

        return reviewResponse;
    }

    public ArchitectureReviewService(ChatClient chatClient, AiProviderProperties aiProviderProperties, AiReviewMetrics aiReviewMetrics,
        TechnologyKnowledgeTools technologyKnowledgeTools, ToolCallback technologyRiskScoreTool) {
        this.chatClient = chatClient;
        this.aiProviderProperties = aiProviderProperties;
        this.aiReviewMetrics = aiReviewMetrics;
        this.technologyKnowledgeTools = technologyKnowledgeTools;
        this.technologyRiskScoreTool = technologyRiskScoreTool;
    }

    public CompareResponse compare(CompareRequest compareRequest) {
        long startTime = System.currentTimeMillis();
        try {
            ResponseEntity<ChatResponse, TechnologyComparisonAiOutput> response = chatClient.prompt()
                .user(user -> user.text(PromptCatalogue.TECHNOLOGY_COMPARISON_V1)
                    .param("technology1", compareRequest.technology1())
                    .param("technology2", compareRequest.technology2()))
                .call()
                .responseEntity(TechnologyComparisonAiOutput.class);
            long latencyMs = System.currentTimeMillis() - startTime;
            TechnologyComparisonAiOutput aiOutput = response.getEntity();
            TokenUsage tokenUsage = extractTokenUsage(response.getResponse());

            CompareResponse compareResponse = new CompareResponse(aiOutput.summary(), aiOutput.keyDifferences(), aiOutput.technology1Strengths(),
                aiOutput.technology2Strengths(), aiOutput.recommendations(), aiProviderProperties.provider(), aiProviderProperties.model(),
                aiProviderProperties.profile(), latencyMs, PromptCatalogue.TECHNOLOGY_COMPARISON_PROMPT_VERSION, tokenUsage);
            aiReviewMetrics.recordSuccess("compare", aiProviderProperties.provider(), aiProviderProperties.model(), latencyMs, tokenUsage.totalTokens());

            return compareResponse;
        } catch (RuntimeException e) {
            long latencyMs = System.currentTimeMillis() - startTime;

            aiReviewMetrics.recordFailure(

                "compare",

                aiProviderProperties.provider(),

                aiProviderProperties.model(),

                latencyMs,

                e.getClass()
                    .getSimpleName()

            );

            throw e;
        }
    }

    private TokenUsage extractTokenUsage(ChatResponse chatResponse) {
        if (chatResponse == null) {
            return new TokenUsage(null, null, null);
        } else {
            chatResponse.getMetadata()
                .getUsage();
        }

        Usage usage = chatResponse.getMetadata()
            .getUsage();

        return new TokenUsage(usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
    }

    public ProviderInfoResponse providerInfo() {
        return new ProviderInfoResponse(aiProviderProperties.provider(), aiProviderProperties.model(), aiProviderProperties.profile());
    }

    public Flux<String> streamCompare(@Valid CompareRequest compareRequest) {
        long startTime = System.currentTimeMillis();
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
            .doOnError(ex -> {
                long latencyMs = System.currentTimeMillis() - startTime;
                aiReviewMetrics.recordFailure("compare-stream", aiProviderProperties.provider(), aiProviderProperties.model(), latencyMs, ex.getClass()
                    .getSimpleName());
                log.warn("AI stream failed. technology1 {} and technology2: {}", compareRequest.technology1(), compareRequest.technology2(), ex);
            });
    }

    public ObservabilitySummaryResponse observabilitySummary() {
        return new ObservabilitySummaryResponse(aiProviderProperties.provider(), aiProviderProperties.model(), aiProviderProperties.profile(),
            java.util.List.of("logs", "metrics", "prometheus", "tokenUsage"), java.util.List.of("ai.review.requests", "ai.review.latency", "ai.review.tokens"));
    }

    public ToolAwareReviewResponse reviewWithTools(ReviewRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            ResponseEntity<ChatResponse, ToolAwareReviewAiOutput> response = chatClient.prompt()
                .user(user -> user.text(PromptCatalogue.TOOL_AWARE_REVIEW_V1)
                    .param("technology", request.technology()))
                .tools(technologyKnowledgeTools)
                .call()
                .responseEntity(ToolAwareReviewAiOutput.class);
            long latencyMs = System.currentTimeMillis() - startTime;
            ToolAwareReviewAiOutput aiOutput = response.getEntity();
            TokenUsage tokenUsage = extractTokenUsage(response.response());

            ToolAwareReviewResponse toolAwareReviewResponse = new ToolAwareReviewResponse(aiOutput.summary(), aiOutput.toolDataAvailable(),
                aiOutput.toolFactsUsed(), aiOutput.strengths(), aiOutput.risks(), aiOutput.recommendations(), aiProviderProperties.provider(),
                aiProviderProperties.model(), aiProviderProperties.profile(), PromptCatalogue.TOOL_AWARE_ARCHITECT_REVIEW_PROMPT_VERSION, tokenUsage,
                latencyMs);
            aiReviewMetrics.recordSuccess("architecture-tools", aiProviderProperties.provider(), aiProviderProperties.model(), latencyMs,
                tokenUsage.totalTokens());

            return toolAwareReviewResponse;
        } catch (RuntimeException ex) {
            long latencyMs = System.currentTimeMillis() - startTime;
            aiReviewMetrics.recordFailure("architecture-tools", aiProviderProperties.provider(), aiProviderProperties.model(), latencyMs, ex.getClass()
                .getSimpleName());

            throw ex;
        }

    }

    public FunctionRiskReviewResponse reviewWithFunctionTool(ReviewRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            ResponseEntity<ChatResponse, FunctionRiskReviewAiOutput> response = chatClient.prompt()
                .user(user -> user.text(PromptCatalogue.FUNCTION_TOOL_AWARE_REVIEW_V1)
                    .param("technology", request.technology()))
                .tools(technologyRiskScoreTool)
                .call()
                .responseEntity(FunctionRiskReviewAiOutput.class);
            TokenUsage tokenUsage = extractTokenUsage(response.response());
            long latency = System.currentTimeMillis() - startTime;
            FunctionRiskReviewAiOutput aiOutput = response.getEntity();
            FunctionRiskReviewResponse functionRiskReviewResponse = new FunctionRiskReviewResponse(aiOutput.summary(), aiOutput.functionUsed(),
                aiOutput.riskScore(), aiOutput.riskLevel(), aiOutput.riskReasoning(), aiOutput.risks(), aiOutput.recommendations(), providerInfo().provider(),
                aiProviderProperties.model(), providerInfo().profile(), PromptCatalogue.FUNCTION_TOOL_AWARE_ARCHITECT_REVIEW_PROMPT_VERSION, tokenUsage,
                latency);
            aiReviewMetrics.recordSuccess("architecture-function-risk", aiProviderProperties.provider(), aiProviderProperties.model(), latency,
                tokenUsage.totalTokens());
            return functionRiskReviewResponse;
        } catch (Exception e) {
            long latencyMs = System.currentTimeMillis() - startTime;
            aiReviewMetrics.recordFailure("architecture-function-risk", aiProviderProperties.provider(), aiProviderProperties.model(), latencyMs, e.getClass()
                .getSimpleName());

            throw e;
        }
    }
}
