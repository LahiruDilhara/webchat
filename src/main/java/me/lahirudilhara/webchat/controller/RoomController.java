package me.lahirudilhara.webchat.controller;

import jakarta.validation.Valid;
import me.lahirudilhara.webchat.dto.api.room.*;
import me.lahirudilhara.webchat.dto.api.user.UserResponseDTO;
import me.lahirudilhara.webchat.dtoEntityMappers.api.MessageMapper;
import me.lahirudilhara.webchat.dtoEntityMappers.api.RoomMapper;
import me.lahirudilhara.webchat.dtoEntityMappers.api.UserMapper;
import me.lahirudilhara.webchat.entities.room.DualUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.MultiUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.RoomDetailsEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.service.message.MessageManagementService;
import me.lahirudilhara.webchat.service.api.room.RoomManagementService;
import me.lahirudilhara.webchat.service.api.room.RoomQueryService;
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
    private final MessageManagementService messageManagementService;

    public RoomController(RoomMapper roomMapper, RoomManagementService roomManagementService, MessageMapper messageMapper, UserMapper userMapper,  RoomQueryService roomQueryService, MessageManagementService messageManagementService) {
        this.roomMapper = roomMapper;
        this.roomManagementService = roomManagementService;
        this.messageMapper = messageMapper;
        this.userMapper = userMapper;
        this.roomQueryService = roomQueryService;
        this.messageManagementService = messageManagementService;
    }

    @PostMapping("/multiUser")
    public ResponseEntity<RoomDetailsResponseDTO> createMultiUserRoom(@Valid @RequestBody AddMultiUserRoomDTO addMultiUserRoomDTO, Principal principal){
        MultiUserRoomEntity roomEntity = roomMapper.addMultiUserRoomDTOToMultiUserRoomEntity(addMultiUserRoomDTO);
        roomEntity.setCreatedBy(principal.getName());
        RoomEntity createdRoom = roomManagementService.createMultiUserRoom(roomEntity);
        return new ResponseEntity<>(roomMapper.roomEntityToRoomResponseDTO(createdRoom),HttpStatus.CREATED);
    }

    @PostMapping("/dualUser")
    public ResponseEntity<RoomDetailsResponseDTO> createDualUserRoom(@Valid @RequestBody AddDualUserRoomDTO addDualUserRoomDTO, Principal principal){
        DualUserRoomEntity roomEntity = roomMapper.addDualUserRoomDTOToDualUserRoomEntity(addDualUserRoomDTO);
        roomEntity.setCreatedBy(principal.getName());
        RoomEntity createdRoom = roomManagementService.createDualUserRoom(roomEntity,addDualUserRoomDTO.getAddingUsername());
        return new ResponseEntity<>(roomMapper.roomEntityToRoomResponseDTO(createdRoom),HttpStatus.CREATED);
    }

    @GetMapping("/")
    public List<RoomDetailsResponseDTO> getUserJoinedRooms(Principal principal){
        List<RoomDetailsEntity> roomEntities = roomQueryService.getUserJoinedRooms(principal.getName()).getData();
        return roomEntities.stream().map(roomMapper::roomEntityToRoomResponseDTO).toList();
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity deleteRoom(@PathVariable int roomId, Principal principal){
        roomManagementService.deleteRoom(principal.getName(),roomId);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/multiUser/{roomId}")
    public RoomDetailsResponseDTO updateMultiUserRoom(@PathVariable int roomId, @RequestBody UpdateMultiUserRoomDTO updateMultiUserRoomDTO, Principal principal){
        MultiUserRoomEntity roomEntity = roomMapper.updateMultiUserDtoToMultiUserRoomEntity(updateMultiUserRoomDTO);
        roomEntity.setId(roomId);
        roomEntity.setCreatedBy(principal.getName());
        RoomEntity updatedRoomEntity = roomManagementService.updateMultiUserRoom(roomEntity);
        return roomMapper.roomEntityToRoomResponseDTO(updatedRoomEntity);
    }



    @GetMapping("/{roomId}/users")
    public List<UserResponseDTO> getRoomUsers(@PathVariable int roomId, Principal principal){
        List<UserEntity> users = roomQueryService.getValidatedRoomUsers(roomId,principal.getName());
        return users.stream().map(userMapper::userEntityToUserResponseDTO).toList();
    }

}
