package me.lahirudilhara.webchat.websocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
public class BaseResponseMessage {
    private String uuid;
}
