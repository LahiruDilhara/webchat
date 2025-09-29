package me.lahirudilhara.webchat.repositories.room;

import me.lahirudilhara.webchat.models.room.MultiUserRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultiUserRoomRepository extends JpaRepository<MultiUserRoom,Integer> {
}
