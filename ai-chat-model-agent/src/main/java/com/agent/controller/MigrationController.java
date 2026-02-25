package com.agent.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MigrationController {
    
    private static final Logger logger = LoggerFactory.getLogger(MigrationController.class);
    private final ChatModel chatModel;
    
    public MigrationController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/api/va/chats")
    public ResponseEntity<?> prompts(
            @RequestParam(value = "message", defaultValue = "What is the weather of Hyderabad?") String message) {
        try {
            logger.info("Processing chat request with message: {}", message);
            
            UserMessage msg = new UserMessage(message);
            ChatResponse res = chatModel.call(new Prompt(List.of(msg)));
            
            if (res == null || res.getResult() == null) {
                logger.warn("Empty response from ChatModel");
                return ResponseEntity.status(500).body(Map.of("error", "No response from AI"));
            }
            
            String responseText = res.getResult().getOutput().getText();
            logger.info("Chat response generated successfully");
            
            return ResponseEntity.ok(Map.of("response", responseText));
        } catch (Exception e) {
            logger.error("Error processing chat request", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}