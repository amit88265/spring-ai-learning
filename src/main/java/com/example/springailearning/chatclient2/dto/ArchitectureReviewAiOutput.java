package com.example.springailearning.chatclient2.dto;

import java.util.List;

public record ArchitectureReviewAiOutput(

    String summary,

    List<String> strengths,

    List<String> weaknesses,

    List<String> recommendations

) {

}
