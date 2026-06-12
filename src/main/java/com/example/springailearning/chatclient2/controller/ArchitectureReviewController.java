package com.example.springailearning.chatclient2.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springailearning.chatclient2.dto.CompareRequest;
import com.example.springailearning.chatclient2.dto.CompareResponse;
import com.example.springailearning.chatclient2.dto.ReviewRequest;
import com.example.springailearning.chatclient2.dto.ReviewResponse;
import com.example.springailearning.chatclient2.service.ArchitectureReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
public class ArchitectureReviewController {

    private final ArchitectureReviewService architectureReviewService;

    @PostMapping("/architecture")
    public ReviewResponse review(@Valid @RequestBody ReviewRequest reviewRequest) {
       return architectureReviewService.review(reviewRequest);
    }

    @PostMapping("/compare")
    public CompareResponse compare(@Valid @RequestBody CompareRequest compareRequest) {
       return architectureReviewService.compare(compareRequest);
    }

    public ArchitectureReviewController(ArchitectureReviewService architectureReviewService) {
        this.architectureReviewService = architectureReviewService;
    }
}
