package me.lahirudilhara.webchat.websocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewRoomUserResponse extends ClientMessage {
    private String username;
    private Integer roomId;
}
