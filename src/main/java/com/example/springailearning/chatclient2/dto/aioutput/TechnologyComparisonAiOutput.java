package com.example.springailearning.chatclient2.dto.aioutput;

import java.util.List;

public record TechnologyComparisonAiOutput(

    String summary,

    List<String> keyDifferences,

    List<String> technology1Strengths,

    List<String> technology2Strengths,

    List<String> recommendations

) {

}
