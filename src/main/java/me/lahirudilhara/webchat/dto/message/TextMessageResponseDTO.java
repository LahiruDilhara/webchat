package me.lahirudilhara.webchat.dto.message;

import java.time.Instant;

public class TextMessageResponseDTO extends MessageResponseDTO {
    private String content;
    private Instant editedAt;

    public TextMessageResponseDTO() {
    }

    public TextMessageResponseDTO(int id, Instant createdAt, String senderUsername, int senderId, int roomId, String content, Instant editedAt) {
        super(id, createdAt, senderUsername, senderId, roomId);
        this.content = content;
        this.editedAt = editedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(Instant editedAt) {
        this.editedAt = editedAt;
    }
}
