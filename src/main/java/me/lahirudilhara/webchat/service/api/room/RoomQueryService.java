package me.lahirudilhara.webchat.service.api.room;

import me.lahirudilhara.webchat.common.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.common.types.CachableObject;
import me.lahirudilhara.webchat.entities.room.RoomDetailsEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entityModelMappers.RoomMapper;
import me.lahirudilhara.webchat.entityModelMappers.UserMapper;
import me.lahirudilhara.webchat.models.room.Room;
import me.lahirudilhara.webchat.repositories.room.RoomRepository;
import me.lahirudilhara.webchat.service.api.user.UserQueryService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomQueryService {
    private final RoomRepository roomRepository;
    private final RoomQueryService self;
    private final RoomMapper roomMapper;
    private final UserMapper  userMapper;
    private final RoomValidator roomValidator;
    private final UserQueryService userQueryService;
    private final RoomBuilder roomBuilder;

    public RoomQueryService(RoomRepository roomRepository, @Lazy RoomQueryService roomQueryService, RoomMapper roomMapper, UserMapper userMapper, RoomValidator roomValidator, UserQueryService userQueryService, RoomBuilder roomBuilder) {
        this.roomRepository = roomRepository;
        this.self = roomQueryService;
        this.roomMapper = roomMapper;
        this.userMapper = userMapper;
        this.roomValidator = roomValidator;
        this.userQueryService = userQueryService;
        this.roomBuilder = roomBuilder;
    }

    public List<UserEntity> getValidatedRoomUsers(int roomId, String username){
        if(roomValidator.isNotUserAbleToAccessRoom(username, roomId)) throw new RoomNotFoundException();
        return self.getRoomUsers(roomId).getData();
    }

    @Cacheable(value = RoomCacheNames.ROOM_USERS_BY_ROOM_ID,key = "#roomId")
    public CachableObject<List<UserEntity>> getRoomUsers(int roomId){
        Room room = roomRepository.findByIdWithUsers(roomId).orElseThrow(RoomNotFoundException::new);
        return  new CachableObject<>(room.getUsers().stream().map(userMapper::userToUserEntity).toList());
    }

    @Cacheable(value = RoomCacheNames.ROOM_BY_ROOM_ID,key = "#roomId")
    public RoomEntity getRoom(int roomId){
        Room room = roomRepository.findByIdWithUsers(roomId).orElseThrow(RoomNotFoundException::new);
        return roomMapper.roomToRoomEntity(room);
    }

    @Cacheable(value = RoomCacheNames.USER_OWNED_ROOMS_BY_USERNAME,key = "#username")
    public CachableObject<List<RoomEntity>> getOwnerRooms(String username){
        List<Room> rooms = roomRepository.findByCreatedByUsername(username);
        return new CachableObject<>(rooms.stream().map(roomMapper::roomToRoomEntity).toList());
    }

    @Cacheable(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#username")
    public CachableObject<List<RoomDetailsEntity>> getUserJoinedRooms(String username){
        List<Room> rooms = roomRepository.findUserJoinedRooms(username);
        UserEntity user = userQueryService.getUserByUsername(username);
        return new CachableObject<>(rooms.stream().map(r->roomBuilder.buildRoomDetailsEntity(r,user.getId())).toList());
    }

    public List<UserEntity> getRoomMembers(int roomId){
        Room room = roomRepository.findByIdWithUsers(roomId).orElseThrow(RoomNotFoundException::new);
        return room.getUsers().stream().map(userMapper::userToUserEntity).toList();
    }
}
