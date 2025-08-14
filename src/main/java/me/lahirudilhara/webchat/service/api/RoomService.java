package me.lahirudilhara.webchat.service.api;

import me.lahirudilhara.webchat.core.exceptions.UserNotFoundException;
import me.lahirudilhara.webchat.dto.room.AddRoomDTO;
import me.lahirudilhara.webchat.mappers.RoomMapper;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.RoomRepository;
import me.lahirudilhara.webchat.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
}
