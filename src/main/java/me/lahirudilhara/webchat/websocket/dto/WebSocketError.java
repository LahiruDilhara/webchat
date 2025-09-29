package me.lahirudilhara.webchat.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketError {
    private String error;
    private final String type = "ConnectionError";
}