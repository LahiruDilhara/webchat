package me.lahirudilhara.webchat.service.websocket;

import me.lahirudilhara.webchat.common.exceptions.BaseWebSocketException;
import me.lahirudilhara.webchat.common.types.Either;
import me.lahirudilhara.webchat.common.types.ErrorResponse;
import me.lahirudilhara.webchat.mappers.websocket.WebSocketMessageMapper;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.RoomRepository;
import me.lahirudilhara.webchat.websocket.entities.BroadcastData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketRoomService {
    private final RoomRepository roomRepository;
    private final WebSocketMessageServiceOld webSocketMessageService;
    private final WebSocketMessageMapper webSocketMessageMapper;
    private final WebSocketMessageHandler webSocketMessageHandler;

    public WebSocketRoomService(RoomRepository roomRepository, WebSocketMessageServiceOld webSocketMessageService, WebSocketMessageMapper webSocketMessageMapper, WebSocketMessageHandler webSocketMessageHandler) {
        this.roomRepository = roomRepository;
        this.webSocketMessageService = webSocketMessageService;
        this.webSocketMessageMapper = webSocketMessageMapper;
        this.webSocketMessageHandler = webSocketMessageHandler;
    }

    public void canUserSendMessageToRoom(int roomId, String username){
        Room room = roomRepository.findByIdWithUsers(roomId).orElse(null);
        if (room == null) throw new BaseWebSocketException("The room does not exist");
        List<User> members = room.getUsers();
        if(members.stream().noneMatch(u -> u.getUsername().equals(username))) throw new  BaseWebSocketException("The user is not member of the specified room");
        if(!room.isAcceptMessages()) throw new BaseWebSocketException("The room is not accepting messages");
    }

//    public Either<BroadcastData, ErrorResponse> sendMessageToRoom(int roomId, String senderUsername){
//
//    }

}
