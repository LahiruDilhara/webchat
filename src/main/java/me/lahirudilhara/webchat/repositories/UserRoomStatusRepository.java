package me.lahirudilhara.webchat.repositories;

import me.lahirudilhara.webchat.entities.UserRoomId;
import me.lahirudilhara.webchat.entities.UserRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoomStatusRepository extends JpaRepository<UserRoomStatus, UserRoomId> {
}
