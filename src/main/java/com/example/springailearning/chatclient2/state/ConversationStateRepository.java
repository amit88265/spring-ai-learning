package com.example.springailearning.chatclient2.state;

import com.example.springailearning.chatclient2.dto.ConversationState;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ConversationStateRepository {

    private final Map<String, ConversationState> store =
        new ConcurrentHashMap<>();

    public ConversationState getOrCreate(String conversationId) {
        return store.computeIfAbsent(
            conversationId,
            ConversationState::empty
        );
    }

    public ConversationState save(ConversationState state) {
        store.put(state.conversationId(), state);
        return state;
    }

    public void delete(String conversationId) {
        store.remove(conversationId);
    }
}