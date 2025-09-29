package me.lahirudilhara.webchat.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lahirudilhara.webchat.models.room.Room;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @MapsId("roomId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

    private Instant lastSeenAt;
}

