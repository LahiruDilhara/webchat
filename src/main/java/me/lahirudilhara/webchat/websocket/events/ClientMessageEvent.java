package me.lahirudilhara.webchat.websocket.events;

import me.lahirudilhara.webchat.dto.wc.WebSocketMessageDTO;

public class ClientMessageEvent {
    private final String username;
    private final WebSocketMessageDTO messageDTO;

    public ClientMessageEvent(String username, WebSocketMessageDTO messageDTO) {
        this.username = username;
        this.messageDTO = messageDTO;
    }

    public String getUsername() {
        return username;
    }

    public WebSocketMessageDTO getMessageDTO() {
        return messageDTO;
    }
}
