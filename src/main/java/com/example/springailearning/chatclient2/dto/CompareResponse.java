package com.example.springailearning.chatclient2.dto;

import java.util.List;

public record CompareResponse(

    String comparison,

    List<String> strengths,

    List<String> weaknesses,

    List<String> recommendations,

    String model,

    Long latencyMs) {

}
