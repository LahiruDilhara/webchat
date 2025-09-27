package me.lahirudilhara.webchat.controller;

import me.lahirudilhara.webchat.service.api.room.RoomMembershipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/rooms")
public class UserRoomMembershipController {
    private final RoomMembershipService roomMembershipService;

    public UserRoomMembershipController(RoomMembershipService roomMembershipService) {
        this.roomMembershipService = roomMembershipService;
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity joinToRoom(@PathVariable int roomId, Principal principal){
        roomMembershipService.joinToRoom(principal.getName(),roomId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roomId}/add/{username}")
    public ResponseEntity addUserToRoom(@PathVariable int roomId,@PathVariable String username, Principal principal){
        roomMembershipService.addUserToRoom(username,roomId,principal.getName());
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{roomId}/remove/{username}")
    public ResponseEntity removeUserFromRoom(@PathVariable int roomId, @PathVariable String username, Principal principal){
        roomMembershipService.removeUserFromRoom(username,roomId,principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roomId}/leave")
    public ResponseEntity leaveRoom(@PathVariable int roomId, Principal principal){
        roomMembershipService.leaveFromRoom(roomId,principal.getName());
        return ResponseEntity.noContent().build();
    }
}
