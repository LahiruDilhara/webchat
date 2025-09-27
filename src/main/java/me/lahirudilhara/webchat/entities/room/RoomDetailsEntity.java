package me.lahirudilhara.webchat.entities.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.lahirudilhara.webchat.entities.UserStatEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;

import java.time.Instant;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailsEntity extends RoomEntity {
    private Integer unreadMessagesCount;
    private List<UserEntity> roomMembers;
}
