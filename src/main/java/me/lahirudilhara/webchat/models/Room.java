package me.lahirudilhara.webchat.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean isPrivate;

    private Instant createdAt;

    private boolean closed;

    @ManyToOne
    @JoinColumn(name = "createdRooms")
    private User createdBy;

    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL)
    private List<Message> messages;

    @ManyToMany
    @JoinTable(
            name = "connect",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();
}
