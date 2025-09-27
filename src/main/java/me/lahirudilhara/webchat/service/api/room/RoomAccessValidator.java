package me.lahirudilhara.webchat.service.api.room;

import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomAccessValidator {

    private final RoomQueryService roomQueryService;

    public RoomAccessValidator(@Lazy RoomQueryService roomQueryService) {
        this.roomQueryService = roomQueryService;
    }

    public boolean isNotUserAbleToAccessRoom(String currentAccessUser, int roomId){
        RoomEntity roomEntity = roomQueryService.getRoom(roomId);
        if(roomEntity.getCreatedBy().equals(currentAccessUser)) return false;
        List<UserEntity> roomMembers = roomQueryService.getRoomUsers(roomId).getData();
        if (roomMembers.stream().anyMatch(u -> u.getUsername().equals(currentAccessUser))) return false;
        return true;
    }
}
