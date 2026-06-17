package com.example.springailearning.chatclient2.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;

public class AiAuditAdvisor implements CallAdvisor {

    private static final Logger log = LoggerFactory.getLogger(AiAuditAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {

        long startTime = System.currentTimeMillis();
        try {
            log.info("AI request started. advisor={}", getName());
            ChatClientResponse response = callAdvisorChain.nextCall(chatClientRequest);
            long latencyMs = System.currentTimeMillis() - startTime;
            log.info("AI request completed. latencyMs={}", latencyMs);
            return response;
        } catch (RuntimeException ex) {
            long latencyMs = System.currentTimeMillis() - startTime;
            log.warn("AI request failed. latencyMs={}", latencyMs, ex);

            throw ex;

        }
    }

    @Override
    public String getName() {
        return "ai-audit-advisor";
    }

    @Override
    public int getOrder() {
        return 1000;
    }
}
