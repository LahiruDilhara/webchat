package me.lahirudilhara.webchat.service.websocket;

import org.springframework.stereotype.Service;

@Service
public class WebSocketMessageServiceOld {
//    private final MessageRepository messageRepository;
//    private final RoomRepository roomRepository;
//    private final UserRepository userRepository;

//    public WebSocketMessageServiceOld(MessageRepository messageRepository, RoomRepository roomRepository, UserRepository userRepository) {
//        this.messageRepository = messageRepository;
//        this.roomRepository = roomRepository;
//        this.userRepository = userRepository;
//    }
//
//    public Message addMessage(Message message,int roomId, String username){
//        Room room = roomRepository.findById(roomId).orElse(null);
//        if(room == null){
//            throw new BaseException("The room not found", HttpStatus.BAD_REQUEST);
//        }
//        message.setRoom(room);
//        message.setCreatedAt(Instant.now());
//        message.setEditedAt(message.getCreatedAt());
//        User user = userRepository.findByUsername(username);
//        if(user == null){
//            throw new BaseException("The user not found", HttpStatus.BAD_REQUEST);
//        }
//        message.setSentBy(user);
//        return messageRepository.save(message);
//    }
}
