package me.lahirudilhara.webchat.websocket.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class TypingResponse extends BaseResponseMessage {
    private String username;
    private Integer roomId;
    private final String type = "Typing";
}
