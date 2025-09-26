package me.lahirudilhara.webchat.service.api;

import me.lahirudilhara.webchat.common.exceptions.UserNotFoundException;
import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.models.UserRoomStatus;
import me.lahirudilhara.webchat.entities.room.UserRoomStatEntity;
import me.lahirudilhara.webchat.entityModelMappers.RoomMapper;
import me.lahirudilhara.webchat.entityModelMappers.UserMapper;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.MessageRepository;
import me.lahirudilhara.webchat.repositories.UserRepository;
import me.lahirudilhara.webchat.repositories.UserRoomStatusRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    private final UserMapper userMapper;
    private final UserRoomStatusRepository userRoomStatusRepository;
    private final MessageRepository messageRepository;
    private final RoomMapper roomMapper;

    public UserService(UserRepository userRepository,UserMapper userMapper,UserRoomStatusRepository userRoomStatusRepository,MessageRepository messageRepository,RoomMapper roomMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userRoomStatusRepository = userRoomStatusRepository;
        this.messageRepository = messageRepository;
        this.roomMapper = roomMapper;
    }

    public UserEntity addUser(UserEntity userEntity) {
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        User user = userRepository.save(userMapper.userEntityToUser(userEntity));
        return userMapper.userToUserEntity(user);
    }

    @Cacheable(value = "userServiceUserByUsername",key = "#username")
    public UserEntity getUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        System.out.println(user);
        if(user == null) throw new UserNotFoundException();
        return userMapper.userToUserEntity(userRepository.findByUsername(username));
    }

    @Cacheable(value = "userById",key = "#id")
    public UserEntity getUserById(int id){
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.userToUserEntity(user);
    }

    public List<UserRoomStatEntity> getUserRoomStats(String username){
        User user = userRepository.findByUsername(username);
        if(user == null) throw new UserNotFoundException();
        List<Room> userRooms = user.getRooms();

        Map<Integer,UserRoomStatus> statusMap = userRoomStatusRepository.findAllByUserId(user.getId()).stream().collect(Collectors.toMap(s->s.getRoom().getId(),s->s));
        List<UserRoomStatEntity> userRoomStats = new ArrayList<>();

        for (Room room : userRooms) {
            UserRoomStatus status = statusMap.get(room.getId());
            if(status ==null) continue;

            long unReadMessageCount = messageRepository.countUnreadMessagesForUser(room.getId(),status.getLastSeenAt(),username);
            int memberCount = room.getUsers().size();
            userRoomStats.add(roomMapper.userRoomStatToUserRoomStatEntity(room,status,(int) unReadMessageCount,memberCount));
        }
        return userRoomStats;
    }
}
