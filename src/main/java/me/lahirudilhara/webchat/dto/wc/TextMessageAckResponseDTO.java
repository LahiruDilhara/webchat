package me.lahirudilhara.webchat.dto.wc;

import me.lahirudilhara.webchat.dto.message.TextMessageResponseDTO;

import java.time.Instant;

public class TextMessageAckResponseDTO extends TextMessageResponseDTO {
    private String uuid;

    public TextMessageAckResponseDTO() {
    }

    public TextMessageAckResponseDTO(int id, Instant createdAt, String senderUsername, int senderId, int roomId, String content, Instant editedAt, String uuid) {
        super(id, createdAt, senderUsername, senderId, roomId, content, editedAt);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
