package me.lahirudilhara.webchat.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.lahirudilhara.webchat.core.util.JsonUtil;
import me.lahirudilhara.webchat.core.util.SchemaValidator;
import me.lahirudilhara.webchat.dto.wc.WebSocketMessageDTO;
import me.lahirudilhara.webchat.service.websocket.WebSocketMessageDispatcher;
import me.lahirudilhara.webchat.service.websocket.WebSocketRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WebChatController {
    private static final Logger log = LoggerFactory.getLogger(WebChatController.class);
    private final WebSocketRoomService webSocketRoomService;
    private final WebSocketMessageDispatcher webSocketMessageDispatcher;

    public WebChatController( WebSocketRoomService webSocketRoomService, WebSocketMessageDispatcher webSocketMessageDispatcher ) {
        this.webSocketRoomService = webSocketRoomService;
        this.webSocketMessageDispatcher = webSocketMessageDispatcher;
    }

    public String onMessage(String payload, String username) throws JsonProcessingException {
        // parse the message from json
        WebSocketMessageDTO webSocketMessageDTO = null;
        try{
            webSocketMessageDTO = JsonUtil.jsonToObject(payload, WebSocketMessageDTO.class);
        }
        catch(JsonProcessingException e){
            return "The message json is not in correct format";
        } catch (Exception e) {
            return "Unknown error occurred";
        }
        if(webSocketMessageDTO == null) return "Unknown error occurred";
        String error = SchemaValidator.validate(webSocketMessageDTO);
        if(error != null)return error;

        return null;
//        webSocketMessageDispatcher.dispatch(webSocketMessageDto,username);
//        webSocketRoomService.sendMessageToRoom(sendMessageDTO, username);
    }

}
