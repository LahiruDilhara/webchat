package me.lahirudilhara.webchat.websocket.refactor.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketUserSession {
    String username;
    Instant connectedAt;
    WebSocketSession session;
}
