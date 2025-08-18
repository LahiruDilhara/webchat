package me.lahirudilhara.webchat.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.lahirudilhara.webchat.core.util.JsonUtil;
import me.lahirudilhara.webchat.core.util.SchemaValidator;
import me.lahirudilhara.webchat.dto.websocket.message.SendMessageDTO;
import me.lahirudilhara.webchat.mappers.websocket.WebSocketMessageMapper;
import me.lahirudilhara.webchat.models.Message;
import me.lahirudilhara.webchat.service.websocket.WebSocketMessageService;
import me.lahirudilhara.webchat.service.websocket.WebSocketRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WebChatController {
    private static final Logger log = LoggerFactory.getLogger(WebChatController.class);
    private final WebSocketRoomService webSocketRoomService;

    public WebChatController( WebSocketRoomService webSocketRoomService) {
        this.webSocketRoomService = webSocketRoomService;
    }

    public void onMessage(String payload, String username) throws JsonProcessingException {
        // parse the message from json
        SendMessageDTO sendMessageDTO = JsonUtil.jsonToObject(payload, SendMessageDTO.class);
        SchemaValidator.validate(sendMessageDTO);

        webSocketRoomService.sendMessageToRoom(sendMessageDTO, username);
    }

}
