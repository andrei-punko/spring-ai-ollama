package by.andd3dfx.ai.controller;

import by.andd3dfx.ai.controller.dto.MsgRequest;
import by.andd3dfx.ai.controller.dto.MsgResponse;
import by.andd3dfx.ai.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/api/generate")
    public MsgResponse generate(@RequestBody MsgRequest request) {
        return chatService.generate(request);
    }
}
