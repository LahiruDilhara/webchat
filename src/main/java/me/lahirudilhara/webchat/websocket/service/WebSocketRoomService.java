package me.lahirudilhara.webchat.websocket.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.lahirudilhara.webchat.common.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.common.types.Either;
import me.lahirudilhara.webchat.common.types.WebSocketErrorResponse;
import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.message.TextMessage;
import me.lahirudilhara.webchat.service.MessageService;
import me.lahirudilhara.webchat.service.api.RoomService;
import me.lahirudilhara.webchat.websocket.entities.BroadcastData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class WebSocketRoomService {
    private static final Logger log = LoggerFactory.getLogger(WebSocketRoomService.class);
    private final RoomService roomService;
    private final MessageService messageService;

    @PersistenceContext
    private EntityManager entityManager;

    public WebSocketRoomService(RoomService roomService, MessageService messageService) {
        this.roomService = roomService;
        this.messageService = messageService;
    }

    private String canUserSendMessageToRoom(List<UserEntity> roomMemebers, String senderUsername){
        if(roomMemebers.stream().noneMatch(u->u.getUsername().equals(senderUsername))) return "User is not a member of the room";
        return null;
    }

    private Either<WebSocketErrorResponse,List<UserEntity>> getRoomUsers(int roomId){
        try{
            return Either.right(roomService.getRoomUsers(roomId).getData());
        }
        catch (RoomNotFoundException e){
            return Either.left(new WebSocketErrorResponse("Room not found"));
        }
        catch (Exception e){
            return Either.left(new WebSocketErrorResponse("Unknown error occurred"));
        }
    }

    public Either<WebSocketErrorResponse, BroadcastData<TextMessage>> publishMessageToRoom(int roomId, String senderUsername,TextMessage textMessage){
        var dataOrError = getRoomUsers(roomId);
        if(dataOrError.isLeft()) return Either.left(dataOrError.getLeft());
        List<UserEntity> roomUsers = dataOrError.getRight();

        String error = canUserSendMessageToRoom(roomUsers,senderUsername);
        if(error != null) return Either.left(new WebSocketErrorResponse(error));

        // populate the message data
        Instant createdTime = Instant.now();
        Room roomRef = entityManager.getReference(Room.class, roomId);
        textMessage.setRoom(roomRef);
        textMessage.setCreatedAt(createdTime);
        textMessage.setEditedAt(createdTime);

        TextMessage addedMessage = messageService.addMessage(textMessage);
        List<String> receivers = roomUsers.stream().map(UserEntity::getUsername).toList();
        return Either.right(new BroadcastData<>(receivers,addedMessage));
    }
}
