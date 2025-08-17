package me.lahirudilhara.webchat.service.api;

import me.lahirudilhara.webchat.core.exceptions.BaseException;
import me.lahirudilhara.webchat.core.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.core.exceptions.UserNotFoundException;
import me.lahirudilhara.webchat.dto.room.AddRoomDTO;
import me.lahirudilhara.webchat.mappers.RoomMapper;
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
        return roomRepository.save(room);
    }

    public void addToRoom(String username, int roomId){
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
}
