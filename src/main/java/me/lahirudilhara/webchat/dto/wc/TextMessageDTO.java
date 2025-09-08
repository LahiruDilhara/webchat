package me.lahirudilhara.webchat.dto.wc;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lahirudilhara.webchat.websocket.dispatcher.PacketType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextMessageDTO extends WebSocketMessageDTO {
    @NotNull(message = "The message cannot be null")
    @NotEmpty(message = "The message cannot be empty")
    @Size(min = 1, max = 2000,message = "The message size should be between 1 and 2000")
    private String message;

    @NotNull(message = "The uuid cannot be null")
    @NotEmpty(message = "The uuid cannot be empty")
    @Size(min = 5, max = 50, message = "The uuid size should be between 5 and 50")
    private String uuid;
}