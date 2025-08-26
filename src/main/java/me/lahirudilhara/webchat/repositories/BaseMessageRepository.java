package me.lahirudilhara.webchat.repositories;

import me.lahirudilhara.webchat.models.message.BaseMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseMessageRepository extends JpaRepository<BaseMessage,Integer> {
}
