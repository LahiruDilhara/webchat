package me.lahirudilhara.webchat.repositories;

import me.lahirudilhara.webchat.entities.UserRoomId;
import me.lahirudilhara.webchat.entities.UserRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoomStatusRepository extends JpaRepository<UserRoomStatus, UserRoomId> {
    List<UserRoomStatus> findAllByUser_Id(Integer userId);

    @Query("SELECT s FROM UserRoomStatus s WHERE s.user.id = :userId")
    List<UserRoomStatus> findAllByUserId(@Param("userId") Integer userId);
}
