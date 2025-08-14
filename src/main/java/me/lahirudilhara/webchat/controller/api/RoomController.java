package me.lahirudilhara.webchat.controller.api;

import jakarta.validation.Valid;
import me.lahirudilhara.webchat.dto.room.AddRoomDTO;
import me.lahirudilhara.webchat.dto.room.RoomResponseDTO;
import me.lahirudilhara.webchat.mappers.RoomMapper;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.service.api.RoomService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final RoomMapper roomMapper;
    private final RoomService roomService;

    public RoomController(RoomMapper roomMapper, RoomService roomService) {
        this.roomMapper = roomMapper;
        this.roomService = roomService;
    }

    @PostMapping("/")
    public RoomResponseDTO createRoom(@Valid @RequestBody AddRoomDTO addRoomDTO, Principal principal){
        Room room = roomService.createRoom(addRoomDTO, principal.getName());
        return roomMapper.roomDtoToRoomResponseDTO(room);
    }
}
