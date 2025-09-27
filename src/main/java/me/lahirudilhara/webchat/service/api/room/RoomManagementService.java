package me.lahirudilhara.webchat.service.api.room;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.lahirudilhara.webchat.common.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.common.exceptions.ValidationException;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.user.BaseUserEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entityModelMappers.MessageMapper;
import me.lahirudilhara.webchat.entityModelMappers.RoomMapper;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.RoomRepository;
import me.lahirudilhara.webchat.service.api.UserRoomStatusService;
import me.lahirudilhara.webchat.service.api.UserService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomManagementService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final UserService userService;
    private final CacheManager cacheManager;
    private final UserRoomStatusService userRoomStatusService;
    private final RoomQueryService roomQueryService;

    @PersistenceContext
    private EntityManager entityManager;

    public RoomManagementService(RoomRepository roomRepository,  RoomMapper roomMapper, UserService userService,  CacheManager cacheManager, MessageMapper messageMapper, UserRoomStatusService userRoomStatusService, RoomQueryService roomQueryService) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.userService = userService;
        this.cacheManager = cacheManager;
        this.userRoomStatusService = userRoomStatusService;
        this.roomQueryService = roomQueryService;
    }

    @Caching(evict = {
            @CacheEvict(value = RoomCacheNames.USER_OWNED_ROOMS_BY_USERNAME,key = "#roomEntity.createdBy"),
            @CacheEvict(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#roomEntity.createdBy"),
    })
    public RoomEntity createMultiUserRoom(RoomEntity roomEntity){
        BaseUserEntity userEntity = userService.getUserByUsername(roomEntity.getCreatedBy());
        User userRef = entityManager.getReference(User.class, userEntity.getId());

        Room room = roomMapper.roomEntityToRoom(roomEntity);
        room.setCreatedBy(userRef);
        room.setCreatedAt(Instant.now());
        List<User> members = new ArrayList<>();
        members.add(userRef);
        room.setUsers(members);
        room.setMultiUser(true);
        String error = validateRoomData(room);
        if (error != null) {
            throw new ValidationException(error);
        }
        Room createdRoom = roomRepository.save(room);

        userRoomStatusService.addUserRoomStatus(userRef.getId(),  createdRoom.getId());
        return roomMapper.roomToRoomEntity(createdRoom);
    }

    @Caching(evict = {
            @CacheEvict(value = RoomCacheNames.USER_OWNED_ROOMS_BY_USERNAME,key = "#roomEntity.createdBy"),
            @CacheEvict(value = RoomCacheNames.USER_OWNED_ROOMS_BY_USERNAME,key = "#nextUsername"),
            @CacheEvict(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#roomEntity.createdBy"),
            @CacheEvict(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#nextUsername"),
    })
    public RoomEntity createDualUserRoom(RoomEntity roomEntity, String nextUsername){
        BaseUserEntity owner = userService.getUserByUsername(roomEntity.getCreatedBy());
        BaseUserEntity user = userService.getUserByUsername(nextUsername);

        User ownerRef = entityManager.getReference(User.class, owner.getId());
        User userRef = entityManager.getReference(User.class, user.getId());

        Room room = roomMapper.roomEntityToRoom(roomEntity);
        room.setCreatedBy(ownerRef);
        room.setCreatedAt(Instant.now());
        room.setIsPrivate(true);
        room.setMultiUser(false);
        room.setUsers(List.of(ownerRef,userRef));
        room.setClosed(true);
        String error = validateRoomData(room);
        if (error != null) {
            throw new ValidationException(error);
        }
        Room createdRoom = roomRepository.save(room);

        userRoomStatusService.addUserRoomStatus(owner.getId(),  createdRoom.getId());
        userRoomStatusService.addUserRoomStatus(user.getId(),  createdRoom.getId());

        return roomMapper.roomToRoomEntity(createdRoom);
    }


    @Caching(evict = {
            @CacheEvict(value = RoomCacheNames.USER_OWNED_ROOMS_BY_USERNAME,key = "#username"),
            @CacheEvict(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#username"),
            @CacheEvict(value = RoomCacheNames.ROOM_BY_ROOM_ID,key = "#roomId"),
            @CacheEvict(value = RoomCacheNames.ROOM_USERS_BY_ROOM_ID,key = "#roomId")
    })
    public void deleteRoom(String username, int roomId){
        BaseUserEntity userEntity = userService.getUserByUsername(username);
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(userEntity.getId())){
            throw new ValidationException("Only the owner can delete the room");
        }
        List<UserEntity> roomUsers = roomQueryService.getRoomUsers(roomId).getData();
        evictRemovedRoomUserCache(roomUsers);
        roomRepository.delete(room);
    }

    @CacheEvict(value = RoomCacheNames.ROOM_BY_ROOM_ID,key = "#roomEntity.id")
    public RoomEntity updateMultiUserRoom(RoomEntity roomEntity){
        BaseUserEntity user = userService.getUserByUsername(roomEntity.getCreatedBy());

        Room room = roomRepository.findById(roomEntity.getId()).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(user.getId())){
            throw new ValidationException("Only the owner can update the room");
        }
        roomMapper.mapRoomEntityToRoom(roomEntity,room);
        String error = validateRoomData(room);
        if(error != null){
            throw new ValidationException(error);
        }
        return roomMapper.roomToRoomEntity(roomRepository.save(room));
    }


    private String validateRoomData(Room room){
        int usersCount = room.getUsers().size();
        if(!room.getMultiUser() && usersCount > 2){
            return "Cannot create non multi user room with more than two users";
        }
        if(!room.getMultiUser() && !room.getIsPrivate()){
            return "Cannot make non multi user room public";
        }
        return null;
    }

    private void evictRemovedRoomUserCache(List<UserEntity> users){
        Cache cache = cacheManager.getCache("userRoomsByUsername");
        if(cache != null){
            for(UserEntity user : users){
                cache.evict(user.getUsername());
            }
        }
    }

}
