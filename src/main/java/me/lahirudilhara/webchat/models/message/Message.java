package me.lahirudilhara.webchat.models.message;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "message")
@Inheritance(strategy = InheritanceType.JOINED)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Instant createdAt;

    private Boolean deleted = false;

    @ManyToOne()
    @JoinColumn(name = "messages",nullable = false)
    private User sender;

    @ManyToOne()
    @JoinColumn(name = "room",nullable = false)
    private Room room;

}
