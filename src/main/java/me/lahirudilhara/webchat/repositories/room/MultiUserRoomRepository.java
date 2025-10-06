package me.lahirudilhara.webchat.repositories.room;

import me.lahirudilhara.webchat.models.room.MultiUserRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MultiUserRoomRepository extends JpaRepository<MultiUserRoom,Integer> {

    @Query("SELECT r FROM multi_user_room r WHERE r.isPrivate = false AND r.name LIKE CONCAT('%', COALESCE(:roomName, ''), '%')")
    Page<MultiUserRoom> getPublicMultiUserRooms(@Param("roomName") String roomName,Pageable pageable);
}
