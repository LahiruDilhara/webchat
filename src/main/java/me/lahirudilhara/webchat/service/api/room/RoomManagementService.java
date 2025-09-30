package me.lahirudilhara.webchat.service.api.room;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.lahirudilhara.webchat.common.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.common.exceptions.ValidationException;
import me.lahirudilhara.webchat.entities.room.DualUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.MultiUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entityModelMappers.RoomMapper;
import me.lahirudilhara.webchat.models.room.DualUserRoom;
import me.lahirudilhara.webchat.models.room.MultiUserRoom;
import me.lahirudilhara.webchat.models.room.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.room.MultiUserRoomRepository;
import me.lahirudilhara.webchat.repositories.room.RoomRepository;
import me.lahirudilhara.webchat.service.api.user.UserQueryService;
import me.lahirudilhara.webchat.service.api.user.UserRoomStatusService;
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
    private final CacheManager cacheManager;
    private final UserRoomStatusService userRoomStatusService;
    private final RoomQueryService roomQueryService;
    private final UserQueryService userQueryService;
    private final RoomBuilder roomBuilder;
    private final MultiUserRoomRepository multiUserRoomRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public RoomManagementService(RoomRepository roomRepository, RoomMapper roomMapper, CacheManager cacheManager, UserRoomStatusService userRoomStatusService, RoomQueryService roomQueryService, UserQueryService userQueryService, RoomBuilder roomBuilder, MultiUserRoomRepository multiUserRoomRepository) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.cacheManager = cacheManager;
        this.userRoomStatusService = userRoomStatusService;
        this.roomQueryService = roomQueryService;
        this.userQueryService = userQueryService;
        this.roomBuilder = roomBuilder;
        this.multiUserRoomRepository = multiUserRoomRepository;
    }

    @Caching(evict = {
            @CacheEvict(value = RoomCacheNames.USER_OWNED_ROOMS_BY_USERNAME,key = "#roomEntity.createdBy"),
            @CacheEvict(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#roomEntity.createdBy"),
    })
    public RoomEntity createMultiUserRoom(MultiUserRoomEntity roomEntity) {
        UserEntity userEntity = userQueryService.getUserByUsername(roomEntity.getCreatedBy());
        User userRef = entityManager.getReference(User.class, userEntity.getId());

        MultiUserRoom room = roomMapper.multiUserRoomEntityToMultiUserRoom(roomEntity);
        room.setCreatedBy(userRef);
        room.setCreatedAt(Instant.now());
        List<User> members = new ArrayList<>();
        members.add(userRef);
        room.setUsers(members);
        MultiUserRoom createdRoom = multiUserRoomRepository.save(room);

        userRoomStatusService.addUserRoomStatus(userRef.getId(),  createdRoom.getId());
        return roomBuilder.MultiUserRoomEntityFromMultiUserRoom(createdRoom,userEntity.getId());
    }

    @Caching(evict = {
            @CacheEvict(value = RoomCacheNames.USER_OWNED_ROOMS_BY_USERNAME,key = "#roomEntity.createdBy"),
            @CacheEvict(value = RoomCacheNames.USER_OWNED_ROOMS_BY_USERNAME,key = "#nextUsername"),
            @CacheEvict(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#roomEntity.createdBy"),
            @CacheEvict(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#nextUsername"),
    })
    public DualUserRoomEntity createDualUserRoom(DualUserRoomEntity roomEntity, String nextUsername){
        UserEntity owner = userQueryService.getUserByUsername(roomEntity.getCreatedBy());
        UserEntity user = userQueryService.getUserByUsername(nextUsername);

        User ownerRef = entityManager.getReference(User.class, owner.getId());
        User userRef = entityManager.getReference(User.class, user.getId());

        DualUserRoom room = roomMapper.dualUserRoomEntityToDualUserRoom(roomEntity);
        room.setCreatedBy(ownerRef);
        room.setCreatedAt(Instant.now());
        room.setUsers(List.of(ownerRef,userRef));
        Room createdRoom = roomRepository.save(room);

        userRoomStatusService.addUserRoomStatus(owner.getId(),  createdRoom.getId());
        userRoomStatusService.addUserRoomStatus(user.getId(),  createdRoom.getId());


        return roomBuilder.DualUserRoomEntityFromDualUserRoom(room,owner.getId());
    }


    @Caching(evict = {
            @CacheEvict(value = RoomCacheNames.USER_OWNED_ROOMS_BY_USERNAME,key = "#username"),
            @CacheEvict(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#username"),
            @CacheEvict(value = RoomCacheNames.ROOM_BY_ROOM_ID,key = "#roomId"),
            @CacheEvict(value = RoomCacheNames.ROOM_USERS_BY_ROOM_ID,key = "#roomId")
    })
    public void deleteRoom(String username, int roomId){
        UserEntity userEntity = userQueryService.getUserByUsername(username);
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(userEntity.getId())){
            throw new ValidationException("Only the owner can delete the room");
        }
        List<UserEntity> roomUsers = roomQueryService.getRoomUsers(roomId).getData();
        evictRemovedRoomUserCache(roomUsers);
        roomRepository.delete(room);
    }

    @CacheEvict(value = RoomCacheNames.ROOM_BY_ROOM_ID,key = "#roomEntity.id")
    public MultiUserRoomEntity updateMultiUserRoom(MultiUserRoomEntity roomEntity){
        UserEntity user = userQueryService.getUserByUsername(roomEntity.getCreatedBy());

        MultiUserRoom room = multiUserRoomRepository.findById(roomEntity.getId()).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(user.getId())){
            throw new ValidationException("Only the owner can update the room");
        }
        roomMapper.mapMultiUserRoomEntityToMultiUserRoom(roomEntity,room);
        MultiUserRoom updatedRoom = roomRepository.save(room);
        return roomBuilder.MultiUserRoomEntityFromMultiUserRoom(updatedRoom,user.getId());
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
