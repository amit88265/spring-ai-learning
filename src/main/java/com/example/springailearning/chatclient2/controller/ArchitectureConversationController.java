package com.example.springailearning.chatclient2.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springailearning.chatclient2.dto.ConversationState;
import com.example.springailearning.chatclient2.dto.request.ArchitectureConversationRequest;
import com.example.springailearning.chatclient2.dto.response.ArchitectureConversationResponse;
import com.example.springailearning.chatclient2.service.ArchitectureConversationService;

@RestController
@RequestMapping("/api/conversations/architecture-review")
public class ArchitectureConversationController {

    private final ArchitectureConversationService service;

    public ArchitectureConversationController(ArchitectureConversationService service) {
        this.service = service;
    }

    @PostMapping
    public ArchitectureConversationResponse chat(@Valid @RequestBody ArchitectureConversationRequest request) {
        return service.chat(request);
    }

    @GetMapping("/{conversationId}")
    public ConversationState getState(@PathVariable String conversationId) {
        return service.getState(conversationId);
    }

    @DeleteMapping("/{conversationId}")
    public void clear(@PathVariable String conversationId) {
        service.clear(conversationId);
    }
}
