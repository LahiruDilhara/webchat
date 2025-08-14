package me.lahirudilhara.webchat.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "MyUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;
    private String password;

    @OneToMany(mappedBy = "sentBy",cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy",cascade = CascadeType.ALL)
    private List<Room> createdRooms = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    private List<Room> rooms = new ArrayList<>();

    public User(){}

    public User(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Room> getCreatedRooms() {
        return createdRooms;
    }

    public void setCreatedRooms(List<Room> createdRooms) {
        this.createdRooms = createdRooms;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
