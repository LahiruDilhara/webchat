package me.lahirudilhara.webchat.websocket.refactor.handlers;

import me.lahirudilhara.webchat.websocket.dto.requests.JoinRoomMessageDTO;

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
