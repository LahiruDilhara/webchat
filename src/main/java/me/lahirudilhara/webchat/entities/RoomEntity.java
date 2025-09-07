package me.lahirudilhara.webchat.entities;

import lombok.ToString;

import java.time.Instant;

@ToString
public class RoomEntity {
    private Integer id;
    private String name;
    private Boolean isPrivate;
    private Instant createdAt;
    private Boolean closed = false;
    private Boolean multiUser;
    private String createdBy;

    public RoomEntity() {
    }

    public RoomEntity(Integer id, String name, Boolean isPrivate, Instant createdAt, Boolean closed, Boolean multiUser, String createdBy) {
        this.id = id;
        this.name = name;
        this.isPrivate = isPrivate;
        this.createdAt = createdAt;
        this.closed = closed;
        this.multiUser = multiUser;
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

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Boolean getMultiUser() {
        return multiUser;
    }

    public void setMultiUser(Boolean multiUser) {
        this.multiUser = multiUser;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "RoomEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isPrivate=" + isPrivate +
                ", createdAt=" + createdAt +
                ", closed=" + closed +
                ", multiUser=" + multiUser +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
