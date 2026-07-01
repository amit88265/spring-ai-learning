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
import com.example.springailearning.chatclient2.dto.request.StatefulChatRequest;
import com.example.springailearning.chatclient2.dto.response.StatefulChatResponse;
import com.example.springailearning.chatclient2.service.StatefulChatService;

@RestController
@RequestMapping("/api/chat/stateful")
public class StatefulChatController {

    private final StatefulChatService statefulChatService;

    public StatefulChatController(StatefulChatService statefulChatService) {
        this.statefulChatService = statefulChatService;
    }

    @PostMapping
    public StatefulChatResponse chat(@Valid @RequestBody StatefulChatRequest request) {
        return statefulChatService.chat(request);
    }

    @GetMapping("/{conversationId}")
    public ConversationState getState(@PathVariable String conversationId) {
        return statefulChatService.getState(conversationId);
    }

    @DeleteMapping("/{conversationId}")
    public void clear(@PathVariable String conversationId) {
        statefulChatService.clear(conversationId);
    }
}
