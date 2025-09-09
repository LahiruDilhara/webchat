package me.lahirudilhara.webchat.controller;

import me.lahirudilhara.webchat.dto.api.message.UpdateMessageDTO;
import me.lahirudilhara.webchat.dto.message.MessageResponseDTO;
import me.lahirudilhara.webchat.dtoEntityMappers.api.MessageMapper;
import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.entities.message.TextMessageEntity;
import me.lahirudilhara.webchat.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {
    private static final Logger log = LoggerFactory.getLogger(MessageController.class);
    private final MessageMapper messageMapper;
    private final MessageService messageService;

    public MessageController(MessageMapper messageMapper, MessageService messageService) {
        this.messageMapper = messageMapper;
        this.messageService = messageService;
    }

    @PutMapping("/{messageId}")
    public MessageResponseDTO updateMessage(@PathVariable int messageId, @Validated @RequestBody UpdateMessageDTO updateMessageDTO, Principal principal) {
        TextMessageEntity textMessage = messageMapper.updateMessageDTOToMessageEntity(updateMessageDTO,principal.getName());
        MessageEntity updatedMessage = messageService.updateMessage(textMessage,messageId);
        return messageMapper.messageEntityToMessageResponseDTO(updatedMessage);
    }

    @DeleteMapping("/{roomId}/{messageId}")
    public ResponseEntity deleteMessage(@PathVariable int roomId, @PathVariable int messageId, Principal principal){
        messageService.deleteMessage(messageId,roomId,principal.getName());
        return ResponseEntity.noContent().build();
    }
}
