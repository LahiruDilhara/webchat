package me.lahirudilhara.webchat.service.websocket;

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

    public String sendMessageToRoom(SendMessageDTO sendMessageDTO,String senderUserName){
        Room room = roomRepository.findByIdWithUsers(sendMessageDTO.getRoomId()).orElse(null);
        if (room == null) {
            return "The room not found";
        }
        List<User> members = room.getUsers();
        if(members.stream().noneMatch(u -> u.getUsername().equals(senderUserName))){
            return "The user is not member of the specified room";
        }
        Message message = webSocketMessageMapper.SendMessageDtoToMessage(sendMessageDTO);
        messageService.addMessage(message,room.getId(),senderUserName);
        List<String> multiCastMembers = members.stream().map(User::getUsername).filter(username ->!username.equals(senderUserName)).toList();
        webSocketMessageService.multicastToOnlineUsers(multiCastMembers,message.getContent());
        return null;
    }
}
