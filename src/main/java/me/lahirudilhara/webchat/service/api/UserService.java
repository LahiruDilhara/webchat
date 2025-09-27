package me.lahirudilhara.webchat.service.api;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.common.exceptions.UserNotFoundException;
import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.entities.UserStatEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.models.UserRoomStatus;
import me.lahirudilhara.webchat.entities.room.UserRoomStatEntity;
import me.lahirudilhara.webchat.entityModelMappers.RoomMapper;
import me.lahirudilhara.webchat.entityModelMappers.UserMapper;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import me.lahirudilhara.webchat.repositories.UserRepository;
import me.lahirudilhara.webchat.repositories.UserRoomStatusRepository;
import me.lahirudilhara.webchat.service.api.room.RoomService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoomService roomService;
    private final UserRoomStatusRepository userRoomStatusRepository;
    private final MessageRepository messageRepository;
    private final RoomMapper roomMapper;
    private final PasswordEncoder passwordEncoder;

    private UserService self;
    // created for caching, because when use cachable methods inside same class, that not use cache proxy, instead call directly

    public UserService(UserRepository userRepository, UserMapper userMapper, @Lazy RoomService roomService, @Lazy UserService self, UserRoomStatusRepository userRoomStatusRepository, MessageRepository messageRepository, RoomMapper roomMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roomService = roomService;
        this.self = self;
        this.userRoomStatusRepository = userRoomStatusRepository;
        this.messageRepository = messageRepository;
        this.roomMapper = roomMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity addUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setLastSeen(Instant.now());
        User user = userRepository.save(userMapper.userEntityToUser(userEntity));
        return userMapper.userToUserEntity(user);
    }

    @Cacheable(value = "userServiceUserByUsername",key = "#username")
    public UserEntity getUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        if(user == null) throw new UserNotFoundException();
        return userMapper.userToUserEntity(userRepository.findByUsername(username));
    }

    @Cacheable(value = "userById",key = "#id")
    public UserEntity getUserById(int id){
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.userToUserEntity(user);
    }

    public List<UserRoomStatEntity> getUserRoomStats(String username){
        UserEntity user = self.getUserByUsername(username);
        List<RoomEntity> userRooms = roomService.getUserJoinedRooms(username).getData();
        List<UserRoomStatus> userRoomStats = userRoomStatusRepository.findAllByRoomIds(userRooms.stream().map(RoomEntity::getId).toList());

        List<UserRoomStatEntity> userRoomStatEntities = new ArrayList<>();

        for (RoomEntity room : userRooms) {
            List<UserRoomStatus> roomUserStats = userRoomStats.stream().filter(r->r.getUserRoomId().getRoomId().equals(room.getId())).toList();
            UserRoomStatus currentUserRoomStatus = roomUserStats.stream().filter(r->r.getUserRoomId().getRoomId().equals(room.getId())).findFirst().orElse(null);
            if(currentUserRoomStatus == null) {
                log.error("The current user of the room hasn't any room status the user is {} and roomId is {}",user.getUsername(), room.getId());
                continue;
            }
            int membersCount = roomUserStats.size();
            int unReadMessageCount = (int) messageRepository.countUnreadMessagesForUser(room.getId(),currentUserRoomStatus.getLastSeenAt(),username);
            List<UserStatEntity> memberStatEntities = roomUserStats.stream().map(u->new UserStatEntity(self.getUserById(u.getUserRoomId().getUserId()).getUsername(),u.getLastSeenAt())).toList();
            userRoomStatEntities.add(roomMapper.userRoomStatToUserRoomStatEntity(room,currentUserRoomStatus,unReadMessageCount,membersCount,memberStatEntities));
        }
        return userRoomStatEntities;
    }
}
