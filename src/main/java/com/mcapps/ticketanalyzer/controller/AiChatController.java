package com.mcapps.ticketanalyzer.controller;

import com.mcapps.ticketanalyzer.ai.AiChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @GetMapping("/chat")
    public ChatResponse chat(@RequestParam String message) {
        String answer = aiChatService.ask(message);
        return new ChatResponse(answer);
    }

    public record ChatResponse(String answer) {
    }
}
