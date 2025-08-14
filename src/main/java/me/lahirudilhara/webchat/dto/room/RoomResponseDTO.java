package me.lahirudilhara.webchat.dto.room;

import me.lahirudilhara.webchat.dto.user.UserResponseDTO;

import java.time.Instant;
import java.util.List;

public class RoomResponseDTO {
    private Integer id;
    private String name;
    private boolean isPrivate;
    private Instant createdAt;
    private boolean closed;
    private UserResponseDTO createdBy;
    private List<UserResponseDTO> members;

    public RoomResponseDTO() {
    }

    public RoomResponseDTO(Integer id, String name, boolean isPrivate, Instant createdAt, boolean closed, UserResponseDTO createdBy, List<UserResponseDTO> members) {
        this.id = id;
        this.name = name;
        this.isPrivate = isPrivate;
        this.createdAt = createdAt;
        this.closed = closed;
        this.createdBy = createdBy;
        this.members = members;
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
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

    public UserResponseDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserResponseDTO createdBy) {
        this.createdBy = createdBy;
    }

    public List<UserResponseDTO> getMembers() {
        return members;
    }

    public void setMembers(List<UserResponseDTO> members) {
        this.members = members;
    }
}
