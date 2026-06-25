package com.example.springailearning.chatclient2.service;

import jakarta.validation.Valid;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import com.example.springailearning.chatclient2.config.AiProviderProperties;
import com.example.springailearning.chatclient2.dto.request.MemoryChatRequest;
import com.example.springailearning.chatclient2.dto.response.ConversationMemorySummaryResponse;
import com.example.springailearning.chatclient2.dto.response.MemoryChatResponse;

@Service
public class MemoryChatService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final AiProviderProperties aiProviderProperties;

    public MemoryChatResponse chat(@Valid MemoryChatRequest request) {
        long currentTimeMillis = System.currentTimeMillis();
        String content = chatClient.prompt()
            .user(request.message())
            .advisors(MessageChatMemoryAdvisor.builder(chatMemory)
                .build())
            .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, request.conversationId()))
            .call()
            .content();
        long latency = System.currentTimeMillis() - currentTimeMillis;

        return new MemoryChatResponse(request.conversationId(), content, aiProviderProperties.provider(), aiProviderProperties.model(),
            aiProviderProperties.profile(), latency);
    }

    public MemoryChatService(ChatClient chatClient, ChatMemory chatMemory, AiProviderProperties aiProviderProperties) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
        this.aiProviderProperties = aiProviderProperties;
    }

    public void clear(String conversationId) {
        chatMemory.clear(conversationId);
    }

    public ConversationMemorySummaryResponse summary(String conversationId) {
        return new ConversationMemorySummaryResponse(conversationId, chatMemory.get(conversationId)
            .size());
    }
}
