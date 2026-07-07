package com.example.springailearning.chatclient2.error;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
    Instant timestamp,
    int status,
    AiErrorCode code,
    String message,
    String path,
    List<String> details
) {
}
