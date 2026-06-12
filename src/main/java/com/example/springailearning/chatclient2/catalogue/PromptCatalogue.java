package com.example.springailearning.chatclient2.catalogue;

public final class PromptCatalogue {

    private PromptCatalogue() {
    }

    public static final String SYSTEM_MESSAGE = """
        You are a Principal Software Architect.
        Provide practical, unbiased analysis for Java and Spring engineers.
        Follow the requested structured output fields exactly.
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

}
