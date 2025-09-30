package me.lahirudilhara.webchat.controller.dev;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.lahirudilhara.webchat.dto.dev.AddTextMessageDTO;
import me.lahirudilhara.webchat.dto.message.MessageResponseDTO;
import me.lahirudilhara.webchat.dtoEntityMappers.api.MessageMapper;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.models.message.TextMessage;
import me.lahirudilhara.webchat.models.room.Room;
import me.lahirudilhara.webchat.service.api.user.UserQueryService;
import me.lahirudilhara.webchat.service.message.MessageManagementService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;

@RestController
@Profile("dev")
@RequestMapping("/dev/messages")
public class DevMessageController {

    private final UserQueryService userQueryService;
    private final MessageManagementService messageManagementService;
    private final MessageMapper  messageMapper;
    private final me.lahirudilhara.webchat.entityModelMappers.MessageMapper emMessageMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public DevMessageController(UserQueryService userQueryService, MessageManagementService messageManagementService, MessageMapper messageMapper, me.lahirudilhara.webchat.entityModelMappers.MessageMapper emMessageMapper) {
        this.userQueryService = userQueryService;
        this.messageManagementService = messageManagementService;
        this.messageMapper = messageMapper;
        this.emMessageMapper = emMessageMapper;
    }


    @PostMapping("/text/{roomId}")
    public MessageResponseDTO addMessageToRoom(@PathVariable int roomId, @RequestBody AddTextMessageDTO addTextMessageDTO, Principal principal) {
        String username = principal.getName();
        Room room = entityManager.getReference(Room.class,roomId);
        UserEntity userEntity = userQueryService.getUserByUsername(username);
        User user = entityManager.getReference(User.class, userEntity.getId());
        TextMessage message = new TextMessage();
        message.setRoom(room);
        message.setSender(user);
        Instant time = Instant.now();
        message.setCreatedAt(time);
        message.setEditedAt(time);
        message.setContent(addTextMessageDTO.getMessage());
        Message addedMessage = messageManagementService.addMessage(message);

        return messageMapper.messageEntityToMessageResponseDTO(emMessageMapper.messageToMessageEntity(addedMessage));
    }
}
