package me.lahirudilhara.webchat.websocket.handlers;

import me.lahirudilhara.webchat.dto.wc.JoinRoomMessageDTO;
import org.springframework.stereotype.Service;

@Service
public class JoinRoomRequestMessageHandler implements MessageHandler<JoinRoomMessageDTO> {
    @Override
    public Class<JoinRoomMessageDTO> getMessageType() {
        return JoinRoomMessageDTO.class;
    }

    @Override
    public void handleMessage(JoinRoomMessageDTO message, String senderUsername, String sessionId) {
        System.out.println("Handling join room request");
    }
}
