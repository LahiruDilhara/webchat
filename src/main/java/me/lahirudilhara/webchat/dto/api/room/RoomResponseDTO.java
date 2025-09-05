package me.lahirudilhara.webchat.dto.api.room;

import me.lahirudilhara.webchat.dto.api.user.UserResponseDTO;

import java.time.Instant;
import java.util.List;

public class RoomResponseDTO {
    private Integer id;
    private String name;
    private boolean isPrivate;
    private Instant createdAt;
    private boolean closed;
    private String createdBy;

    public RoomResponseDTO() {
    }

    public RoomResponseDTO(Integer id, String name, boolean isPrivate, Instant createdAt, boolean closed, String createdBy) {
        this.id = id;
        this.name = name;
        this.isPrivate = isPrivate;
        this.createdAt = createdAt;
        this.closed = closed;
        this.createdBy = createdBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
