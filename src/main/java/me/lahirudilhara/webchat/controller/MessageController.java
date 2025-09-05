package me.lahirudilhara.webchat.controller;

import me.lahirudilhara.webchat.dtoEntityMappers.api.MessageMapper;
import me.lahirudilhara.webchat.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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

//    @PutMapping("/{messageId}")
//    public MessageResponseDTO updateMessage(@PathVariable int messageId, @Validated @RequestBody UpdateMessageDTO updateMessageDTO, Principal principal) {
//        Message message = messageService.updateMessage(updateMessageDTO,messageId,principal.getName());
//        return messageMapper.MessageToMessageResponseDTO(message);
//    }

//    @DeleteMapping("/{messageId}")
//    public ResponseEntity deleteMessage(@PathVariable int messageId, Principal principal){
//        messageService.deleteMessage(messageId,principal.getName());
//        return ResponseEntity.noContent().build();
//    }
}
