package com.example.springailearning.chatclient2.state;

import java.time.Instant;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.example.springailearning.chatclient2.dto.ConversationState;

@Service
public class ConversationStateUpdater {

    public ConversationState updateFromUserMessage(ConversationState current, String message) {

        String normalized = message == null ? "" : message.toLowerCase(Locale.ROOT);

        String technology = current.technology();
        String deploymentScale = current.deploymentScale();
        Boolean regulatedData = current.regulatedData();
        String currentStep = current.currentStep();
        String pendingAction = current.pendingAction();

        if (normalized.contains("kafka")) {
            technology = "Kafka";
        } else if (normalized.contains("rabbitmq") || normalized.contains("rabbit mq")) {
            technology = "RabbitMQ";
        } else if (normalized.contains("kubernetes") || normalized.contains("k8s")) {
            technology = "Kubernetes";
        }

        if (normalized.contains("enterprise") || normalized.contains("large scale")) {
            deploymentScale = "enterprise";
        } else if (normalized.contains("small")) {
            deploymentScale = "small";
        }

        if (normalized.contains("regulated") || normalized.contains("pii") || normalized.contains("payment data")) {
            regulatedData = true;
        }

        if (technology != null && deploymentScale != null) {
            currentStep = "architecture_review";
            pendingAction = "review_risks";
        }

        return new ConversationState(current.conversationId(), technology, deploymentScale, regulatedData, currentStep, pendingAction, Instant.now());
    }
}