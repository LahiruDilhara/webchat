package me.lahirudilhara.webchat.repositories;

import me.lahirudilhara.webchat.models.UserRoomId;
import me.lahirudilhara.webchat.models.UserRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoomStatusRepository extends JpaRepository<UserRoomStatus, UserRoomId> {
    List<UserRoomStatus> findAllByUser_Id(Integer userId);

    @Query("SELECT s FROM UserRoomStatus s WHERE s.user.id = :userId")
    List<UserRoomStatus> findAllByUserId(@Param("userId") Integer userId);


    @Query("SELECT s FROM UserRoomStatus s WHERE s.room.id = :roomId AND s.user.id IN :userIds")
    List<UserRoomStatus> findAllByRoomIdAndUserIds(@Param("roomId") Integer roomId, @Param("userIds") List<Integer> userIds);

    @Query("SELECT s FROM UserRoomStatus s WHERE s.room.id IN :roomIds")
    List<UserRoomStatus> findAllByRoomIds(@Param("roomIds") List<Integer> roomIds);
}
