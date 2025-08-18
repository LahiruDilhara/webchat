package me.lahirudilhara.webchat.controller.api;

import jakarta.validation.Valid;
import me.lahirudilhara.webchat.dto.api.room.AddRoomDTO;
import me.lahirudilhara.webchat.dto.api.room.RoomResponseDTO;
import me.lahirudilhara.webchat.dto.api.room.UpdateRoomDTO;
import me.lahirudilhara.webchat.mappers.RoomMapper;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.service.api.RoomService;
import me.lahirudilhara.webchat.service.api.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final RoomMapper roomMapper;
    private final RoomService roomService;
    private final UserService userService;

    public RoomController(RoomMapper roomMapper, RoomService roomService, UserService userService) {
        this.roomMapper = roomMapper;
        this.roomService = roomService;
        this.userService = userService;
    }

    @PostMapping("/")
    public RoomResponseDTO createRoom(@Valid @RequestBody AddRoomDTO addRoomDTO, Principal principal){
        Room room = roomService.createRoom(addRoomDTO, principal.getName());
        return roomMapper.roomDtoToRoomResponseDTO(room);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity joinToRoom(@PathVariable int roomId, Principal principal){
        roomService.joinToRoom(principal.getName(),roomId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    public List<RoomResponseDTO> getRooms(Principal principal){
        List<Room> rooms = roomService.getUserRooms(principal.getName());
        return rooms.stream().map(roomMapper::roomDtoToRoomResponseDTO).toList();
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity deleteRoom(@PathVariable int roomId, Principal principal){
        roomService.deleteRoom(principal.getName(),roomId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roomId}/add/{userId}")
    public ResponseEntity addUserToRoom(@PathVariable int roomId,@PathVariable int userId, Principal principal){
        roomService.addUserToRoom(userId,roomId,principal.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{roomId}/remove/{userId}")
    public ResponseEntity removeUserFromRoom(@PathVariable int roomId, @PathVariable int userId, Principal principal){
        roomService.removeUserFromRoom(userId,roomId,principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{roomId}")
    public RoomResponseDTO updateRoom(@PathVariable int roomId, @RequestBody UpdateRoomDTO updateRoomDTO, Principal principal){
        Room room = roomService.updateRoom(roomId,updateRoomDTO,principal.getName());
        return roomMapper.roomDtoToRoomResponseDTO(room);
    }
}
