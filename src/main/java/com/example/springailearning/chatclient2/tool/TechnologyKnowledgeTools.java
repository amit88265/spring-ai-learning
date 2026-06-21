package com.example.springailearning.chatclient2.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TechnologyKnowledgeTools {

    private static final Map<String, String> TECHNOLOGY_NOTES = Map.of(
        "Kafka", """
                    Kafka is preferred for high-throughput event streaming,
                    durable logs, async workflows, and event-driven architecture.
                    Main risks: operational complexity, partition planning,
                    rebalancing, schema governance, and consumer lag.
                    """,
        "RabbitMQ", """
                    RabbitMQ is preferred for traditional message queues,
                    routing, request/reply patterns, and simpler operational models.
                    Main risks: throughput limits compared to Kafka and queue buildup.
                    """,
        "Kubernetes", """
                    Kubernetes is preferred for container orchestration,
                    self-healing workloads, horizontal scaling, and deployment standardization.
                    Main risks: platform complexity, cluster governance, and cost control.
                    """
    );

    @Tool(description = """
            Get internal architecture notes for a technology.
            Use this when reviewing technology adoption, architecture risks,
            production readiness, or enterprise suitability.
            """)
    public String getTechnologyArchitectureNotes(
        @ToolParam(description = "Technology name, for example Kafka, RabbitMQ, or Kubernetes")
        String technology) {

        String normalizedTechnology = technology == null ? "" : technology.trim()
            .toLowerCase();

        return switch (normalizedTechnology) {
            case "kafka" -> TECHNOLOGY_NOTES.get("Kafka");
            case "rabbitmq", "rabbit mq" -> TECHNOLOGY_NOTES.get("RabbitMQ");
            case "kubernetes", "k8s" -> TECHNOLOGY_NOTES.get("Kubernetes");
            default -> "No internal architecture notes are available for: " + technology;
        };
    }
}
