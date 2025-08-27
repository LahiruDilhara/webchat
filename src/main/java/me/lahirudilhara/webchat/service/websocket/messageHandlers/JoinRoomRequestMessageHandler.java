package me.lahirudilhara.webchat.service.websocket.messageHandlers;

import me.lahirudilhara.webchat.dto.wc.WebSocketJoinRoomMessageDTO;
import me.lahirudilhara.webchat.websocket.MessageHandler;
import org.springframework.stereotype.Service;

@Service
public class JoinRoomRequestMessageHandler implements MessageHandler<WebSocketJoinRoomMessageDTO> {
    @Override
    public Class<WebSocketJoinRoomMessageDTO> getMessageType() {
        return WebSocketJoinRoomMessageDTO.class;
    }

    @Override
    public void handleMessage(WebSocketJoinRoomMessageDTO message, String senderUsername) {
        System.out.println("Handling join room request");
    }
}
