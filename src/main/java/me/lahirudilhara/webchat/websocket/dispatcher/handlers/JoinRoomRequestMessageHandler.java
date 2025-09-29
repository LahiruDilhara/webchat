package me.lahirudilhara.webchat.websocket.dispatcher.handlers;

import me.lahirudilhara.webchat.websocket.dto.JoinRoomMessageDTO;
import me.lahirudilhara.webchat.websocket.dispatcher.MessageHandler;
import org.springframework.stereotype.Component;

@Component
public class JoinRoomRequestMessageHandler implements MessageHandler<JoinRoomMessageDTO> {
    @Override
    public Class<JoinRoomMessageDTO> getMessageClassType() {
        return JoinRoomMessageDTO.class;
    }

    @Override
    public void handleMessage(JoinRoomMessageDTO message, String senderUsername, String sessionId) {
        System.out.println("Handling join room request message");
    }
}
