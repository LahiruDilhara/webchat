package me.lahirudilhara.webchat.dto.websocket.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import me.lahirudilhara.webchat.dto.websocket.PacketType;
import me.lahirudilhara.webchat.dto.websocket.TextMessage;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserTextMessageDto.class, name = "Text")
})
public class UserBaseMessageDto {
    @NotNull(message = "The type cannot be null")
    private PacketType type;

    @NotNull
    @Min(value = 1,message = "RoomId should be greater than 0")
    private int roomId;

    public UserBaseMessageDto() {
    }

    public UserBaseMessageDto(PacketType type, int roomId) {
        this.type = type;
        this.roomId = roomId;
    }

    public PacketType getType() {
        return type;
    }

    public void setType(PacketType type) {
        this.type = type;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
