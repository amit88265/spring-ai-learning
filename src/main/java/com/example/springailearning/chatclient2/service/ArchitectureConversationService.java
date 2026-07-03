package com.example.springailearning.chatclient2.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import com.example.springailearning.chatclient2.catalogue.PromptCatalogue;
import com.example.springailearning.chatclient2.config.AiProviderProperties;
import com.example.springailearning.chatclient2.conversation.ArchitectureConversationStepResolver;
import com.example.springailearning.chatclient2.dto.ConversationState;
import com.example.springailearning.chatclient2.dto.request.ArchitectureConversationRequest;
import com.example.springailearning.chatclient2.dto.response.ArchitectureConversationResponse;
import com.example.springailearning.chatclient2.enums.ArchitectureConversationStep;
import com.example.springailearning.chatclient2.state.ConversationStateRepository;
import com.example.springailearning.chatclient2.state.ConversationStateUpdater;

@Service
public class ArchitectureConversationService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final AiProviderProperties aiProviderProperties;
    private final ConversationStateRepository stateRepository;
    private final ConversationStateUpdater stateUpdater;
    private final ArchitectureConversationStepResolver stepResolver;

    public ArchitectureConversationService(ChatClient chatClient, ChatMemory chatMemory, AiProviderProperties aiProviderProperties,
        ConversationStateRepository stateRepository, ConversationStateUpdater stateUpdater, ArchitectureConversationStepResolver stepResolver) {

        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
        this.aiProviderProperties = aiProviderProperties;
        this.stateRepository = stateRepository;
        this.stateUpdater = stateUpdater;
        this.stepResolver = stepResolver;
    }

    public ArchitectureConversationResponse chat(ArchitectureConversationRequest request) {

        long start = System.currentTimeMillis();

        ConversationState currentState = stateRepository.getOrCreate(request.conversationId());

        ConversationState updatedState = stateUpdater.updateFromUserMessage(currentState, request.message());

        List<String> missingFields = stepResolver.missingFields(updatedState);

        ArchitectureConversationStep step = stepResolver.resolve(updatedState);

        String answer = chatClient.prompt()
            .user(user -> user.text(PromptCatalogue.MULTI_TURN_ARCHITECTURE_REVIEW_V1)
                .param("step", step.name())
                .param("state", formatState(updatedState))
                .param("missingFields", missingFields.toString())
                .param("message", request.message()))
            .advisors(MessageChatMemoryAdvisor.builder(chatMemory)
                .build())
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, request.conversationId()))
            .call()
            .content();

        stateRepository.save(updatedState);

        long latencyMs = System.currentTimeMillis() - start;

        return new ArchitectureConversationResponse(request.conversationId(), answer, step, missingFields, updatedState, aiProviderProperties.provider(),
            aiProviderProperties.model(), aiProviderProperties.profile(), latencyMs);
    }

    public ConversationState getState(String conversationId) {
        return stateRepository.getOrCreate(conversationId);
    }

    public void clear(String conversationId) {
        stateRepository.delete(conversationId);
        chatMemory.clear(conversationId);
    }

    private String formatState(ConversationState state) {
        return """
            technology: %s
            deploymentScale: %s
            regulatedData: %s
            currentStep: %s
            pendingAction: %s
            """.formatted(state.technology(), state.deploymentScale(), state.regulatedData(), state.currentStep(), state.pendingAction());
    }
}