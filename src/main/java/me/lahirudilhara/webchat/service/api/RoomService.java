package me.lahirudilhara.webchat.service.api;

import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.repositories.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(Room room) {
//        room.setCreatedAt();
        return roomRepository.save(room);
    }
}
