package me.lahirudilhara.webchat.dto.websocket.message;

import java.time.Instant;

public class MessageResponseDTO {
    private Integer id;
    private Instant createdAt;
    private String message;
    private Instant editedAt;
    private String senderUsername;
    private Integer roomId;

    public MessageResponseDTO() {
    }

    public MessageResponseDTO(Integer id, Instant createdAt, String message, Instant editedAt, String senderUsername, Integer roomId) {
        this.id = id;
        this.createdAt = createdAt;
        this.message = message;
        this.editedAt = editedAt;
        this.senderUsername = senderUsername;
        this.roomId = roomId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(Instant editedAt) {
        this.editedAt = editedAt;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}
