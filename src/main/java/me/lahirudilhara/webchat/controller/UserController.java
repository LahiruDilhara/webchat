package me.lahirudilhara.webchat.controller;

import me.lahirudilhara.webchat.dto.api.room.RoomResponseDTO;
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
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/rooms")
//    public List<RoomResponseDTO> getRooms(Principal principal) {
//
//    }
}
