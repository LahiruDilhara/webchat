package me.lahirudilhara.webchat.dto.message;

import java.time.Instant;

public class MessageResponseDTO {
    private int id;
    private Instant createdAt;
    private String senderUsername;
    private int senderId;
    private int roomId;

    public MessageResponseDTO() {
    }

    public MessageResponseDTO(int id, Instant createdAt, String senderUsername, int senderId, int roomId) {
        this.id = id;
        this.createdAt = createdAt;
        this.senderUsername = senderUsername;
        this.senderId = senderId;
        this.roomId = roomId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
