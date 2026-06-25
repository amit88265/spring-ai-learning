package com.example.springailearning.chatclient2.service;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.function.Function;

import com.example.springailearning.chatclient2.dto.request.RiskScoreRequest;
import com.example.springailearning.chatclient2.dto.response.RiskScoreResponse;

@Component
public class TechnologyRiskScoreFunction
    implements Function<RiskScoreRequest, RiskScoreResponse> {

    @Override
    public RiskScoreResponse apply(RiskScoreRequest request) {
        String technology = normalize(request.technology());
        String scale = normalize(request.deploymentScale());

        int score = 20;

        if ("kafka".equals(technology)) {
            score += 25;
        }
        else if ("kubernetes".equals(technology) || "k8s".equals(technology)) {
            score += 30;
        }
        else if ("rabbitmq".equals(technology) || "rabbit mq".equals(technology)) {
            score += 15;
        }
        else {
            score += 10;
        }

        if ("enterprise".equals(scale) || "large".equals(scale)) {
            score += 20;
        }
        else if ("medium".equals(scale)) {
            score += 10;
        }

        if (request.regulatedData()) {
            score += 15;
        }

        int boundedScore = Math.min(score, 100);

        String level = boundedScore >= 70
            ? "HIGH"
            : boundedScore >= 40
            ? "MEDIUM"
            : "LOW";

        String reasoning = """
                Risk score is based on technology complexity, deployment scale,
                and whether regulated data is involved.
                """;

        return new RiskScoreResponse(
            request.technology(),
            boundedScore,
            level,
            reasoning
        );
    }

    private String normalize(String value) {
        return value == null
            ? ""
            : value.trim().toLowerCase(Locale.ROOT);
    }
}