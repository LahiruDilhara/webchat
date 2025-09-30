package me.lahirudilhara.webchat.websocket.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class DeviceDisconnectedResponse extends BaseResponseMessage{
    private final String type = "DeviceDisconnected";
}
