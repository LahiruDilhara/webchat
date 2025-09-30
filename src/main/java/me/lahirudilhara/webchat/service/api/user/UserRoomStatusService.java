package me.lahirudilhara.webchat.service.api.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.models.UserRoomId;
import me.lahirudilhara.webchat.models.UserRoomStatus;
import me.lahirudilhara.webchat.models.room.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.UserRoomStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class UserRoomStatusService {

    private final UserRoomStatusRepository userRoomStatusRepository;
    private final UserQueryService userQueryService;
    @PersistenceContext
    private EntityManager entityManager;

    public UserRoomStatusService(UserRoomStatusRepository userRoomStatusRepository, UserQueryService userQueryService) {
        this.userRoomStatusRepository = userRoomStatusRepository;
        this.userQueryService = userQueryService;
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

    @Transactional
    public void updateUserRoomLastScene(String username, Integer roomId) {
        UserEntity userEntity = userQueryService.getUserByUsername(username);
        UserRoomStatus userRoomStatus = getUserRoomStatus(userEntity.getId(), roomId);
        userRoomStatus.setLastSeenAt(Instant.now());
        userRoomStatusRepository.save(userRoomStatus);
    }
}
