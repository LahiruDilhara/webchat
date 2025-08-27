package me.lahirudilhara.webchat.dto.wc;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.lahirudilhara.webchat.service.websocket.PacketType;

public class WebSocketTextMessageDTO extends WebSocketMessageDTO {
    @NotNull(message = "The message cannot be null")
    @NotEmpty(message = "The message cannot be empty")
    @Size(min = 1, max = 2000,message = "The message size should be between 1 and 2000")
    private String message;

    public WebSocketTextMessageDTO() {
    }

    public WebSocketTextMessageDTO(PacketType type, int roomId, String message) {
        super(type, roomId);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
