package com.example.springailearning.chatclient2.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springailearning.chatclient2.dto.ReviewRequest;
import com.example.springailearning.chatclient2.dto.ReviewResponse;
import com.example.springailearning.chatclient2.service.ArchitectureReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ArchitectureReviewController {

    final ArchitectureReviewService architectureReviewService;

    @PostMapping("/architecture")
    public ReviewResponse prompt(@RequestBody ReviewRequest reviewRequest) {
       return architectureReviewService.callModel(reviewRequest);
    }

    public ArchitectureReviewController(ArchitectureReviewService architectureReviewService) {
        this.architectureReviewService = architectureReviewService;
    }
}
