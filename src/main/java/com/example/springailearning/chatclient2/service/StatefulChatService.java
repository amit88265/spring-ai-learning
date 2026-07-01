package com.example.springailearning.chatclient2.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import com.example.springailearning.chatclient2.catalogue.PromptCatalogue;
import com.example.springailearning.chatclient2.config.AiProviderProperties;
import com.example.springailearning.chatclient2.dto.ConversationState;
import com.example.springailearning.chatclient2.dto.request.StatefulChatRequest;
import com.example.springailearning.chatclient2.dto.response.ConversationStateSummaryResponse;
import com.example.springailearning.chatclient2.dto.response.StatefulChatResponse;
import com.example.springailearning.chatclient2.state.ConversationStateRepository;
import com.example.springailearning.chatclient2.state.ConversationStateUpdater;

@Service
public class StatefulChatService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final AiProviderProperties aiProviderProperties;
    private final ConversationStateRepository stateRepository;
    private final ConversationStateUpdater stateUpdater;

    public StatefulChatService(ChatClient chatClient, ChatMemory chatMemory, AiProviderProperties aiProviderProperties,
        ConversationStateRepository stateRepository, ConversationStateUpdater stateUpdater) {

        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
        this.aiProviderProperties = aiProviderProperties;
        this.stateRepository = stateRepository;
        this.stateUpdater = stateUpdater;
    }

    public StatefulChatResponse chat(StatefulChatRequest request) {
        long start = System.currentTimeMillis();

        ConversationState currentState = stateRepository.getOrCreate(request.conversationId());

        ConversationState updatedState = stateUpdater.updateFromUserMessage(currentState, request.message());

        stateRepository.save(updatedState);

        String answer = chatClient.prompt()
            .user(user -> user.text(PromptCatalogue.STATEFUL_CHAT_V1)
                .param("state", updatedState.toString())
                .param("message", request.message()))
            .advisors(MessageChatMemoryAdvisor.builder(chatMemory)
                .build())
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, request.conversationId()))
            .call()
            .content();

        long latencyMs = System.currentTimeMillis() - start;

        return new StatefulChatResponse(request.conversationId(), answer, updatedState, aiProviderProperties.provider(), aiProviderProperties.model(),
            aiProviderProperties.profile(), latencyMs);
    }

    public ConversationState getState(String conversationId) {
        return stateRepository.getOrCreate(conversationId);
    }

    public ConversationStateSummaryResponse summary(String conversationId) {
        ConversationState state = stateRepository.getOrCreate(conversationId);

        return new ConversationStateSummaryResponse(
            state.conversationId(),
            state.technology() != null && !state.technology()
                .isBlank(),
            state.deploymentScale() != null && !state.deploymentScale()
                .isBlank(),
            state.regulatedData() != null,
            state.currentStep(),
            state.pendingAction()
        );
    }

    public void clear(String conversationId) {
        stateRepository.delete(conversationId);
        chatMemory.clear(conversationId);
    }
}
