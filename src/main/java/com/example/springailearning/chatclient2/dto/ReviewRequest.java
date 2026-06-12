package com.example.springailearning.chatclient2.dto;

import jakarta.validation.constraints.NotBlank;

public record ReviewRequest(@NotBlank String technology) {

}
