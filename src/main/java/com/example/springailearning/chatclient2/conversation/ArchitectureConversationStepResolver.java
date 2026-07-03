package com.example.springailearning.chatclient2.conversation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.springailearning.chatclient2.dto.ConversationState;
import com.example.springailearning.chatclient2.enums.ArchitectureConversationStep;

@Component
public class ArchitectureConversationStepResolver {

    public List<String> missingFields(ConversationState state) {
        List<String> missing = new ArrayList<>();
        if (state.technology() == null) {
            missing.add("technology");
        }
        if (state.deploymentScale() == null) {
            missing.add("deploymentScale");
        }
        if (state.regulatedData() == null) {
            missing.add("regulatedData");
        }
        return missing;
    }

    public ArchitectureConversationStep resolve(ConversationState state) {
        List<String> missing = missingFields(state);
        if (!missing.isEmpty()) {
            return ArchitectureConversationStep.COLLECTING_CONTEXT;
        }
        return ArchitectureConversationStep.READY_FOR_REVIEW;
    }
}
