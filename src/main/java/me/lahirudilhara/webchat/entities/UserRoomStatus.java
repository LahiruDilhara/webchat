package me.lahirudilhara.webchat.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "user_room_status")
public class UserRoomStatus {
    @EmbeddedId
    private UserRoomId userRoomId;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("roomId")
    private Room room;

    private Instant lastSeenAt;
}

