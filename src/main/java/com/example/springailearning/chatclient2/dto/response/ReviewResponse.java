package com.example.springailearning.chatclient2.dto.response;

import java.util.List;

import com.example.springailearning.chatclient2.dto.TokenUsage;

public record ReviewResponse(

    String summary,

    List<String> strengths,

    List<String> weaknesses,

    List<String> recommendations,

    String provider,

    String model,

    String profile,

    Long latencyMs,

    String promptVersion,

    TokenUsage tokenUsage) {

}
