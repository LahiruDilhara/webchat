package me.lahirudilhara.webchat.service.api.room;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.lahirudilhara.webchat.common.exceptions.ConflictException;
import me.lahirudilhara.webchat.common.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.common.exceptions.ValidationException;
import me.lahirudilhara.webchat.entities.room.MultiUserRoomEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.models.room.DualUserRoom;
import me.lahirudilhara.webchat.models.room.MultiUserRoom;
import me.lahirudilhara.webchat.models.room.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.room.MultiUserRoomRepository;
import me.lahirudilhara.webchat.repositories.room.RoomRepository;
import me.lahirudilhara.webchat.service.api.user.UserQueryService;
import me.lahirudilhara.webchat.service.api.user.UserRoomStatusService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomMembershipService {

    private final RoomRepository roomRepository;
    private final MultiUserRoomRepository multiUserRoomRepository;
    private final UserRoomStatusService userRoomStatusService;
    private final UserQueryService userQueryService;

    @PersistenceContext
    private EntityManager entityManager;


    public RoomMembershipService( RoomRepository roomRepository, UserRoomStatusService userRoomStatusService, UserQueryService userQueryService, MultiUserRoomRepository multiUserRoomRepository) {
        this.roomRepository = roomRepository;
        this.userRoomStatusService = userRoomStatusService;
        this.userQueryService = userQueryService;
        this.multiUserRoomRepository = multiUserRoomRepository;
    }

    @Caching(evict = {
            @CacheEvict(value = RoomCacheNames.ROOM_USERS_BY_ROOM_ID,key = "#roomId"),
            @CacheEvict(value = RoomCacheNames.USER_OWNED_ROOMS_BY_USERNAME,key = "#username"),
            @CacheEvict(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#username"),
    })
    public void joinToRoom(String username, int roomId){
        UserEntity userEntity = userQueryService.getUserByUsername(username);
        MultiUserRoom multiUserRoom = multiUserRoomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        if(!multiUserRoom.getCreatedBy().getId().equals(userEntity.getId())){
            if(multiUserRoom.getClosed()){
                throw new ValidationException("The room is closed");
            }
        }

        List<User> members = multiUserRoom.getUsers();
        if(members.stream().anyMatch(u -> u.getId().equals(userEntity.getId()))){
            throw new ConflictException("The user already exists in the room");
        }

        members.add(entityManager.getReference(User.class, userEntity.getId()));
        multiUserRoom.setUsers(members);
        multiUserRoomRepository.save(multiUserRoom);

        userRoomStatusService.addUserRoomStatus(userEntity.getId(),  multiUserRoom.getId());
    }

    @Caching(evict = {
            @CacheEvict(value = RoomCacheNames.ROOM_USERS_BY_ROOM_ID,key = "#roomId"),
            @CacheEvict(value = RoomCacheNames.USER_OWNED_ROOMS_BY_USERNAME,key = "#addingUsername"),
            @CacheEvict(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#addingUsername"),
    })
    public void addUserToRoom(String addingUsername, int roomId, String ownerUsername){
        UserEntity owner = userQueryService.getUserByUsername(ownerUsername);
        MultiUserRoom room = multiUserRoomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(owner.getId())){
            throw new ValidationException("Only the owner can add user to the room");
        }
        UserEntity user = userQueryService.getUserByUsername(addingUsername);
        List<User> members = room.getUsers();
        if(members.contains(user)){
            throw new ConflictException("The user already exists in the room");
        }
        User userModel = new User();
        userModel.setId(user.getId());
        members.add(userModel);
        multiUserRoomRepository.save(room);

        userRoomStatusService.addUserRoomStatus(user.getId(),  room.getId());
    }

    @Caching(evict = {
            @CacheEvict(value = RoomCacheNames.ROOM_USERS_BY_ROOM_ID,key = "#roomId"),
            @CacheEvict(value = RoomCacheNames.USER_OWNED_ROOMS_BY_USERNAME,key = "#removingUsername"),
            @CacheEvict(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#removingUsername"),
    })
    public void removeUserFromRoom(String removingUsername, int roomId,  String ownerUsername){
        UserEntity owner = userQueryService.getUserByUsername(ownerUsername);
        MultiUserRoom room = multiUserRoomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);

        if(!room.getCreatedBy().getId().equals(owner.getId())){
            throw new ValidationException("Only the owner can remove user from the room");
        }
        UserEntity user = userQueryService.getUserByUsername(removingUsername);
        if(room.getUsers().stream().noneMatch(u->u.getId().equals(user.getId()))){
            throw new ValidationException("The user is not a member of the room");
        }
        List<User> members = room.getUsers();
        members.removeIf(u->u.getId().equals(user.getId()));
        if(members.isEmpty()){
            multiUserRoomRepository.delete(room);
        }
        else{
            multiUserRoomRepository.save(room);
        }
    }

    @Caching(evict = {
            @CacheEvict(value = RoomCacheNames.ROOM_USERS_BY_ROOM_ID,key = "#roomId"),
            @CacheEvict(value = RoomCacheNames.USER_OWNED_ROOMS_BY_USERNAME,key = "#username"),
            @CacheEvict(value = RoomCacheNames.USER_JOINED_ROOMS_BY_USERNAME,key = "#username"),
    })
    public void leaveFromRoom(int roomId,String username){
        MultiUserRoom room =  multiUserRoomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        UserEntity user = userQueryService.getUserByUsername(username);

        if(room.getUsers().stream().noneMatch(u->u.getId().equals(user.getId()))){
            throw new ValidationException("User is not a member of the room");
        }
        if(room.getCreatedBy().getId().equals(user.getId())){
            throw new ValidationException("The owner cannot be left the room");
        }
        List<User> users = room.getUsers();
        users.removeIf(u->u.getId().equals(user.getId()));
        multiUserRoomRepository.save(room);
    }

}
