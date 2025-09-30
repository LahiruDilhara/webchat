package me.lahirudilhara.webchat.controller;

import me.lahirudilhara.webchat.dto.message.MessageResponseDTO;
import me.lahirudilhara.webchat.dtoEntityMappers.api.MessageMapper;
import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.service.message.MessageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomMessageQueryController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    public RoomMessageQueryController(MessageService messageService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    @GetMapping("/{roomId}/messages")
    public List<MessageResponseDTO> getMessagesOfRoom(@PathVariable int roomId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy, Principal principal){
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());
        List<MessageEntity> messages = messageService.getMessagesByRoomId(roomId,principal.getName(),pageable);
        return messages.stream().map(messageMapper::messageEntityToMessageResponseDTO).toList();
    }

    @GetMapping("/{roomId}/messages/last")
    public List<MessageResponseDTO> getLast10MessagesInRoom(@PathVariable int roomId, Principal principal){
        List<MessageEntity> messages = messageService.getLast10MessagesInRoom(roomId,principal.getName());
        return messages.stream().map(messageMapper::messageEntityToMessageResponseDTO).toList();
    }

    @GetMapping("/{roomId}/messages/before/{messageId}")
    public List<MessageResponseDTO> getPrevious15MessagesInRoom(@PathVariable int roomId, @PathVariable int messageId, Principal principal){
        List<MessageEntity> messages = messageService.get15MessagesBeforeGivenMessageIdInRoom(roomId,messageId,principal.getName());
        return messages.stream().map(messageMapper::messageEntityToMessageResponseDTO).toList();
    }

    @GetMapping("/{roomid}/messages/after/{messageId}")
    public List<MessageResponseDTO> getMessagesAfterMessageId(@PathVariable int roomId, @PathVariable int messageId, Principal principal){
        List<MessageEntity> messages = messageService.getMessagesAfterGivenMessageIdInRoom(roomId,messageId,principal.getName());
        return messages.stream().map(messageMapper::messageEntityToMessageResponseDTO).toList();
    }
}
