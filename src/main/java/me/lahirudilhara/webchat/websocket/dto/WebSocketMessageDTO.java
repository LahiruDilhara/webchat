package me.lahirudilhara.webchat.websocket.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lahirudilhara.webchat.websocket.refactor.dispatcher.PacketType;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextMessageDTO.class, name = "Text"),
        @JsonSubTypes.Type(value = JoinRoomMessageDTO.class, name = "Join")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessageDTO {
    @NotNull(message = "The type cannot be null")
    private PacketType type;

    @NotNull(message = "The roomId should not be null")
    @Min(value = 1,message = "The roomId should be greater than 0")
    private Integer roomId;

    @NotNull(message = "The uuid cannot be null")
    @NotEmpty(message = "The uuid cannot be empty")
    @Size(min = 5, max = 50, message = "The uuid size should be between 5 and 50")
    private String uuid;
}
