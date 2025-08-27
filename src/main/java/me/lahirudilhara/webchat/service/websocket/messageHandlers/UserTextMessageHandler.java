package me.lahirudilhara.webchat.service.websocket.messageHandlers;

import me.lahirudilhara.webchat.core.exceptions.BaseWebSocketException;
import me.lahirudilhara.webchat.dto.websocket.user.UserTextMessageDto;
import me.lahirudilhara.webchat.mappers.websocket.WebSocketMessageMapper;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.message.TextMessage;
import me.lahirudilhara.webchat.service.MessageService;
import me.lahirudilhara.webchat.service.api.RoomService;
import me.lahirudilhara.webchat.service.api.UserService;
import me.lahirudilhara.webchat.websocket.UserMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserTextMessageHandler implements UserMessageHandler<UserTextMessageDto> {
    private final RoomService roomService;
    private final UserService userService;
    private final WebSocketMessageMapper webSocketMessageMapper;
    private final MessageService messageService;

    private static final Logger log = LoggerFactory.getLogger(UserTextMessageHandler.class);

    public UserTextMessageHandler(RoomService roomService,WebSocketMessageMapper webSocketMessageMapper, UserService userService, MessageService messageService) {
        this.roomService = roomService;
        this.userService = userService;
        this.webSocketMessageMapper = webSocketMessageMapper;
        this.messageService = messageService;
    }

    @Override
    public Class<UserTextMessageDto> getMessageType() {
        return UserTextMessageDto.class;
    }

    @Override
    public void handleMessage(UserTextMessageDto message, String senderUsername) {
        Room room = roomService.getRoom(message.getRoomId());
        if(room.getUsers().stream().noneMatch(u -> u.getUsername().equals(senderUsername))) {
            throw new BaseWebSocketException("User cannot have access to the room");
        }
        if(!room.isAcceptMessages()) throw new BaseWebSocketException("The room is not accepting messages");

        TextMessage textMessage = webSocketMessageMapper.UserTextMessageDtoToTextMessage(message);
        textMessage.setCreatedAt(Instant.now());
        textMessage.setEditedAt(Instant.now());
        textMessage.setRoom(room);
        textMessage.setSender(userService.getUser(senderUsername));

        // base message should have a relation to rooms

        System.out.println("UserTextMessageHandler.handleMessage");
        log.debug(message.toString());
    }
}
