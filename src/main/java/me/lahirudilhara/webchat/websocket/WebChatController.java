package me.lahirudilhara.webchat.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.lahirudilhara.webchat.core.util.JsonUtil;
import me.lahirudilhara.webchat.core.util.SchemaValidator;
import me.lahirudilhara.webchat.dto.websocket.message.SendMessageDTO;
import me.lahirudilhara.webchat.service.websocket.WebSocketMessageService;
import me.lahirudilhara.webchat.service.websocket.WebSocketRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WebChatController {
    private static final Logger log = LoggerFactory.getLogger(WebChatController.class);
    private final WebSocketMessageService webSocketMessageService;
    private final WebSocketRoomService webSocketRoomService;

    public WebChatController(WebSocketMessageService webSocketMessageService, WebSocketRoomService webSocketRoomService) {
        this.webSocketMessageService = webSocketMessageService;
        this.webSocketRoomService = webSocketRoomService;
    }

    public void onMessage(String payload, String username) throws JsonProcessingException {
        // parse the message from json
        SendMessageDTO sendMessageDTO = JsonUtil.jsonToObject(payload, SendMessageDTO.class);
        SchemaValidator.validate(sendMessageDTO);

//        String error = webSocketRoomService.sendMessageToRoom(sendMessageDTO, username);
//        if (error != null) {
//            ErrorResponse errorResponse = new ErrorResponse(error, HttpStatus.BAD_REQUEST);
//            webSocketMessageService.sendMessageToUser(username, errorResponse.toJson());
//        }

    }

}
