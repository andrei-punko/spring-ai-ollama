package by.andd3dfx.ai.service;

import by.andd3dfx.ai.controller.dto.MsgRequest;
import by.andd3dfx.ai.controller.dto.MsgResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder
                // Adjust system message to define role of AI assistant
                .defaultSystem("You are a AI assistant answering questions to user")
                .build();
    }

    public MsgResponse generate(MsgRequest request) {
        var prompt = request.getPrompt();
        log.info("Got prompt: {}", prompt);

        var answer = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        return MsgResponse.builder().answer(answer).build();
    }
}
