package me.lahirudilhara.webchat.controller;

import jakarta.validation.Valid;
import me.lahirudilhara.webchat.dto.api.room.*;
import me.lahirudilhara.webchat.dto.api.user.UserResponseDTO;
import me.lahirudilhara.webchat.dto.message.MessageResponseDTO;
import me.lahirudilhara.webchat.dtoEntityMappers.api.MessageMapper;
import me.lahirudilhara.webchat.dtoEntityMappers.api.RoomMapper;
import me.lahirudilhara.webchat.dtoEntityMappers.api.UserMapper;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.service.message.MessageService;
import me.lahirudilhara.webchat.service.api.room.RoomMembershipService;
import me.lahirudilhara.webchat.service.api.room.RoomManagementService;
import me.lahirudilhara.webchat.service.api.room.RoomQueryService;
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
    private final RoomManagementService roomManagementService;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;
    private final RoomQueryService roomQueryService;
    private final MessageService messageService;

    public RoomController(RoomMapper roomMapper, RoomManagementService roomManagementService, MessageMapper messageMapper, UserMapper userMapper,  RoomQueryService roomQueryService, MessageService messageService) {
        this.roomMapper = roomMapper;
        this.roomManagementService = roomManagementService;
        this.messageMapper = messageMapper;
        this.userMapper = userMapper;
        this.roomQueryService = roomQueryService;
        this.messageService = messageService;
    }

    @PostMapping("/multiUser")
    public ResponseEntity<RoomResponseDTO> createMultiUserRoom(@Valid @RequestBody AddMultiUserRoomDTO addMultiUserRoomDTO, Principal principal){
        RoomEntity roomEntity = roomMapper.addMultiUserRoomDtoToRoomEntity(addMultiUserRoomDTO);
        roomEntity.setCreatedBy(principal.getName());
        RoomEntity createdRoom = roomManagementService.createMultiUserRoom(roomEntity);
        return new ResponseEntity<>(roomMapper.roomEntityToRoomResponseDTO(createdRoom),HttpStatus.CREATED);
    }

    @PostMapping("/dualUser")
    public ResponseEntity<RoomResponseDTO> createDualUserRoom(@Valid @RequestBody AddDualUserRoomDTO addDualUserRoomDTO, Principal principal){
        RoomEntity roomEntity = roomMapper.addDualUserRoomDtoToRoomEntity(addDualUserRoomDTO);
        roomEntity.setCreatedBy(principal.getName());
        RoomEntity createdRoom = roomManagementService.createDualUserRoom(roomEntity,addDualUserRoomDTO.getAddingUsername());
        return new ResponseEntity<>(roomMapper.roomEntityToRoomResponseDTO(createdRoom),HttpStatus.CREATED);
    }

    @GetMapping("/")
    public List<RoomResponseDTO> getRooms(Principal principal){
        List<RoomEntity> roomEntities = roomQueryService.getUserRooms(principal.getName()).getData();
        return roomEntities.stream().map(roomMapper::roomEntityToRoomResponseDTO).toList();
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity deleteRoom(@PathVariable int roomId, Principal principal){
        roomManagementService.deleteRoom(principal.getName(),roomId);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/multiUser/{roomId}")
    public RoomResponseDTO updateMultiUserRoom(@PathVariable int roomId, @RequestBody UpdateMultiUserRoomDTO updateMultiUserRoomDTO, Principal principal){
        RoomEntity roomEntity = roomMapper.updateMultiUserDtoToRoomEntity(updateMultiUserRoomDTO);
        roomEntity.setId(roomId);
        roomEntity.setCreatedBy(principal.getName());
        RoomEntity updatedRoomEntity = roomManagementService.updateMultiUserRoom(roomEntity);
        System.out.println(updatedRoomEntity);
        return roomMapper.roomEntityToRoomResponseDTO(updatedRoomEntity);
    }

    @GetMapping("/{roomId}/messages")
    public List<MessageResponseDTO> getMessagesOfRoom(@PathVariable int roomId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy, Principal principal){
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());
        List<MessageEntity> messages = messageService.getMessagesByRoomId(roomId,principal.getName(),pageable);
        return messages.stream().map(messageMapper::messageEntityToMessageResponseDTO).toList();
    }

    @GetMapping("/{roomId}/users")
    public List<UserResponseDTO> getRoomUsers(@PathVariable int roomId, Principal principal){
        List<UserEntity> users = roomQueryService.getValidatedRoomUsers(roomId,principal.getName());
        return users.stream().map(userMapper::userEntityToUserResponseDTO).toList();
    }

}
