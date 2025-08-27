package me.lahirudilhara.webchat.service.websocket.messageHandlers;

import me.lahirudilhara.webchat.dto.websocket.user.JoinRoomRequestMessageDto;
import me.lahirudilhara.webchat.websocket.UserMessageHandler;
import org.springframework.stereotype.Service;

@Service
public class JoinRoomRequestMessageHandler implements UserMessageHandler<JoinRoomRequestMessageDto> {
    @Override
    public Class<JoinRoomRequestMessageDto> getMessageType() {
        return JoinRoomRequestMessageDto.class;
    }

    @Override
    public void handleMessage(JoinRoomRequestMessageDto message, String senderUsername) {
        System.out.println("Handling join room request");
    }
}
