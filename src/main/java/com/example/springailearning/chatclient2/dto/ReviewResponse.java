package com.example.springailearning.chatclient2.dto;

public record ReviewResponse(

    String review,

    String model,

    Long latencyMs

){}
