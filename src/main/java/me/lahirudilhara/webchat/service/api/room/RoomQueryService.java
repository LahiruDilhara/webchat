package me.lahirudilhara.webchat.service.api.room;

import me.lahirudilhara.webchat.common.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.common.types.CachableObject;
import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entityModelMappers.MessageMapper;
import me.lahirudilhara.webchat.entityModelMappers.RoomMapper;
import me.lahirudilhara.webchat.entityModelMappers.UserMapper;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import me.lahirudilhara.webchat.repositories.RoomRepository;
import me.lahirudilhara.webchat.repositories.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomQueryService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomQueryService self;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final RoomMapper roomMapper;
    private final UserMapper  userMapper;
    private final RoomAccessValidator roomAccessValidator;

    public RoomQueryService(RoomRepository roomRepository, UserRepository userRepository, @Lazy RoomQueryService roomQueryService, MessageRepository messageRepository, MessageMapper messageMapper, RoomMapper roomMapper, UserMapper userMapper, RoomAccessValidator roomAccessValidator) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.self = roomQueryService;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.roomMapper = roomMapper;
        this.userMapper = userMapper;
        this.roomAccessValidator = roomAccessValidator;
    }

    public List<UserEntity> getValidatedRoomUsers(int roomId, String username){
        if(roomAccessValidator.isNotUserAbleToAccessRoom(username, roomId)) throw new RoomNotFoundException();
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
    public CachableObject<List<RoomEntity>> getUserJoinedRooms(String username){
        List<Room> rooms = roomRepository.findUserJoinedRooms(username);
        return new CachableObject<>(rooms.stream().map(roomMapper::roomToRoomEntity).toList());
    }
}
