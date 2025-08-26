package me.lahirudilhara.webchat.dto.websocket;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TextMessage extends BaseMessage{
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 2000)
    private String message;
}
