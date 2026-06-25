package com.example.springailearning.chatclient2.config;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springailearning.chatclient2.dto.request.RiskScoreRequest;
import com.example.springailearning.chatclient2.service.TechnologyRiskScoreFunction;

@Configuration
public class FunctionToolConfig {

    @Bean
    ToolCallback technologyRiskScoreTool(TechnologyRiskScoreFunction riskScoreFunction) {

        return FunctionToolCallback
            .builder("calculateTechnologyRiskScore", riskScoreFunction)
            .description("""
                        Calculate a deterministic enterprise adoption risk score
                        for a technology. Use this when the user asks about
                        adoption risk, production risk, regulated-data risk,
                        or deployment complexity.
                        """)
            .inputType(RiskScoreRequest.class)
            .build();
    }
}
