package com.example.springailearning.chatclient2.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springailearning.chatclient2.advisor.AiAuditAdvisor;
import com.example.springailearning.chatclient2.catalogue.PromptCatalogue;

@Configuration
public class AiClientConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem(PromptCatalogue.SYSTEM_MESSAGE)
            .defaultAdvisors(new AiAuditAdvisor(), new SimpleLoggerAdvisor())
            .build();
    }
}
