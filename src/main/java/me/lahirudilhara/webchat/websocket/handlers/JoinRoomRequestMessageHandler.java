package me.lahirudilhara.webchat.websocket.handlers;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.common.util.JsonUtil;
import me.lahirudilhara.webchat.dto.wc.JoinRoomMessageDTO;
import me.lahirudilhara.webchat.websocket.events.NewClientJoinedEvent;
import me.lahirudilhara.webchat.websocket.service.WebSocketRoomService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
public class JoinRoomRequestMessageHandler implements MessageHandler<JoinRoomMessageDTO> {
    private final WebSocketRoomService webSocketRoomService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public JoinRoomRequestMessageHandler(WebSocketRoomService webSocketRoomService,ApplicationEventPublisher applicationEventPublisher) {
        this.webSocketRoomService = webSocketRoomService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Class<JoinRoomMessageDTO> getMessageType() {
        return JoinRoomMessageDTO.class;
    }

    @Override
    public void handleMessage(JoinRoomMessageDTO joinRoomMessageDTO, String senderUsername, WebSocketSession session) {
        var dataOrError = webSocketRoomService.canUserJoinToRoom(joinRoomMessageDTO.getRoomId(),senderUsername);
        if(dataOrError.isLeft()){
            if(!session.isOpen()) return;
            try{
                session.sendMessage(new TextMessage(JsonUtil.objectToJson(dataOrError.getLeft())));
                return;
            }
            catch(Exception e){
                log.error(e.getMessage(),e);
            }
        }
        applicationEventPublisher.publishEvent(new NewClientJoinedEvent(senderUsername,session,joinRoomMessageDTO.getRoomId()));
    }
}
