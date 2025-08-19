package me.lahirudilhara.webchat.controller.api;

import me.lahirudilhara.webchat.dto.api.message.UpdateMessageDTO;
import me.lahirudilhara.webchat.dto.websocket.message.MessageResponseDTO;
import me.lahirudilhara.webchat.mappers.api.MessageMapper;
import me.lahirudilhara.webchat.models.Message;
import me.lahirudilhara.webchat.service.api.MessageService;
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
        Message message = messageService.updateMessage(updateMessageDTO,messageId,principal.getName());
        return messageMapper.MessageToMessageResponseDTO(message);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity deleteMessage(@PathVariable int messageId, Principal principal){
        messageService.deleteMessage(messageId,principal.getName());
        return ResponseEntity.noContent().build();
    }
}
