package me.lahirudilhara.webchat.service.message;

import me.lahirudilhara.webchat.common.exceptions.MessageNotFoundException;
import me.lahirudilhara.webchat.common.exceptions.ValidationException;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.models.message.TextMessage;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import me.lahirudilhara.webchat.service.api.room.RoomQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageAccessValidator {
    private final MessageRepository messageRepository;
    private final RoomQueryService roomQueryService;

    public MessageAccessValidator(MessageRepository messageRepository, RoomQueryService roomQueryService) {
        this.messageRepository = messageRepository;
        this.roomQueryService = roomQueryService;
    }

    public void validateAlteration(String username, int roomId, int messageId){
        Message message = messageRepository.findByIdWithSenderAndRoom(messageId).orElseThrow(MessageNotFoundException::new);
        if(!(message instanceof TextMessage)) throw new ValidationException("Message type not support update");
        if(message.getDeleted()) throw new ValidationException("Message not found");
        if(!message.getSender().getUsername().equals(username)) throw new ValidationException("Message not found");
        List<UserEntity> roomMembers = roomQueryService.getRoomUsers(roomId).getData();
        if(roomMembers.stream().noneMatch(u->u.getUsername().equals(username))) throw new ValidationException("Room not found");
    }
}
