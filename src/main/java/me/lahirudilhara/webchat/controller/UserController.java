package me.lahirudilhara.webchat.controller;

import me.lahirudilhara.webchat.dtoEntityMappers.api.RoomMapper;
import me.lahirudilhara.webchat.service.api.user.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final RoomMapper roomMapper;

    public UserController(UserService userService, RoomMapper roomMapper) {
        this.userService = userService;
        this.roomMapper = roomMapper;
    }

}
