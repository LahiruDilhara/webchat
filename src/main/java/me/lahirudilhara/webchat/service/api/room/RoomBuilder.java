package me.lahirudilhara.webchat.service.api.room;

import me.lahirudilhara.webchat.entities.room.DualUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.MultiUserRoomEntity;
import me.lahirudilhara.webchat.entityModelMappers.RoomMapper;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.models.room.DualUserRoom;
import me.lahirudilhara.webchat.models.room.MultiUserRoom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomBuilder {
    private final RoomMapper roomMapper;

    public RoomBuilder(RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
    }

    public MultiUserRoomEntity MultiUserRoomEntityFromMultiUserRoom (MultiUserRoom room){
        int userCount = room.getUsers().size();
        return roomMapper.multiUserRoomToMultiUserRoomEntity(room,userCount);
    }

    public DualUserRoomEntity  DualUserRoomEntityFromDualUserRoom (DualUserRoom room){
        List<User> users = room.getUsers();
        if(users.size() != 2) throw new  IllegalArgumentException("Wrong number of users");
        return roomMapper.dualUserRoomToDualUserRoomEntity(room, users.get(0).getUsername(),users.get(1).getUsername());
    }
}
