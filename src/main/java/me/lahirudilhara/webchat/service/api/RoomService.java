package me.lahirudilhara.webchat.service.api;

import me.lahirudilhara.webchat.common.exceptions.BaseException;
import me.lahirudilhara.webchat.common.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.common.exceptions.UserNotFoundException;
import me.lahirudilhara.webchat.entities.RoomEntity;
import me.lahirudilhara.webchat.entityModelMappers.RoomMapper;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import me.lahirudilhara.webchat.repositories.RoomRepository;
import me.lahirudilhara.webchat.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, UserRepository userRepository,MessageRepository messageRepository,RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.roomMapper = roomMapper;
    }

    public RoomEntity createRoom(RoomEntity roomEntity) {
        User user = userRepository.findByUsername(roomEntity.getCreatedBy());
        if (user == null) {
            throw new UserNotFoundException();
        }
        Room room = roomMapper.roomEntityToRoom(roomEntity,user);
        room.setCreatedBy(user);
        room.setCreatedAt(Instant.now());
        List<User> members = new ArrayList<>();
        members.add(user);
        room.setUsers(members);
        String error = validateRoom(room);
        if (error != null) {
            throw new BaseException(error,HttpStatus.BAD_REQUEST);
        }
        return roomMapper.roomToRoomEntity(roomRepository.save(room));
    }

//    @CacheEvict(value = "room",key = "#roomId")
    public void joinToRoom(String username, int roomId){
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(user.getId())){
            if(room.getIsPrivate()){
                throw new BaseException("The room is private. cannot join",HttpStatus.BAD_REQUEST);
            }
            else if(room.getClosed()){
                throw new BaseException("The room is closed",HttpStatus.BAD_REQUEST);
            }
        }

        List<User> members = room.getUsers();
        if(members.contains(user)){
            throw new BaseException("The user already exists in the room", HttpStatus.CONFLICT);
        }
        if(members.size() >= 2 && !room.getMultiUser()){
            throw new BaseException("The room is not multiuser. cannot join",HttpStatus.BAD_REQUEST);
        }
        members.add(user);
        room.setUsers(members);
        roomRepository.save(room);
    }

//    @Cacheable(value = "room",key = "#username")
    public List<RoomEntity> getUserRooms(String username){
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }

        return roomRepository.findAll().stream().map(r->roomMapper.roomToRoomEntity(r)).toList();
    }

//    @CacheEvict(value = "room",key = "#roomId")
    public void deleteRoom(String username, int roomId){
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(user.getId())){
            throw new BaseException("Only the owner can delete the room",HttpStatus.CONFLICT);
        }
        roomRepository.delete(room);
    }

//    @CacheEvict(value = "room",key = "#roomId")
    public void addUserToRoom(int userId, int roomId, String ownerUsername){
        User owner = userRepository.findByUsername(ownerUsername);
        if (owner == null) {
            throw new UserNotFoundException();
        }
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(owner.getId())){
            throw new BaseException("Only the owner can add user to the room",HttpStatus.BAD_REQUEST);
        }
        if(room.getClosed()){
            throw new BaseException("The room is closed",HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<User> members = room.getUsers();
        if(members.contains(user)){
            throw new BaseException("The user already exists in the room", HttpStatus.CONFLICT);
        }
        if(members.size() >= 2 && !room.getMultiUser()){
            throw new BaseException("The room is not multiuser. cannot join",HttpStatus.BAD_REQUEST);
        }
        members.add(user);
        roomRepository.save(room);
    }

//    @CacheEvict(value = "room",key = "#roomId")
    public void removeUserFromRoom(int userId, int roomId,  String ownerUsername){
        User owner = userRepository.findByUsername(ownerUsername);
        if (owner == null) {
            throw new UserNotFoundException();
        }
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(owner.getId())){
            throw new BaseException("Only the owner can remove user from the room",HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(!room.getUsers().contains(user)){
            throw new BaseException("The user is not a member of the room",HttpStatus.BAD_REQUEST);
        }
        List<User> members = room.getUsers();
        members.remove(user);
        if(members.isEmpty()){
            roomRepository.delete(room);
        }
        else{
            roomRepository.save(room);
        }
    }

//    @CacheEvict(value = "room",key = "#roomId")
    public RoomEntity updateRoom(RoomEntity roomEntity){
        User user = userRepository.findByUsername(roomEntity.getCreatedBy());
        if (user == null) {
            throw new UserNotFoundException();
        }

        Room room = roomRepository.findById(roomEntity.getId()).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(user.getId())){
            throw new BaseException("Only the owner can update the room",HttpStatus.BAD_REQUEST);
        }

        roomMapper.mapRoomEntityToRoom(roomEntity,room);
        String error = validateRoom(room);
        if(error != null){
            throw new BaseException(error,HttpStatus.BAD_REQUEST);
        }
        return roomMapper.roomToRoomEntity(roomRepository.save(room));
    }

    private String validateRoom(Room room){
        int usersCount = room.getUsers().size();
        if(!room.getMultiUser() && usersCount > 2){
            return "Cannot create non multi user room with more than two users";
        }
        if(!room.getMultiUser() && !room.getIsPrivate()){
            return "Cannot make non multi user room public";
        }
        return null;
    }

    public List<Message> getRoomMessages(int roomId, Pageable pageable){
        Page<Message> page = messageRepository.findByRoomIdOrderByCreatedAtDesc(roomId, pageable);
        List<Message> messages = page.getContent().stream().filter(m->!m.isDeleted()).toList();
        return messages;
    }

    public RoomEntity getRoom(int roomId){
        Room room = roomRepository.findByIdWithUsers(roomId).orElseThrow(RoomNotFoundException::new);
        return roomMapper.roomToRoomEntity(room);
    }

}
