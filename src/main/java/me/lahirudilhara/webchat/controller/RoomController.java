package me.lahirudilhara.webchat.controller;

import jakarta.validation.Valid;
import me.lahirudilhara.webchat.dto.api.room.AddRoomDTO;
import me.lahirudilhara.webchat.dto.api.room.RoomResponseDTO;
import me.lahirudilhara.webchat.dto.api.room.UpdateRoomDTO;
import me.lahirudilhara.webchat.dto.api.user.UserResponseDTO;
import me.lahirudilhara.webchat.dto.message.MessageResponseDTO;
import me.lahirudilhara.webchat.dtoEntityMappers.api.MessageMapper;
import me.lahirudilhara.webchat.dtoEntityMappers.api.RoomMapper;
import me.lahirudilhara.webchat.dtoEntityMappers.api.UserMapper;
import me.lahirudilhara.webchat.entities.RoomEntity;
import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.service.api.RoomService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final RoomMapper roomMapper;
    private final RoomService roomService;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;

    public RoomController(RoomMapper roomMapper, RoomService roomService,MessageMapper messageMapper, UserMapper userMapper) {
        this.roomMapper = roomMapper;
        this.roomService = roomService;
        this.messageMapper = messageMapper;
        this.userMapper = userMapper;
    }

    @PostMapping("/")
    public ResponseEntity<RoomResponseDTO> createRoom(@Valid @RequestBody AddRoomDTO addRoomDTO, Principal principal){
        RoomEntity roomEntity = roomMapper.addRoomDtoToRoomEntity(addRoomDTO);
        roomEntity.setCreatedBy(principal.getName());
        RoomEntity createdRoom = roomService.createRoom(roomEntity);
        return new ResponseEntity<>(roomMapper.roomEntityToRoomResponseDTO(createdRoom), HttpStatus.CREATED);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity joinToRoom(@PathVariable int roomId, Principal principal){
        roomService.joinToRoom(principal.getName(),roomId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    public List<RoomResponseDTO> getRooms(Principal principal){
        List<RoomEntity> roomEntities = roomService.getUserRooms(principal.getName());
        return roomEntities.stream().map(roomMapper::roomEntityToRoomResponseDTO).toList();
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
        RoomEntity roomEntity = roomMapper.updateRoomDtoToRoomEntity(updateRoomDTO);
        roomEntity.setId(roomId);
        roomEntity.setCreatedBy(principal.getName());
        RoomEntity updatedRoomEntity = roomService.updateRoom(roomEntity);
        return roomMapper.roomEntityToRoomResponseDTO(updatedRoomEntity);
    }

    @GetMapping("/{roomId}/messages")
    public List<MessageResponseDTO> getMessagesOfRoom(@PathVariable int roomId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy, Principal principal){
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());
        List<Message> messages = roomService.getRoomMessages(roomId,pageable);
        return messages.stream().map(messageMapper::messageToMessageResponse).toList();
    }

    @GetMapping("/{roomId}/users")
    public List<UserResponseDTO> getRoomUsers(@PathVariable int roomId, Principal principal){
        List<UserEntity> users = roomService.getRoomUsers(roomId,principal.getName());
        return users.stream().map(userMapper::userEntityToUserResponseDTO).toList();
    }
}
