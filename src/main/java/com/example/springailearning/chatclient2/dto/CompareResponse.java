package com.example.springailearning.chatclient2.dto;

public record CompareResponse(

    String comparison,

    String model,

    Long latencyMs

) {

}
