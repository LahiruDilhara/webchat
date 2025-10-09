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
    private Integer senderId;
    private String senderUsername;
    private Integer roomId;
    private String uuid;
    private final String type = "Message";
}
