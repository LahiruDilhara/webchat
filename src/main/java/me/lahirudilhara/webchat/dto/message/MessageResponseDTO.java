package me.lahirudilhara.webchat.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {
    private Integer id;
    private Instant createdAt;
    private String senderUsername;
    private Integer senderId;
    private Integer roomId;
}
