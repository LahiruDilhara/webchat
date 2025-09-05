package me.lahirudilhara.webchat.websocket.handlers;

import me.lahirudilhara.webchat.dto.wc.TextMessageDTO;
import me.lahirudilhara.webchat.dtoEntityMappers.api.MessageMapper;
import me.lahirudilhara.webchat.dtoEntityMappers.websocket.WebSocketMessageMapper;
import me.lahirudilhara.webchat.models.message.TextMessage;
import me.lahirudilhara.webchat.service.websocket.WebSocketRoomService;
import me.lahirudilhara.webchat.websocket.entities.BroadcastData;
import me.lahirudilhara.webchat.websocket.events.ClientExceptionEvent;
import me.lahirudilhara.webchat.websocket.events.MulticastDataEvent;
import me.lahirudilhara.webchat.websocket.events.UnicastDataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTextMessageHandler implements MessageHandler<TextMessageDTO> {
    private final WebSocketRoomService webSocketRoomService;
    private final WebSocketMessageMapper webSocketMessageMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MessageMapper messageMapper;

    private static final Logger log = LoggerFactory.getLogger(UserTextMessageHandler.class);

    public UserTextMessageHandler(WebSocketRoomService webSocketRoomService, WebSocketMessageMapper webSocketMessageMapper,ApplicationEventPublisher applicationEventPublisher,MessageMapper messageMapper) {
        this.webSocketRoomService = webSocketRoomService;
        this.webSocketMessageMapper = webSocketMessageMapper;
        this.applicationEventPublisher = applicationEventPublisher;
        this.messageMapper= messageMapper;
    }

    @Override
    public Class<TextMessageDTO> getMessageType() {
        return TextMessageDTO.class;
    }

    @Override
    public void handleMessage(TextMessageDTO message, String senderUsername) {
        var dataOrError = webSocketRoomService.sendTextMessageToRoom(message.getRoomId(),senderUsername,webSocketMessageMapper.textMessageDtoToTextMessage(message));
        if(dataOrError.isLeft()){
            System.out.println(dataOrError.getLeft().getError());
            applicationEventPublisher.publishEvent(new ClientExceptionEvent(senderUsername,dataOrError.getLeft().getError()));
            return;
        }
        BroadcastData<TextMessage> data = dataOrError.getRight();
        List<String> multicastUsernames = data.getUsers().stream().filter(u->!u.equals(senderUsername)).toList();
        applicationEventPublisher.publishEvent(new MulticastDataEvent(multicastUsernames,messageMapper.messageToMessageResponse(data.getData())));
        applicationEventPublisher.publishEvent(new UnicastDataEvent(senderUsername,webSocketMessageMapper.textMessageToTextMessageAckResponseDTO(data.getData(),message.getUuid())));
    }
}
