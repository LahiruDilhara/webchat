package me.lahirudilhara.webchat.entities.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lahirudilhara.webchat.entities.user.UserEntity;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomEntity {
    private Integer id;
    private String name;
    private Instant createdAt;
    private String createdBy;
}
