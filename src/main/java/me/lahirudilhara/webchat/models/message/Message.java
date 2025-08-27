package me.lahirudilhara.webchat.models.message;

import jakarta.persistence.*;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;

import java.time.Instant;

@Entity(name = "message")
@Inheritance(strategy = InheritanceType.JOINED)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Instant createdAt;

    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "messages")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;

    public Message() {
    }

    public Message(Integer id, Instant createdAt, boolean deleted, User sender, Room room) {
        this.id = id;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.sender = sender;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
