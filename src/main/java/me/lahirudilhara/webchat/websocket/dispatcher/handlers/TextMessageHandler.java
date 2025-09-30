package me.lahirudilhara.webchat.websocket.dispatcher.handlers;

import me.lahirudilhara.webchat.websocket.dispatcher.MessageHandler;
import me.lahirudilhara.webchat.websocket.dto.requests.TextMessageDTO;
import org.springframework.stereotype.Component;

@Component
public class TextMessageHandler implements MessageHandler<TextMessageDTO> {
    @Override
    public Class<TextMessageDTO> getMessageClassType() {
        return TextMessageDTO.class;
    }

    @Override
    public void handleMessage(TextMessageDTO message, String senderUsername, String sessionId) {
        System.out.println("Handling text message request message");
    }
}
