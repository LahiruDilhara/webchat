package me.lahirudilhara.webchat.service;

import me.lahirudilhara.webchat.common.exceptions.MessageNotFoundException;
import me.lahirudilhara.webchat.common.exceptions.ValidationException;
import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.entities.message.TextMessageEntity;
import me.lahirudilhara.webchat.entityModelMappers.MessageMapper;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.models.message.TextMessage;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import me.lahirudilhara.webchat.service.api.room.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final RoomService roomService;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper, RoomService roomService) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.roomService = roomService;
    }

    public MessageEntity addMessageAsync(Message message){
        Message addedMessage = messageRepository.save(message);
        Message queryiedMessage = messageRepository.findByIdWithSenderAndRoom(addedMessage.getId()).orElseThrow(MessageNotFoundException::new);
        return messageMapper.messageToMessageEntity(queryiedMessage);
    }

    public Message addMessage(Message message){
        return messageRepository.save(message);
    }

    public MessageEntity getMessageById(int id){
        Message message = messageRepository.findByIdWithSenderAndRoom(id).orElseThrow(MessageNotFoundException::new);
        return messageMapper.baseMessageToMessageEntity(message);
    }


    public MessageEntity updateMessage(TextMessageEntity messageEntity, int messageId){
        validateAlteration(messageEntity.getSenderUsername(),messageEntity.getRoomId(),messageId);
        messageEntity.setEditedAt(Instant.now());
        TextMessage message = (TextMessage) messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);
        messageMapper.updateTextMessageEntityToTextMessage(messageEntity,message);
        Message updatedMessage = messageRepository.save(message);
        return messageMapper.messageToMessageEntity(updatedMessage);
    }

    public void deleteMessage(int messageId, int roomId, String ownerName){
        validateAlteration(ownerName,roomId,messageId);
        Message message = messageRepository.findByIdWithSenderAndRoom(messageId).orElseThrow(MessageNotFoundException::new);
        message.setDeleted(true);
        messageRepository.save(message);
    }

    private void validateAlteration(String username, int roomId, int messageId){
        Message message = messageRepository.findByIdWithSenderAndRoom(messageId).orElseThrow(MessageNotFoundException::new);
        if(!(message instanceof TextMessage)) throw new ValidationException("Message type not support update");
        if(message.getDeleted()) throw new ValidationException("Message not found");
        if(!message.getSender().getUsername().equals(username)) throw new ValidationException("Message not found");
        List<UserEntity> roomMembers = roomService.getRoomUsers(roomId).getData();
        if(roomMembers.stream().noneMatch(u->u.getUsername().equals(username))) throw new ValidationException("Room not found");
        return;
    }
}
