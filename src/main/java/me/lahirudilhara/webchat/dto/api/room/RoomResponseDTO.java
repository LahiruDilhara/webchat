package me.lahirudilhara.webchat.dto.api.room;

import me.lahirudilhara.webchat.dto.api.user.UserResponseDTO;

import java.time.Instant;
import java.util.List;

public class RoomResponseDTO {
    private Integer id;
    private String name;
    private Boolean isPrivate;
    private Instant createdAt;
    private Boolean closed;
    private String createdBy;
    private Boolean multiUser;

    public RoomResponseDTO() {
    }

    public RoomResponseDTO(Integer id, String name, Boolean isPrivate, Instant createdAt, Boolean closed, String createdBy, Boolean multiUser) {
        this.id = id;
        this.name = name;
        this.isPrivate = isPrivate;
        this.createdAt = createdAt;
        this.closed = closed;
        this.createdBy = createdBy;
        this.multiUser = multiUser;
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

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean isClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getMultiUser() {
        return multiUser;
    }

    public void setMultiUser(Boolean multiUser) {
        this.multiUser = multiUser;
    }
}
