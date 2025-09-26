package me.lahirudilhara.webchat.repositories;

import me.lahirudilhara.webchat.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Integer> {

    @Query("SELECT r FROM room r LEFT JOIN FETCH r.users WHERE r.id = :roomId")
    Optional<Room> findByIdWithUsers(@Param("roomId") int roomId);

    @Query("SELECT r FROM room r WHERE r.createdBy.username = :username")
    List<Room> findByCreatedByUsername(@Param("username") String username);

    @Query("SELECT r FROM room r JOIN r.users u WHERE u.username = :username")
    List<Room> findUserJoinedRooms(@Param("username") String username);
}
