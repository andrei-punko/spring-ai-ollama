package by.andd3dfx.ai.controller;

import by.andd3dfx.ai.controller.dto.MsgRequest;
import by.andd3dfx.ai.service.ChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    ChatService chatService;
    @InjectMocks
    ChatController chatController;

    @Test
    void generate() {
        var request = MsgRequest.builder().build();
        var response = "Some answer";
        when(chatService.generate(request)).thenReturn(response);

        var result = chatController.generate(request);

        assertThat(result).isEqualTo(response);
    }
}
