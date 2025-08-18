package me.lahirudilhara.webchat.service.api;

import me.lahirudilhara.webchat.core.exceptions.BaseException;
import me.lahirudilhara.webchat.core.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.core.exceptions.UserNotFoundException;
import me.lahirudilhara.webchat.dto.api.room.AddRoomDTO;
import me.lahirudilhara.webchat.dto.api.room.UpdateRoomDTO;
import me.lahirudilhara.webchat.mappers.api.RoomMapper;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.RoomRepository;
import me.lahirudilhara.webchat.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, UserRepository userRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.roomMapper = roomMapper;
    }

    public Room createRoom(AddRoomDTO addRoomDTO,String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        Room room = roomMapper.addRoomDtoToRoom(addRoomDTO);
        room.setCreatedBy(user);
        List<User> members = new ArrayList<>();
        members.add(user);
        room.setUsers(members);
        String error = validateRoom(room);
        if (error != null) {
            throw new BaseException(error,HttpStatus.BAD_REQUEST);
        }
        return roomRepository.save(room);
    }

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

    public List<Room> getUserRooms(String username){
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }

        return roomRepository.findAll();
    }

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

    public Room updateRoom(int roomId, UpdateRoomDTO updateRoomDTO, String ownerUserName){
        User user = userRepository.findByUsername(ownerUserName);
        if (user == null) {
            throw new UserNotFoundException();
        }

        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        if(!room.getCreatedBy().getId().equals(user.getId())){
            throw new BaseException("Only the owner can update the room",HttpStatus.BAD_REQUEST);
        }

        roomMapper.updateRoomDtoToRoom(updateRoomDTO,room);
        String error = validateRoom(room);
        if(error != null){
            throw new BaseException(error,HttpStatus.BAD_REQUEST);
        }
        return roomRepository.save(room);
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


}
