package me.lahirudilhara.webchat.websocket.dispatcher.handlers;

import me.lahirudilhara.webchat.websocket.dispatcher.MessageHandler;
import me.lahirudilhara.webchat.websocket.dto.requests.RemainMessageDTO;
import org.springframework.stereotype.Component;

@Component
public class RemainMessageRequestHandler implements MessageHandler<RemainMessageDTO> {
    @Override
    public Class<RemainMessageDTO> getMessageClassType() {
        return  RemainMessageDTO.class;
    }

    @Override
    public void handleMessage(RemainMessageDTO message, String senderUsername, String sessionId) {
        System.out.println("Handling remain message request");
    }
}
