package me.lahirudilhara.webchat.service;

import me.lahirudilhara.webchat.core.exceptions.BaseException;
import me.lahirudilhara.webchat.dto.api.message.UpdateMessageDTO;
import me.lahirudilhara.webchat.mappers.api.MessageMapper;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final MessageRepository baseMessageRepository;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper, MessageRepository baseMessageRepository) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.baseMessageRepository = baseMessageRepository;
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

//    public BaseMessage addMessage(BaseMessage baseMessage){
//
//    }
}
