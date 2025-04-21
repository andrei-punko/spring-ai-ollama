package by.andd3dfx.ai.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MsgRequest {

    private String prompt;
}
