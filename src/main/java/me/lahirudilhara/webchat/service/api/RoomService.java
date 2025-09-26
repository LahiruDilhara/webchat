package me.lahirudilhara.webchat.service.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.lahirudilhara.webchat.common.exceptions.ConflictException;
import me.lahirudilhara.webchat.common.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.common.exceptions.ValidationException;
import me.lahirudilhara.webchat.common.types.CachableObject;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.entityModelMappers.MessageMapper;
import me.lahirudilhara.webchat.entityModelMappers.RoomMapper;
import me.lahirudilhara.webchat.entityModelMappers.UserMapper;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import me.lahirudilhara.webchat.repositories.RoomRepository;
import me.lahirudilhara.webchat.repositories.UserRoomStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final RoomMapper roomMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final CacheManager cacheManager;
    private final MessageMapper messageMapper;
    private final UserRoomStatusService userRoomStatusService;

    @PersistenceContext
    private EntityManager entityManager;

    private RoomService self;

    public RoomService(RoomRepository roomRepository, MessageRepository messageRepository, RoomMapper roomMapper, UserService userService, UserMapper userMapper, CacheManager cacheManager, MessageMapper messageMapper,  UserRoomStatusService userRoomStatusService,@Lazy RoomService self) {
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.roomMapper = roomMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.cacheManager = cacheManager;
        this.messageMapper = messageMapper;
        this.userRoomStatusService = userRoomStatusService;
        this.self = self;
    }

    @Caching(evict = {
            @CacheEvict(value = "userRoomsByUsername",key = "#roomEntity.createdBy"),
            @CacheEvict(value = "userJoinedRoomsByUsername",key = "#roomEntity.createdBy"),
    })
    public RoomEntity createMultiUserRoom(RoomEntity roomEntity){
        UserEntity userEntity = userService.getUserByUsername(roomEntity.getCreatedBy());
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
            @CacheEvict(value = "userRoomsByUsername",key = "#roomEntity.createdBy"),
            @CacheEvict(value = "userRoomsByUsername",key = "#nextUsername"),
            @CacheEvict(value = "userJoinedRoomsByUsername",key = "#roomEntity.createdBy"),
            @CacheEvict(value = "userJoinedRoomsByUsername",key = "#nextUsername"),
    })
    public RoomEntity createDualUserRoom(RoomEntity roomEntity, String nextUsername){
        UserEntity owner = userService.getUserByUsername(roomEntity.getCreatedBy());
        UserEntity user = userService.getUserByUsername(nextUsername);

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
            @CacheEvict(value = "roomUsersByRoomId",key = "#roomId"),
            @CacheEvict(value = "userRoomsByUsername",key = "#username"),
            @CacheEvict(value = "userJoinedRoomsByUsername",key = "#username"),
    })
    public void joinToRoom(String username, int roomId){
        UserEntity userEntity = userService.getUserByUsername(username);
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
            @CacheEvict(value = "userRoomsByUsername",key = "#username"),
            @CacheEvict(value = "userJoinedRoomsByUsername",key = "#username"),
            @CacheEvict(value = "roomByRoomId",key = "#roomId"),
            @CacheEvict(value = "roomUsersByRoomId",key = "#roomId")
    })
    public void deleteRoom(String username, int roomId){
        UserEntity userEntity = userService.getUserByUsername(username);
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(userEntity.getId())){
            throw new ValidationException("Only the owner can delete the room");
        }
        List<UserEntity> roomUsers = self.getRoomUsers(roomId).getData();
        evictRemovedRoomUserCache(roomUsers);
        roomRepository.delete(room);
    }


    @Caching(evict = {
            @CacheEvict(value = "roomUsersByRoomId",key = "#roomId"),
            @CacheEvict(value = "userRoomsByUsername",key = "#addingUsername"),
            @CacheEvict(value = "userJoinedRoomsByUsername",key = "#addingUsername"),
    })
    public void addUserToRoom(String addingUsername, int roomId, String ownerUsername){
        UserEntity owner = userService.getUserByUsername(ownerUsername);
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(owner.getId())){
            throw new ValidationException("Only the owner can add user to the room");
        }
        if(room.getClosed()){
            throw new ValidationException("The room is closed");
        }
        UserEntity user = userService.getUserByUsername(addingUsername);
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
        UserEntity owner = userService.getUserByUsername(ownerUsername);
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);

        if(!room.getMultiUser()){
            throw new ValidationException("The user cannot remove from a dual user room");
        }

        if(!room.getCreatedBy().getId().equals(owner.getId())){
            throw new ValidationException("Only the owner can remove user from the room");
        }
        UserEntity user = userService.getUserByUsername(removingUsername);
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

    @CacheEvict(value = "roomByRoomId",key = "#roomEntity.id")
    public RoomEntity updateMultiUserRoom(RoomEntity roomEntity){
        UserEntity user = userService.getUserByUsername(roomEntity.getCreatedBy());

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

    @Cacheable(value = "roomUsersByRoomId",key = "#roomId")
    public CachableObject<List<UserEntity>> getRoomUsers(int roomId){
        Room room = roomRepository.findByIdWithUsers(roomId).orElseThrow(RoomNotFoundException::new);
        return  new CachableObject<>(room.getUsers().stream().map(userMapper::userToUserEntity).toList());
    }

    @Cacheable(value = "roomByRoomId",key = "#roomId")
    public RoomEntity getRoom(int roomId){
        Room room = roomRepository.findByIdWithUsers(roomId).orElseThrow(RoomNotFoundException::new);
        return roomMapper.roomToRoomEntity(room);
    }

    @Cacheable(value = "userRoomsByUsername",key = "#username")
    public CachableObject<List<RoomEntity>> getUserRooms(String username){
        List<Room> rooms = roomRepository.findByCreatedByUsername(username);
        return new CachableObject<>(rooms.stream().map(roomMapper::roomToRoomEntity).toList());
    }

    @Cacheable(value = "userJoinedRoomsByUsername",key = "#username")
    public CachableObject<List<RoomEntity>> getUserJoinedRooms(String username){
        List<Room> rooms = roomRepository.findUserJoinedRooms(username);
        return new CachableObject<>(rooms.stream().map(roomMapper::roomToRoomEntity).toList());
    }

    @Caching(evict = {
            @CacheEvict(value = "roomUsersByRoomId",key = "#roomId"),
            @CacheEvict(value = "userRoomsByUsername",key = "#username"),
            @CacheEvict(value = "userJoinedRoomsByUsername",key = "#username"),
    })
    public void leaveFromRoom(int roomId,String username){
        Room room =  roomRepository.findByIdWithUsers(roomId).orElseThrow(RoomNotFoundException::new);
        UserEntity user = userService.getUserByUsername(username);

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

    private boolean validateRoomDataAccess(String currentAccessUser, int roomId){
        RoomEntity roomEntity = self.getRoom(roomId);
        if(roomEntity.getCreatedBy().equals(currentAccessUser)) return false;
        List<UserEntity> roomMembers = self.getRoomUsers(roomId).getData();
        if(roomMembers.stream().anyMatch(u->u.getUsername().equals(currentAccessUser))) return false;
        return true;
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
