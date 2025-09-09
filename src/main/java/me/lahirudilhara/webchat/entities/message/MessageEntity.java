package me.lahirudilhara.webchat.entities.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {
    private Integer id;
    private Instant createdAt;
    private Boolean deleted;
    private Integer senderId;
    private String senderUsername;
    private Integer roomId;
}
