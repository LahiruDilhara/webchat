package me.lahirudilhara.webchat.dto.wc;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lahirudilhara.webchat.websocket.dispatcher.PacketType;

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

    @NotNull
    @Min(value = 1,message = "RoomId should be greater than 0")
    private Integer roomId;
}
