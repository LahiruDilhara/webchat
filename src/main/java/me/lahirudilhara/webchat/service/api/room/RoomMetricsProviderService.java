package me.lahirudilhara.webchat.service.api.room;

import me.lahirudilhara.webchat.entities.UserStatEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RoomMetricsProviderService {

    private final MessageRepository messageRepository;

    public RoomMetricsProviderService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public int roomUnreadMessageCountByUser(UserEntity userEntity, Integer roomId){
        return (int) messageRepository.countUnreadMessagesForUser(roomId,userEntity.getLastSeen(),userEntity.getUsername());
    }
}
