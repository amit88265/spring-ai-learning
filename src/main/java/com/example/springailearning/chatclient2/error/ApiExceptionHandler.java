package com.example.springailearning.chatclient2.error;

import java.time.Instant;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.ai.retry.TransientAiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> details = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> "%s: %s".formatted(error.getField(), error.getDefaultMessage()))
            .toList();

        return buildResponse(HttpStatus.BAD_REQUEST, AiErrorCode.VALIDATION_ERROR, "Request validation failed.", request, details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<String> details = ex.getConstraintViolations()
            .stream()
            .map(violation -> "%s: %s".formatted(violation.getPropertyPath(), violation.getMessage()))
            .toList();

        return buildResponse(HttpStatus.BAD_REQUEST, AiErrorCode.VALIDATION_ERROR, "Request validation failed.", request, details);
    }

    @ExceptionHandler(TransientAiException.class)
    public ResponseEntity<ApiErrorResponse> handleTransientAi(TransientAiException ex, HttpServletRequest request) {
        log.warn("Transient AI provider failure. path={}", request.getRequestURI(), ex);
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, AiErrorCode.AI_PROVIDER_TRANSIENT_FAILURE,
            "AI provider is temporarily unavailable. Please retry later.", request, List.of());
    }

    @ExceptionHandler(NonTransientAiException.class)
    public ResponseEntity<ApiErrorResponse> handleNonTransientAi(NonTransientAiException ex, HttpServletRequest request) {
        log.warn("Non-transient AI provider failure. path={}", request.getRequestURI(), ex);
        return buildResponse(HttpStatus.BAD_GATEWAY, AiErrorCode.AI_PROVIDER_NON_TRANSIENT_FAILURE,
            "AI provider request failed.", request, List.of());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        if (isTokenBudgetExceeded(ex)) {
            log.warn("Token budget exceeded. path={}", request.getRequestURI(), ex);
            return buildResponse(HttpStatus.BAD_REQUEST, AiErrorCode.TOKEN_BUDGET_EXCEEDED,
                "Token budget exceeded for this request.", request, List.of());
        }

        log.error("Unhandled application error. path={}", request.getRequestURI(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, AiErrorCode.INTERNAL_ERROR,
            "Unexpected server error.", request, List.of());
    }

    private boolean isTokenBudgetExceeded(Throwable ex) {
        Throwable current = ex;
        while (current != null) {
            if ("TokenBudgetExceededException".equals(current.getClass()
                .getSimpleName())) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
        HttpStatus status,
        AiErrorCode code,
        String message,
        HttpServletRequest request,
        List<String> details) {

        return ResponseEntity.status(status)
            .body(new ApiErrorResponse(
                Instant.now(),
                status.value(),
                code,
                message,
                request.getRequestURI(),
                details
            ));
    }
}
