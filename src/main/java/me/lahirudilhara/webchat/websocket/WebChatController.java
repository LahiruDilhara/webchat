package me.lahirudilhara.webchat.websocket;

import me.lahirudilhara.webchat.core.lib.ErrorResponse;
import me.lahirudilhara.webchat.dto.websocket.message.SendMessageDTO;
import me.lahirudilhara.webchat.service.websocket.WebSocketMessageService;
import me.lahirudilhara.webchat.service.websocket.WebSocketRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
public class WebChatController {
    private final WebSocketMessageService webSocketMessageService;
    private final WebSocketRoomService webSocketRoomService;

    public WebChatController(WebSocketMessageService webSocketMessageService, WebSocketRoomService webSocketRoomService) {
        this.webSocketMessageService = webSocketMessageService;
        this.webSocketRoomService = webSocketRoomService;
    }

    public void onMessage(String payload, String username)  {
        // parse the message from json
        SendMessageDTO sendMessageDTO = null;
        try{
            sendMessageDTO = SendMessageDTO.fromJson(payload);
        }
        catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse("Invalid message format", HttpStatus.BAD_REQUEST);
            webSocketMessageService.sendMessageToUser(username, errorResponse.toJson());
            return;
        }
        if (sendMessageDTO == null)return;
        String error = webSocketRoomService.sendMessageToRoom(sendMessageDTO, username);
        if (error != null) {
            ErrorResponse errorResponse = new ErrorResponse(error, HttpStatus.BAD_REQUEST);
            webSocketMessageService.sendMessageToUser(username, errorResponse.toJson());
        }

    }

}
