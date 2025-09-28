package me.lahirudilhara.webchat.service.api.room;

import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.models.UserRoomStatus;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomMetricsProviderService {

    private final MessageRepository messageRepository;

    public RoomMetricsProviderService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public int roomUnreadMessageCountByUser(UserRoomStatus userRoomStatus, Integer roomId){
        return (int) messageRepository.countUnreadMessagesForUser(roomId,userRoomStatus.getLastSeenAt(),userRoomStatus.getUserRoomId().getUserId());
    }
}
