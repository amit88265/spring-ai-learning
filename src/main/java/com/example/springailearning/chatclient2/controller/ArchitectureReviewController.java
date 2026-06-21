package com.example.springailearning.chatclient2.controller;

import jakarta.validation.Valid;

import org.jspecify.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springailearning.chatclient2.dto.CompareRequest;
import com.example.springailearning.chatclient2.dto.CompareResponse;
import com.example.springailearning.chatclient2.dto.ObservabilitySummaryResponse;
import com.example.springailearning.chatclient2.dto.ProviderInfoResponse;
import com.example.springailearning.chatclient2.dto.ReviewRequest;
import com.example.springailearning.chatclient2.dto.ReviewResponse;
import com.example.springailearning.chatclient2.dto.ToolAwareReviewResponse;
import com.example.springailearning.chatclient2.service.ArchitectureReviewService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/reviews")
public class ArchitectureReviewController {

    private final ArchitectureReviewService architectureReviewService;

    @PostMapping(value = "/architecture/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        return architectureReviewService.streamReview(reviewRequest);
    }

    @PostMapping(value = "/compare/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamCompare(@Valid @RequestBody CompareRequest compareRequest) {
        return architectureReviewService.streamCompare(compareRequest);
    }

    @PostMapping("/architecture")
    public ReviewResponse review(@Valid @RequestBody ReviewRequest reviewRequest) {
        return architectureReviewService.review(reviewRequest);
    }

    @PostMapping("/compare")
    public CompareResponse compare(@Valid @RequestBody CompareRequest compareRequest) {
        return architectureReviewService.compare(compareRequest);
    }

    @GetMapping("/provider")
    public ProviderInfoResponse providerInfo() {
        return architectureReviewService.providerInfo();
    }

    @GetMapping("/observability/summary")
    public ObservabilitySummaryResponse observabilitySummary() {
        return architectureReviewService.observabilitySummary();
    }

    public ArchitectureReviewController(ArchitectureReviewService architectureReviewService) {
        this.architectureReviewService = architectureReviewService;
    }
    @PostMapping("/architecture/tools")
    public @Nullable ToolAwareReviewResponse reviewWithTools(
        @Valid @RequestBody ReviewRequest reviewRequest) {

        return architectureReviewService.reviewWithTools(reviewRequest);
    }
}
