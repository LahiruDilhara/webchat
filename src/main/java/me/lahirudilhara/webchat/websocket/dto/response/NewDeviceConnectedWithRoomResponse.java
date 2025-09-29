package me.lahirudilhara.webchat.websocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class NewDeviceConnectedWithRoomResponse extends ClientMessage{
    private final String type = "NewDeviceConnected";
}
