package me.lahirudilhara.webchat.controller;

import me.lahirudilhara.webchat.dto.api.user.UserResponseDTO;
import me.lahirudilhara.webchat.dto.message.MessageResponseDTO;
import me.lahirudilhara.webchat.dtoEntityMappers.api.MessageMapper;
import me.lahirudilhara.webchat.dtoEntityMappers.api.UserMapper;
import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.service.message.RoomMessageQueryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomMessageQueryController {

    private final MessageMapper messageMapper;
    private final UserMapper userMapper;
    private final RoomMessageQueryService roomMessageQueryService;

    public RoomMessageQueryController( MessageMapper messageMapper, RoomMessageQueryService roomMessageQueryService, UserMapper userMapper) {
        this.messageMapper = messageMapper;
        this.roomMessageQueryService = roomMessageQueryService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{roomId}/messages")
    public List<MessageResponseDTO> getMessagesOfRoom(@PathVariable int roomId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy, Principal principal){
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());
        List<MessageEntity> messages = roomMessageQueryService.getMessagesByRoomId(roomId,principal.getName(),pageable);
        return messages.stream().map(messageMapper::messageEntityToMessageResponseDTO).toList();
    }

    @GetMapping("/{roomId}/messages/last")
    public List<MessageResponseDTO> getLast10MessagesInRoom(@PathVariable int roomId, Principal principal){
        List<MessageEntity> messages = roomMessageQueryService.getLast10MessagesInRoom(roomId,principal.getName());
        return messages.stream().map(messageMapper::messageEntityToMessageResponseDTO).toList();
    }

    @GetMapping("/{roomId}/messages/before/{messageId}")
    public List<MessageResponseDTO> getPrevious15MessagesInRoom(@PathVariable int roomId, @PathVariable int messageId, Principal principal){
        List<MessageEntity> messages = roomMessageQueryService.get15MessagesBeforeGivenMessageIdInRoom(roomId,messageId,principal.getName());
        return messages.stream().map(messageMapper::messageEntityToMessageResponseDTO).toList();
    }

    @GetMapping("/{roomId}/messages/after/{messageId}")
    public List<MessageResponseDTO> getMessagesAfterMessageId(@PathVariable int roomId, @PathVariable int messageId, Principal principal){
        List<MessageEntity> messages = roomMessageQueryService.getMessagesAfterGivenMessageIdInRoom(roomId,messageId,principal.getName());
        return messages.stream().map(messageMapper::messageEntityToMessageResponseDTO).toList();
    }

    @GetMapping("/{roomId}/messages/{messageId}/seen")
    public List<UserResponseDTO> getMessageSeenUsers(@PathVariable int roomId, @PathVariable int messageId, Principal principal){
        List<UserEntity> users = roomMessageQueryService.getMessageViewUsersInRoom(roomId,messageId,principal.getName());
        return users.stream().map(userMapper::userEntityToUserResponseDTO).toList();
    }
}
