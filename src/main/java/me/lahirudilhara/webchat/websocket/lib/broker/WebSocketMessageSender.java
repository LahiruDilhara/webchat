package me.lahirudilhara.webchat.websocket.lib.broker;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageSender;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Component
public class WebSocketMessageSender implements MessageSender {
    @Override
    public void sendMessage(String message, WebSocketSession session) {
        if(message == null || message.isEmpty()) return;
        if (session == null || !session.isOpen()) return;
        try{
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            log.error("Error while sending message", e);
        }
    }
}
