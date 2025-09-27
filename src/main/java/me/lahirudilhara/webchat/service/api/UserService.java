package me.lahirudilhara.webchat.service.api;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.common.exceptions.RoomNotFoundException;
import me.lahirudilhara.webchat.common.exceptions.UserNotFoundException;
import me.lahirudilhara.webchat.entities.room.RoomDetailsEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entities.UserStatEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.user.BaseUserEntity;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.UserRoomStatus;
import me.lahirudilhara.webchat.entityModelMappers.RoomMapper;
import me.lahirudilhara.webchat.entityModelMappers.UserMapper;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import me.lahirudilhara.webchat.repositories.RoomRepository;
import me.lahirudilhara.webchat.repositories.UserRepository;
import me.lahirudilhara.webchat.repositories.UserRoomStatusRepository;
import me.lahirudilhara.webchat.service.api.room.RoomManagementService;
import me.lahirudilhara.webchat.service.api.room.RoomMetricsProviderService;
import me.lahirudilhara.webchat.service.api.room.RoomQueryService;
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
    private final RoomManagementService roomManagementService;
    private final UserRoomStatusRepository userRoomStatusRepository;
    private final MessageRepository messageRepository;
    private final RoomMapper roomMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoomQueryService roomQueryService;
    private final RoomMetricsProviderService roomMetricsProviderService;
    private final RoomRepository roomRepository;

    private UserService self;
    // created for caching, because when use cachable methods inside same class, that not use cache proxy, instead call directly

    public UserService(UserRepository userRepository, UserMapper userMapper, @Lazy RoomManagementService roomManagementService, @Lazy UserService self, UserRoomStatusRepository userRoomStatusRepository, MessageRepository messageRepository, RoomMapper roomMapper, PasswordEncoder passwordEncoder, RoomQueryService roomQueryService, RoomMetricsProviderService roomMetricsProviderService, RoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roomManagementService = roomManagementService;
        this.self = self;
        this.userRoomStatusRepository = userRoomStatusRepository;
        this.messageRepository = messageRepository;
        this.roomMapper = roomMapper;
        this.passwordEncoder = passwordEncoder;
        this.roomQueryService = roomQueryService;
        this.roomMetricsProviderService = roomMetricsProviderService;
        this.roomRepository = roomRepository;
    }

    public UserEntity addUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setLastSeen(Instant.now());
        User user = userRepository.save(userMapper.userEntityToUser(userEntity));
        return userMapper.userToUserEntity(user);
    }

    @Cacheable(value = "userServiceUserByUsername",key = "#username")
    public BaseUserEntity getUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        if(user == null) throw new UserNotFoundException();
        return userMapper.userToBaseUserEntity(userRepository.findByUsername(username));
    }

    @Cacheable(value = "userById",key = "#id")
    public BaseUserEntity getBaseUserById(int id){
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.userToBaseUserEntity(user);
    }

    public List<RoomDetailsEntity> getUserJoinedRooms(String username){
        List<RoomEntity> userJoinedRooms = roomQueryService.getUserJoinedRooms(username).getData();
        if(userJoinedRooms.isEmpty()) throw new UserNotFoundException();
        List<RoomDetailsEntity> userRoomStatEntities = new ArrayList<>();

        for (RoomEntity roomEntity : userJoinedRooms) {
            List<UserEntity> roomMembers = roomQueryService.getRoomMembers(roomEntity.getId());

            UserEntity currentUser = roomMembers.stream().filter(u->u.getUsername().equals(username)).findFirst().orElse(null);
            if(currentUser == null) continue;

            int userUnreadMessageCount = roomMetricsProviderService.roomUnreadMessageCountByUser(currentUser,roomEntity.getId());

            userRoomStatEntities.add(roomMapper.userRoomToUserRoomDetailsEntity(roomEntity,userUnreadMessageCount,roomMembers));
        }

        return userRoomStatEntities;
    }
}
