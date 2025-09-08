package me.lahirudilhara.webchat.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.lahirudilhara.webchat.models.message.Message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Boolean isPrivate;

    private Instant createdAt;

    private Boolean closed = false;

    private Boolean multiUser;

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

    public boolean isAcceptMessages(){
        return true;
    }
}
