package by.andd3dfx.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder
                // Adjust system message to define role of AI assistant
                // .defaultSystem("You are a AI assistant answering questions to Russian user")
                .build();
    }

    @GetMapping("/ai/generate")
    public String generate(@RequestParam(value = "message") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
