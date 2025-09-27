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

    public RoomQueryService(RoomRepository roomRepository, UserRepository userRepository, @Lazy RoomQueryService roomQueryService, MessageRepository messageRepository, MessageMapper messageMapper, RoomMapper roomMapper, UserMapper userMapper) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.self = roomQueryService;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.roomMapper = roomMapper;
        this.userMapper = userMapper;
    }

    public List<MessageEntity> getRoomMessages(int roomId, String accessUser, Pageable pageable){
        if(validateRoomDataAccess(accessUser, roomId)) throw new RoomNotFoundException();
        Page<Message> page = messageRepository.findByRoomIdOrderByCreatedAtDesc(roomId, pageable);

        // Remove deleted messages and convert
        return page.getContent().stream().filter(m->!m.getDeleted()).map(messageMapper::messageToMessageEntity).toList();
    }


    public List<UserEntity> getRoomUsers(int roomId, String username){
        if(validateRoomDataAccess(username, roomId)) throw new RoomNotFoundException();
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
    public CachableObject<List<RoomEntity>> getUserRooms(String username){
        List<Room> rooms = roomRepository.findByCreatedByUsername(username);
        return new CachableObject<>(rooms.stream().map(roomMapper::roomToRoomEntity).toList());
    }

    @Cacheable(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#username")
    public CachableObject<List<RoomEntity>> getUserJoinedRooms(String username){
        List<Room> rooms = roomRepository.findUserJoinedRooms(username);
        return new CachableObject<>(rooms.stream().map(roomMapper::roomToRoomEntity).toList());
    }


    private boolean validateRoomDataAccess(String currentAccessUser, int roomId){
        RoomEntity roomEntity = self.getRoom(roomId);
        if(roomEntity.getCreatedBy().equals(currentAccessUser)) return false;
        List<UserEntity> roomMembers = self.getRoomUsers(roomId).getData();
        if(roomMembers.stream().anyMatch(u->u.getUsername().equals(currentAccessUser))) return false;
        return true;
    }
}
