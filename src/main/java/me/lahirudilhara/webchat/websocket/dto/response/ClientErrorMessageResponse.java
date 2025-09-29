package me.lahirudilhara.webchat.websocket.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ClientErrorMessageResponse extends ClientMessage {
    private String error;
    private final String type = "Error";
}
