package me.lahirudilhara.webchat.service.api.user;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.entities.room.RoomDetailsEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entityModelMappers.UserMapper;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.UserRepository;
import me.lahirudilhara.webchat.service.api.room.RoomDetailsEntityBuilder;
import me.lahirudilhara.webchat.service.api.room.RoomQueryService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoomQueryService roomQueryService;
    private final UserQueryService userQueryService;
    private final RoomDetailsEntityBuilder roomDetailsEntityBuilder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, RoomQueryService roomQueryService,  UserQueryService userQueryService, RoomDetailsEntityBuilder roomDetailsEntityBuilder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roomQueryService = roomQueryService;
        this.userQueryService = userQueryService;
        this.roomDetailsEntityBuilder = roomDetailsEntityBuilder;
    }

    public UserEntity addUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setLastSeen(Instant.now());
        User user = userRepository.save(userMapper.userEntityToUser(userEntity));
        return userMapper.userToUserEntity(user);
    }

    public List<RoomDetailsEntity> getUserJoinedRooms(String username){
        UserEntity currentUser = userQueryService.getUserByUsername(username);
        List<RoomEntity> userJoinedRooms = roomQueryService.getUserJoinedRooms(username).getData();
        if(userJoinedRooms.isEmpty()) return Collections.emptyList();

        return userJoinedRooms.stream().map(r->roomDetailsEntityBuilder.build(r,currentUser)).filter(Objects::nonNull).toList();
    }
}

//SRP says: a method (or class) should have only one reason to change.