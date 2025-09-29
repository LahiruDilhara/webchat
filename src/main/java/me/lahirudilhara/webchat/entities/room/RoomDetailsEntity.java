package me.lahirudilhara.webchat.entities.room;

import lombok.*;
import me.lahirudilhara.webchat.entities.user.UserEntity;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RoomDetailsEntity extends RoomEntity {
    private Integer unreadMessagesCount;
    private List<UserEntity> roomMembers;
}
