package me.lahirudilhara.webchat.websocket;

import me.lahirudilhara.webchat.core.lib.ErrorResponse;
import me.lahirudilhara.webchat.dto.websocket.message.SendMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
public class WebChatController {
    public void onMessage(String payload, WebSocketSession session) throws IOException {
        // parse the message from json
        SendMessageDTO sendMessageDTO = null;
        try{
            sendMessageDTO = SendMessageDTO.fromJson(payload);
        }
        catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse("Invalid message format", HttpStatus.BAD_REQUEST);
            session.sendMessage(new TextMessage(errorResponse.toJson()));
        }

    }
}
