package me.lahirudilhara.webchat.repositories;

import me.lahirudilhara.webchat.models.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {
    Page<Message> findByRoomIdOrderByCreatedAtDesc(Integer roomId, Pageable pageable);

    @Query("SELECT m FROM message m JOIN FETCH m.sender s JOIN FETCH m.room r WHERE m.id = :id")
    Optional<Message> findByIdWithSenderAndRoom(@Param("id") Integer id);
}
