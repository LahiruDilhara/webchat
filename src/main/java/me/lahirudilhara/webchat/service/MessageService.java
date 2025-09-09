package me.lahirudilhara.webchat.service;

import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.entityModelMappers.MessageMapper;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    public MessageEntity addMessageAsync(Message message){
        Message addedMessage = messageRepository.save(message);
        Message queryiedMessage = messageRepository.findByIdWithSenderAndRoom(addedMessage.getId()).orElseThrow();
        return messageMapper.messageToMessageEntity(queryiedMessage);
    }

    public Message addMessage(Message message){
        return messageRepository.save(message);
    }

    public MessageEntity getMessageById(int id){
        Message message = messageRepository.findByIdWithSenderAndRoom(id).orElseThrow();
        return messageMapper.baseMessageToMessageEntity(message);
    }


//    public Message updateMessage(UpdateMessageDTO updateMessageDTO, int messageId, String ownerName){
//        Message message = validateAlteration(messageId,ownerName);
//
//        message.setEditedAt(Instant.now());
//        message = messageMapper.updateMessageDTOToMessage(updateMessageDTO,message);
//        return messageRepository.save(message);
//    }

//    public void deleteMessage(int messageId, String ownerName){
//        Message message = validateAlteration(messageId,ownerName);
//        message.setDeleted(true);
//        messageRepository.save(message);
//    }

//    private Message validateAlteration(int messageId, String ownerName){
//        Message message = messageRepository.findById(messageId).orElse(null);
//        if (message == null) throw new BaseException("Message not found", HttpStatus.BAD_REQUEST);
//        if(message.isDeleted()) throw new BaseException("Message not found", HttpStatus.BAD_REQUEST);
//        if(!message.getSentBy().getUsername().equals(ownerName)) throw new BaseException("Only the owner can update or delete the message", HttpStatus.BAD_REQUEST);
//        if(message.getRoom().getUsers().stream().noneMatch(u->u.getUsername().equals(ownerName))) throw new BaseException("User is not a member of the room", HttpStatus.BAD_REQUEST);
//
//        return message;
//    }
}
