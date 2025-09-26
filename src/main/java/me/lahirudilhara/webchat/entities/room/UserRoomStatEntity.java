package me.lahirudilhara.webchat.entities.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.lahirudilhara.webchat.entities.UserStatEntity;

import java.time.Instant;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoomStatEntity extends RoomEntity {
    private Integer unreadMessagesCount;
    private Instant lastAccessedAt;
    private Integer memberCount;
    private List<UserStatEntity> memberStats;
}
