package com.example.springailearning.chatclient2.dto;

public record RiskScoreRequest(

    String technology,

    String deploymentScale,

    boolean regulatedData

) {

}
