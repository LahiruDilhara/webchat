package me.lahirudilhara.webchat.websocket.services;

import me.lahirudilhara.webchat.common.types.Either;
import me.lahirudilhara.webchat.common.types.Failure;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.service.api.room.RoomQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JoinRoomValidator {

    private final RoomQueryService roomQueryService;

    public JoinRoomValidator(RoomQueryService roomQueryService) {
        this.roomQueryService = roomQueryService;
    }

    public Either<Failure,Void> validateJoinRoom(String username, Integer roomId){
        List<UserEntity> roomUsers= roomQueryService.getRoomUsers(roomId).getData();
        if(roomUsers.stream().noneMatch(u->u.getUsername().equals(username))) return Either.left(new Failure("User is not member of the room"));
        return Either.left(null);
    }
}
