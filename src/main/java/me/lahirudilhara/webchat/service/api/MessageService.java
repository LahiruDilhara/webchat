package me.lahirudilhara.webchat.service.api;

import me.lahirudilhara.webchat.core.exceptions.BaseException;
import me.lahirudilhara.webchat.models.Message;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import me.lahirudilhara.webchat.repositories.RoomRepository;
import me.lahirudilhara.webchat.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public Message addMessage(Message message,int roomId, String username){
        Room room = roomRepository.findById(roomId).orElse(null);
        if(room == null){
            throw new BaseException("The room not found", HttpStatus.BAD_REQUEST);
        }
        message.setRoom(room);
        message.setCreatedAt(Instant.now());
        message.setEditedAt(message.getCreatedAt());
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new BaseException("The user not found", HttpStatus.BAD_REQUEST);
        }
        message.setSentBy(user);
        return messageRepository.save(message);
    }
}
