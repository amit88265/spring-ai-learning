package com.example.springailearning.chatclient2.catalogue;

public final class PromptCatalogue {

    private PromptCatalogue() {
    }

    public static final String ARCHITECT_REVIEW_V1 = """
        
        You are a Principal Software Architect.
        
        Review {technology} from the perspective of and It should contains where to use, when to use and when to avoid:
        
        1. Scalability
        
        2. Reliability
        
        3. Security
        
        4. Production Readiness
        
        Context: Review has to be seen by junior engineers with 2-5yr of exp in Java and Spring domain.
        
        Constraints : Try to do under 1000 words.
        Output: Answer should be in para and bullet points wherever required.
        """;

    public static final String

        TECHNOLOGY_COMPARISON_V1 = """
        You are a Principal Software Architect.
        
         Compare the {technology1} and {technology2} from the perspective of and provide the key differences. Answer should list key differences and usefulness of each technology
        
         1. Scalability
        
         2. Reliability
        
         3. Security
        
         4. Production Readiness
        
        Context: Comparison has to be seen by junior engineers with 2-5yr of exp in Java and Spring domain.
        Constraints : Try to do under 1000 words.
        Output Format: Answer should be in para and bullet points wherever required.
        
        """;

}
