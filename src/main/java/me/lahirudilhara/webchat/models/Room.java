package me.lahirudilhara.webchat.models;

import jakarta.persistence.*;
import me.lahirudilhara.webchat.models.message.Message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private boolean isPrivate;

    private Instant createdAt = Instant.now();

    private boolean closed = false;

    private boolean multiUser;

    @ManyToOne
    @JoinColumn(name = "createdRooms")
    private User createdBy;

    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "connect",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();

    public Room(Integer id, String name, boolean isPrivate, User createdBy,boolean multiUser) {
        this.id = id;
        this.name = name;
        this.isPrivate = isPrivate;
        this.createdBy = createdBy;
        this.multiUser = multiUser;
    }

    public Room(){}

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

    public boolean getClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean getMultiUser() {
        return multiUser;
    }

    public void setMultiUser(boolean multiUser) {
        this.multiUser = multiUser;
    }

    public boolean isAcceptMessages(){
        return true;
    }
}
