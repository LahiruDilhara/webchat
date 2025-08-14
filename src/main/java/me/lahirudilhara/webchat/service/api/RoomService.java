package me.lahirudilhara.webchat.service.api;

import me.lahirudilhara.webchat.dto.room.AddRoomDTO;
import me.lahirudilhara.webchat.mappers.RoomMapper;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.repositories.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    public Room createRoom(AddRoomDTO addRoomDTO) {
        Room room = roomMapper.addRoomDtoToRoom(addRoomDTO);
        return roomRepository.save(room);
    }
}
