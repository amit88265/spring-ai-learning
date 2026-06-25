package com.example.springailearning.chatclient2.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springailearning.chatclient2.dto.request.MemoryChatRequest;
import com.example.springailearning.chatclient2.dto.response.ConversationMemorySummaryResponse;
import com.example.springailearning.chatclient2.dto.response.MemoryChatResponse;
import com.example.springailearning.chatclient2.service.MemoryChatService;

;

@RestController
@RequestMapping("/api/chat")
public class MemoryChatController {

    private final MemoryChatService memoryChatService;

    public MemoryChatController(MemoryChatService memoryChatService) {
        this.memoryChatService = memoryChatService;
    }

    @PostMapping("/memory")
    public MemoryChatResponse chat(@Valid @RequestBody MemoryChatRequest request) {
        return memoryChatService.chat(request);
    }

    @DeleteMapping("/memory/{conversationId}")
    public void clear(@PathVariable String conversationId) {
        memoryChatService.clear(conversationId);
    }

    @GetMapping("/memory/{conversationId}")
    public ConversationMemorySummaryResponse summary(@PathVariable String conversationId) {
        return memoryChatService.summary(conversationId);
    }

}
