package me.lahirudilhara.webchat.repositories;

import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.models.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {
    Page<Message> findByRoomIdOrderByCreatedAtDesc(Integer roomId, Pageable pageable);

    @Query("SELECT m FROM message m JOIN FETCH m.sender s JOIN FETCH m.room r WHERE m.id = :id")
    Optional<Message> findByIdWithSenderAndRoom(@Param("id") Integer id);

    @Query("SELECT COUNT(m) from message m JOIN m.sender s JOIN m.room r WHERE r.id = :roomId AND m.createdAt > :lastAccessedAt AND s.id <> :userId")
    long countUnreadMessagesForUser(@Param("roomId") Integer roomId, @Param("lastAccessedAt")Instant lastAccessedAt, @Param("userId") Integer userId);

    List<Message> findTop10ByRoomIdOrderByIdDesc(Integer roomId);

    List<Message> findByRoomIdAndIdGreaterThanOrderByIdAsc(Integer roomId, Integer id);

    List<Message> findTop15ByRoomIdAndIdLessThanOrderByIdDesc(Integer roomId, Integer lastMessageId);

    @Query("SELECT urs.user FROM UserRoomStatus urs JOIN urs.room r JOIN message m ON m.room = r WHERE m.id = :messageId AND r.id = :roomId AND urs.lastSeenAt > m.createdAt")
    List<User> findTheUsersWhoSawTheMessage(@Param("roomId") Integer roomId,@Param("messageId")  Integer messageId);
}
