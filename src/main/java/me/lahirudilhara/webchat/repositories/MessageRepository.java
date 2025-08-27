package me.lahirudilhara.webchat.repositories;

import me.lahirudilhara.webchat.models.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {
    Page<Message> findByRoomIdOrderByCreatedAtDesc(Integer roomId, Pageable pageable);
}
