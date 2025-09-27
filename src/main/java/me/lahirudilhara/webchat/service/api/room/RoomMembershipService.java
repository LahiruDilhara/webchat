package me.lahirudilhara.webchat.service.api.room;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.lahirudilhara.webchat.common.exceptions.ConflictException;
import me.lahirudilhara.webchat.common.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.common.exceptions.ValidationException;
import me.lahirudilhara.webchat.entities.user.BaseUserEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.RoomRepository;
import me.lahirudilhara.webchat.service.api.UserRoomStatusService;
import me.lahirudilhara.webchat.service.api.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomMembershipService {

    private final UserService userService;
    private final RoomRepository roomRepository;
    private final UserRoomStatusService userRoomStatusService;

    @PersistenceContext
    private EntityManager entityManager;


    public RoomMembershipService(UserService userService, RoomRepository roomRepository, UserRoomStatusService userRoomStatusService) {
        this.userService = userService;
        this.roomRepository = roomRepository;
        this.userRoomStatusService = userRoomStatusService;
    }

    @Caching(evict = {
            @CacheEvict(value = "roomUsersByRoomId",key = "#roomId"),
            @CacheEvict(value = "userRoomsByUsername",key = "#username"),
            @CacheEvict(value = "userJoinedRoomsByUsername",key = "#username"),
    })
    public void joinToRoom(String username, int roomId){
        BaseUserEntity userEntity = userService.getUserByUsername(username);
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(userEntity.getId())){
            if(room.getClosed()){
                throw new ValidationException("The room is closed");
            }
        }

        List<User> members = room.getUsers();
        if(members.stream().anyMatch(u -> u.getId().equals(userEntity.getId()))){
            throw new ConflictException("The user already exists in the room");
        }
        if(members.size() >= 2 && !room.getMultiUser()){
            throw new ValidationException("The room is not multiuser. cannot join");
        }

        members.add(entityManager.getReference(User.class, userEntity.getId()));
        room.setUsers(members);
        roomRepository.save(room);

        userRoomStatusService.addUserRoomStatus(userEntity.getId(),  room.getId());
    }

    @Caching(evict = {
            @CacheEvict(value = "roomUsersByRoomId",key = "#roomId"),
            @CacheEvict(value = "userRoomsByUsername",key = "#addingUsername"),
            @CacheEvict(value = "userJoinedRoomsByUsername",key = "#addingUsername"),
    })
    public void addUserToRoom(String addingUsername, int roomId, String ownerUsername){
        BaseUserEntity owner = userService.getUserByUsername(ownerUsername);
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(owner.getId())){
            throw new ValidationException("Only the owner can add user to the room");
        }
        if(room.getClosed()){
            throw new ValidationException("The room is closed");
        }
        BaseUserEntity user = userService.getUserByUsername(addingUsername);
        List<User> members = room.getUsers();
        if(members.contains(user)){
            throw new ConflictException("The user already exists in the room");
        }
        if(members.size() >= 2 && !room.getMultiUser()){
            throw new ValidationException("The room is not multiuser. cannot join");
        }
        User userModel = new User();
        userModel.setId(user.getId());
        members.add(userModel);
        roomRepository.save(room);

        userRoomStatusService.addUserRoomStatus(user.getId(),  room.getId());
    }

    @Caching(evict = {
            @CacheEvict(value = "roomUsersByRoomId",key = "#roomId"),
            @CacheEvict(value = "userRoomsByUsername",key = "#removingUsername"),
            @CacheEvict(value = "userJoinedRoomsByUsername",key = "#removingUsername"),
    })
    public void removeUserFromRoom(String removingUsername, int roomId,  String ownerUsername){
        BaseUserEntity owner = userService.getUserByUsername(ownerUsername);
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);

        if(!room.getMultiUser()){
            throw new ValidationException("The user cannot remove from a dual user room");
        }

        if(!room.getCreatedBy().getId().equals(owner.getId())){
            throw new ValidationException("Only the owner can remove user from the room");
        }
        BaseUserEntity user = userService.getUserByUsername(removingUsername);
        if(room.getUsers().stream().noneMatch(u->u.getId().equals(user.getId()))){
            throw new ValidationException("The user is not a member of the room");
        }
        List<User> members = room.getUsers();
        members.removeIf(u->u.getId().equals(user.getId()));
        if(members.isEmpty()){
            roomRepository.delete(room);
        }
        else{
            roomRepository.save(room);
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "roomUsersByRoomId",key = "#roomId"),
            @CacheEvict(value = "userRoomsByUsername",key = "#username"),
            @CacheEvict(value = "userJoinedRoomsByUsername",key = "#username"),
    })
    public void leaveFromRoom(int roomId,String username){
        Room room =  roomRepository.findByIdWithUsers(roomId).orElseThrow(RoomNotFoundException::new);
        BaseUserEntity user = userService.getUserByUsername(username);

        if(room.getUsers().stream().noneMatch(u->u.getId().equals(user.getId()))){
            throw new ValidationException("User is not a member of the room");
        }
        if(room.getCreatedBy().getId().equals(user.getId())){
            throw new ValidationException("The owner cannot be left the room");
        }
        List<User> users = room.getUsers();
        users.removeIf(u->u.getId().equals(user.getId()));
        roomRepository.save(room);
    }

}
