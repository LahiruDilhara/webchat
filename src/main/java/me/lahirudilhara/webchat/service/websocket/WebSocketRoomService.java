package me.lahirudilhara.webchat.service.websocket;

import me.lahirudilhara.webchat.core.exceptions.BaseWebSocketException;
import me.lahirudilhara.webchat.dto.websocket.message.SendMessageDTO;
import me.lahirudilhara.webchat.mappers.websocket.WebSocketMessageMapper;
import me.lahirudilhara.webchat.models.Message;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.RoomRepository;
import me.lahirudilhara.webchat.service.api.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketRoomService {
    private final RoomRepository roomRepository;
    private final MessageService messageService;
    private final WebSocketMessageMapper webSocketMessageMapper;
    private final WebSocketMessageService webSocketMessageService;

    public WebSocketRoomService(RoomRepository roomRepository, MessageService messageService, WebSocketMessageMapper webSocketMessageMapper, WebSocketMessageService webSocketMessageService) {
        this.roomRepository = roomRepository;
        this.messageService = messageService;
        this.webSocketMessageMapper = webSocketMessageMapper;
        this.webSocketMessageService = webSocketMessageService;
    }

    public void sendMessageToRoom(SendMessageDTO sendMessageDTO,String senderUserName){
        Room room = roomRepository.findByIdWithUsers(sendMessageDTO.getRoomId()).orElse(null);
        if (room == null) throw new BaseWebSocketException("The room does not exist");
        List<User> members = room.getUsers();

        if(members.stream().noneMatch(u -> u.getUsername().equals(senderUserName))) throw new  BaseWebSocketException("The user is not member of the specified room");
        Message addedMessage = messageService.addMessage(webSocketMessageMapper.SendMessageDtoToMessage(sendMessageDTO),room.getId(),senderUserName);

        List<String> multiCastMembers = members.stream().map(u->u.getUsername()).toList();
        webSocketMessageService.multicastDataToOnlineUsers(multiCastMembers, webSocketMessageMapper.MessageToMessageResponseDTO(addedMessage,senderUserName));
    }
}
