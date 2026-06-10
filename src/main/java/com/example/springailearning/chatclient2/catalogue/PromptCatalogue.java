package com.example.springailearning.chatclient2.catalogue;

public final class PromptCatalogue {

    private PromptCatalogue() {
    }

    public static final String SENIOR_ARCHITECT_REVIEW_PROMPT = """
        
        You are a Principal Software Architect.
        
        Review {technology} from the perspective of:
        
        1. Scalability
        
        2. Reliability
        
        3. Security
        
        4. Production Readiness
        
        Provide recommendations.
        
        """;

}
