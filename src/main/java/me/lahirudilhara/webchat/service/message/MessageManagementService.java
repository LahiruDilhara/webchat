package me.lahirudilhara.webchat.service.message;

import me.lahirudilhara.webchat.common.exceptions.MessageNotFoundException;
import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.entities.message.TextMessageEntity;
import me.lahirudilhara.webchat.entityModelMappers.MessageMapper;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.models.message.TextMessage;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import me.lahirudilhara.webchat.service.api.room.RoomValidator;
import me.lahirudilhara.webchat.service.api.room.RoomManagementService;
import me.lahirudilhara.webchat.service.api.room.RoomQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MessageManagementService {
    private static final Logger log = LoggerFactory.getLogger(MessageManagementService.class);
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final MessageAccessValidator messageAccessValidator;

    public MessageManagementService(MessageRepository messageRepository, MessageMapper messageMapper, MessageAccessValidator messageAccessValidator) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.messageAccessValidator = messageAccessValidator;
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
        messageAccessValidator.validateAlteration(messageEntity.getSenderUsername(),messageEntity.getRoomId(),messageId);
        messageEntity.setEditedAt(Instant.now());
        TextMessage message = (TextMessage) messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);
        messageMapper.updateTextMessageEntityToTextMessage(messageEntity,message);
        Message updatedMessage = messageRepository.save(message);
        return messageMapper.messageToMessageEntity(updatedMessage);
    }

    public void deleteMessage(int messageId, int roomId, String ownerName){
        messageAccessValidator.validateAlteration(ownerName,roomId,messageId);
        Message message = messageRepository.findByIdWithSenderAndRoom(messageId).orElseThrow(MessageNotFoundException::new);
        message.setDeleted(true);
        messageRepository.save(message);
    }
}
