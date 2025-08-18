package me.lahirudilhara.webchat.models;

import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private String content;

    private boolean deleted = false;

    private Instant editedAt;

    @ManyToOne
    @JoinColumn(name = "messages")
    private User sentBy;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;

    public Message() {
    }

    public Message(Integer id, Instant createdAt, String content, boolean deleted, Instant editedAt, User sentBy, Room room) {
        this.id = id;
        this.createdAt = createdAt;
        this.content = content;
        this.deleted = deleted;
        this.editedAt = editedAt;
        this.sentBy = sentBy;
        this.room = room;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(Instant editedAt) {
        this.editedAt = editedAt;
    }

    public User getSentBy() {
        return sentBy;
    }

    public void setSentBy(User sentBy) {
        this.sentBy = sentBy;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
