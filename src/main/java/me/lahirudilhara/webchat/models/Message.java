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

    private boolean deleted;

    @Column(nullable = false)
    private Instant editedAt;

    @ManyToOne
    @JoinColumn(name = "messages")
    private User sentBy;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;
}
