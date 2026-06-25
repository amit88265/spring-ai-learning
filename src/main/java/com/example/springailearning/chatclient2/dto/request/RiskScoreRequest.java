package com.example.springailearning.chatclient2.dto.request;

public record RiskScoreRequest(

    String technology,

    String deploymentScale,

    boolean regulatedData

) {

}
