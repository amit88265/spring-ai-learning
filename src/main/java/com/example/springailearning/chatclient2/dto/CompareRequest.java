package com.example.springailearning.chatclient2.dto;

import jakarta.validation.constraints.NotBlank;

public record CompareRequest(@NotBlank String technology1, @NotBlank String technology2) {

}
