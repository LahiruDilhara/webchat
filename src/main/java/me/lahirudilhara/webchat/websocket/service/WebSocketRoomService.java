package me.lahirudilhara.webchat.websocket.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.lahirudilhara.webchat.common.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.common.exceptions.UserNotFoundException;
import me.lahirudilhara.webchat.common.types.Either;
import me.lahirudilhara.webchat.common.types.Failure;
import me.lahirudilhara.webchat.entities.user.BaseUserEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.models.message.TextMessage;
import me.lahirudilhara.webchat.service.message.MessageService;
import me.lahirudilhara.webchat.service.api.room.RoomManagementService;
import me.lahirudilhara.webchat.service.api.UserService;
import me.lahirudilhara.webchat.service.api.room.RoomQueryService;
import me.lahirudilhara.webchat.websocket.entities.BroadcastData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class WebSocketRoomService {
    private static final Logger log = LoggerFactory.getLogger(WebSocketRoomService.class);
    private final RoomManagementService roomManagementService;
    private final MessageService messageService;
    private final UserService userService;
    private final RoomQueryService roomQueryService;

    @PersistenceContext
    private EntityManager entityManager;

    public WebSocketRoomService(RoomManagementService roomManagementService, MessageService messageService, UserService userService, RoomQueryService roomQueryService) {
        this.roomManagementService = roomManagementService;
        this.messageService = messageService;
        this.userService = userService;
        this.roomQueryService = roomQueryService;
    }

    private String canUserSendMessageToRoom(List<UserEntity> roomMembers, String senderUsername){
        if(roomMembers.stream().noneMatch(u->u.getUsername().equals(senderUsername))) return "User is not a member of the room";
        return null;
    }

    private Either<Failure,List<UserEntity>> getRoomUsers(int roomId){
        try{
            return Either.right(roomQueryService.getRoomUsers(roomId).getData());
        }
        catch (RoomNotFoundException e){
            return Either.left(new Failure("Room not found"));
        }
        catch (Exception e){
            return Either.left(new Failure("Unknown error occurred"));
        }
    }

    private Either<Failure, BaseUserEntity> getUserByUsername(String username){
        try{
            return Either.right(userService.getUserByUsername(username));
        } catch (UserNotFoundException e) {
            return Either.left(new Failure("The user not found"));
        }
    }

    public Either<Failure, BroadcastData<MessageEntity>> publishMessageToRoom(int roomId, String senderUsername, TextMessage textMessage){
        var dataOrError = getRoomUsers(roomId);
        if(dataOrError.isLeft()) return Either.left(dataOrError.getLeft());
        List<UserEntity> roomUsers = dataOrError.getRight();

        String error = canUserSendMessageToRoom(roomUsers,senderUsername);
        if(error != null) return Either.left(new Failure(error));

        var userOrError = getUserByUsername(senderUsername);
        if(userOrError.isLeft()) return Either.left(userOrError.getLeft());
        BaseUserEntity user = userOrError.getRight();

        // populate the message data
        Instant createdTime = Instant.now();
        Room roomRef = entityManager.getReference(Room.class, roomId);
        User userRef = entityManager.getReference(User.class,user.getId());
        textMessage.setSender(userRef);
        textMessage.setDeleted(false);
        textMessage.setRoom(roomRef);
        textMessage.setCreatedAt(createdTime);
        textMessage.setEditedAt(createdTime);

        MessageEntity addedMessage = messageService.addMessageAsync(textMessage);

        List<String> receivers = roomUsers.stream().map(UserEntity::getUsername).toList();
        return Either.right(new BroadcastData<>(receivers,addedMessage));
    }
}
