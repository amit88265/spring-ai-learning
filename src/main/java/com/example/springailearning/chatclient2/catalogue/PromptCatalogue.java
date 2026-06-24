package com.example.springailearning.chatclient2.catalogue;

public final class PromptCatalogue {

    private PromptCatalogue() {
    }

    public static final String ARCHITECT_REVIEW_STREAM_V1 = """
        Review {technology} for enterprise adoption.
        
        Audience:
        Java and Spring Boot engineers.
        
        Cover:
        - What it is
        - Where it fits
        - Strengths
        - Weaknesses
        - Production risks
        - Recommendations
        
        Write in clear sections.
        Do not return JSON.
        """;

    public static final String ARCHITECT_COMPARE_STREAM_V1 = """
        Compare {technology1} and {technology2} for enterprise adoption.
        
        Audience:
        Java and Spring Boot engineers.
        
        Cover:
        - What it is
        - Where it fits
        - Strengths
        - Weaknesses
        - Production risks
        - Recommendations
        
        Write in clear sections.
        Do not return JSON.
        """;

    public static final String SYSTEM_MESSAGE = """
        You are a Principal Software Architect.
        
        Provide practical, unbiased analysis for Java and Spring engineers.
        
        Focus on enterprise-grade architecture, production readiness,
        
        scalability, reliability, and security.
        """;

    public static final String ARCHITECT_REVIEW_V1 = """
        Review {technology} for enterprise adoption.
        
        Audience:
        Junior engineers with 2-5 years of Java and Spring experience.
        
        Focus on:
        - Where to use it
        - When to avoid it
        - Scalability
        - Reliability
        - Security
        - Production readiness
        
        Populate the structured response fields:
        - summary
        - strengths
        - weaknesses
        - recommendations
        """;

    public static final String TECHNOLOGY_COMPARISON_V1 = """
        Compare {technology1} and {technology2} for enterprise adoption.
        
        Audience:
        Junior engineers with 2-5 years of Java and Spring experience.
        
        Focus on:
        - Key differences
        - Strengths of {technology1}
        - Strengths of {technology2}
        - Scalability
        - Reliability
        - Security
        - Production readiness
        
        Populate the structured response fields:
        - summary
        - keyDifferences
        - technology1Strengths
        - technology2Strengths
        - recommendations
        """;
    public static final String TOOL_AWARE_REVIEW_V1 = """
        Review {technology} for enterprise adoption.
        
        Use the available architecture-notes tool when useful.
        Explicitly mention whether tool data was available.
        
        Populate the structured response fields:
        - summary
        - toolDataAvailable
        - toolFactsUsed
        - strengths
        - risks
        - recommendations
        """;

    public static final String FUNCTION_TOOL_AWARE_REVIEW_V1 = """
        Review {technology} for enterprise adoption.
        
        Use the risk-score function if risk scoring is useful.
        Assume deploymentScale is "enterprise".
        Assume regulatedData is true.
        
        Return:
            - Summary
                        - Whether risk score function was used
                        - Risk score
                        - Risk level
                        - Main risks
                        - Recommendations
        """;
    public static final String TOOL_AWARE_ARCHITECT_REVIEW_PROMPT_VERSION = "tool-aware-architect-review-v1";
    public static final String FUNCTION_TOOL_AWARE_ARCHITECT_REVIEW_PROMPT_VERSION = "tool-aware-architect-review-v1";
    public static final String ARCHITECT_REVIEW_PROMPT_VERSION = "architect-review-v1";

    public static final String TECHNOLOGY_COMPARISON_PROMPT_VERSION = "technology-comparison-v1";

    public static final String ARCHITECT_REVIEW_STREAM_PROMPT_VERSION = "architect-review-stream-v1";

    public static final String TECHNOLOGY_COMPARISON_STREAM_PROMPT_VERSION = "technology-comparison-stream-v1";
}
