package me.lahirudilhara.webchat.service.api.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.lahirudilhara.webchat.models.UserRoomId;
import me.lahirudilhara.webchat.models.UserRoomStatus;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.UserRoomStatusRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserRoomStatusService {

    private final UserRoomStatusRepository userRoomStatusRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public UserRoomStatusService(UserRoomStatusRepository userRoomStatusRepository) {
        this.userRoomStatusRepository = userRoomStatusRepository;
    }

    public UserRoomStatus addUserRoomStatus(Integer userId, Integer roomId) {
        UserRoomStatus userRoomStatus = new UserRoomStatus();
        UserRoomId userRoomId = new UserRoomId(userId, roomId);
        userRoomStatus.setUserRoomId(userRoomId);
        userRoomStatus.setRoom(entityManager.getReference(Room.class, roomId));
        userRoomStatus.setUser(entityManager.getReference(User.class, userId));
        userRoomStatus.setLastSeenAt(Instant.now());
        return userRoomStatusRepository.save(userRoomStatus);
    }

    public UserRoomStatus getUserRoomStatus(Integer userId, Integer roomId) {
        return userRoomStatusRepository.getReferenceById(new UserRoomId(userId, roomId));
    }
}
