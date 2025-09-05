package me.lahirudilhara.webchat.service.websocket;

import me.lahirudilhara.webchat.common.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.common.types.Either;
import me.lahirudilhara.webchat.common.types.WebSocketErrorResponse;
import me.lahirudilhara.webchat.entities.RoomEntity;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.models.message.TextMessage;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import me.lahirudilhara.webchat.service.api.RoomService;
import me.lahirudilhara.webchat.service.api.UserService;
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
    private final UserService userService;
    private final MessageRepository messageRepository;

    public WebSocketRoomService(RoomService roomService, UserService userService, MessageRepository messageRepository) {
        this.roomService = roomService;
        this.userService = userService;
        this.messageRepository = messageRepository;
    }

    public String canUserSendMessageToRoom(Room room, String username){
        List<User> members = room.getUsers();
        if(members.stream().noneMatch(u -> u.getUsername().equals(username))) return "The user is not member of the specified room";
        if(!room.isAcceptMessages()) return "The room is not accepting messages";
        return null;
    }

    public Either<WebSocketErrorResponse, BroadcastData<TextMessage>> sendTextMessageToRoom(int roomId, String senderUsername, TextMessage message){
//        RoomEntity roomEntity = null;
//        try{
//            roomEntity = roomService.getRoom(roomId);
//        } catch (RoomNotFoundException e) {
//            return Either.left(new WebSocketErrorResponse("The specified room doesn't exists"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Either.left(new WebSocketErrorResponse("Unknown Error occurred"));
//        }
//        String error = canUserSendMessageToRoom(room,senderUsername);
//        if(error != null){
//            return Either.left(new WebSocketErrorResponse(error));
//        }
//        Instant createdTime = Instant.now();
//        message.setRoom(room);
//        message.setCreatedAt(createdTime);
////        message.setSender(userService.getUser(senderUsername));
//        message.setEditedAt(createdTime);
//
//        // Save message
//        TextMessage addedMessage = messageRepository.save(message);
//
//        // Get broadcast users
//        return Either.right(new BroadcastData<>(room.getUsers().stream().map(User::getUsername).toList(),addedMessage));
        return Either.left(new WebSocketErrorResponse("not implemented"));
    }
}
