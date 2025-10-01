package me.lahirudilhara.webchat.service.message;

import me.lahirudilhara.webchat.common.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entityModelMappers.MessageMapper;
import me.lahirudilhara.webchat.entityModelMappers.UserMapper;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import me.lahirudilhara.webchat.service.api.room.RoomValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RoomMessageQueryService {

    private final RoomValidator roomValidator;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;

    public RoomMessageQueryService(RoomValidator roomValidator, MessageRepository messageRepository, MessageMapper messageMapper, UserMapper userMapper) {
        this.roomValidator = roomValidator;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.userMapper = userMapper;
    }

    public List<MessageEntity> getMessagesByRoomId(int roomId, String accessUser, Pageable pageable){
        if(roomValidator.isNotUserAbleToAccessRoom(accessUser, roomId)) throw new RoomNotFoundException();
        Page<Message> page = messageRepository.findByRoomIdOrderByCreatedAtDesc(roomId, pageable);

        // Remove deleted messages and convert
        return page.getContent().stream().filter(m->!m.getDeleted()).map(messageMapper::messageToMessageEntity).toList();
    }

    public List<MessageEntity> getLast10MessagesInRoom(int roomId, String accessUser){
        if(roomValidator.isNotUserAbleToAccessRoom(accessUser, roomId)) throw new RoomNotFoundException();
        List<Message> messages = messageRepository.findTop10ByRoomIdOrderByIdDesc(roomId);
        if(messages.isEmpty()) return Collections.emptyList();
        return messages.stream().filter(m->!m.getDeleted()).map(messageMapper::messageToMessageEntity).toList();
    }

    public List<MessageEntity> get15MessagesBeforeGivenMessageIdInRoom(int roomId, int messageId, String accessUser){
        if(roomValidator.isNotUserAbleToAccessRoom(accessUser, roomId)) throw new RoomNotFoundException();
        List<Message> messages = messageRepository.findTop15ByRoomIdAndIdLessThanOrderByIdDesc(roomId, messageId);
        if(messages.isEmpty()) return Collections.emptyList();
        return messages.stream().filter(m->!m.getDeleted()).map(messageMapper::messageToMessageEntity).toList();
    }

    public List<MessageEntity> getMessagesAfterGivenMessageIdInRoom(int roomId, int messageId, String accessUser){
        if(roomValidator.isNotUserAbleToAccessRoom(accessUser, roomId)) throw new RoomNotFoundException();
        List<Message> messages = messageRepository.findByRoomIdAndIdGreaterThanOrderByIdAsc(roomId, messageId);
        if(messages.isEmpty()) return Collections.emptyList();
        return messages.stream().filter(m->!m.getDeleted()).map(messageMapper::messageToMessageEntity).toList();
    }

    public List<UserEntity> getMessageViewUsersInRoom(int roomId, int messageId, String accessUser){
        if(roomValidator.isNotUserAbleToAccessRoom(accessUser, roomId)) throw new RoomNotFoundException();
        List<User> messageSeenUsers = messageRepository.findTheUsersWhoSawTheMessage(roomId,messageId);
        if(messageSeenUsers.isEmpty()) return Collections.emptyList();
        return messageSeenUsers.stream().map(userMapper::userToUserEntity).toList();
    }
}
