package me.lahirudilhara.webchat.controller;

import me.lahirudilhara.webchat.dto.api.room.RoomResponseDTO;
import me.lahirudilhara.webchat.dtoEntityMappers.api.RoomMapper;
import me.lahirudilhara.webchat.entities.room.UserRoomStatEntity;
import me.lahirudilhara.webchat.service.api.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final RoomMapper roomMapper;

    public UserController(UserService userService, RoomMapper roomMapper) {
        this.userService = userService;
        this.roomMapper = roomMapper;
    }

    @GetMapping("/me/rooms")
    public List<RoomResponseDTO> getRooms(Principal principal) {
        List<UserRoomStatEntity> userRoomStatEntities = userService.getUserRoomStats(principal.getName());
        return userRoomStatEntities.stream().map(roomMapper::roomEntityToRoomResponseDTO).toList();
    }
}
