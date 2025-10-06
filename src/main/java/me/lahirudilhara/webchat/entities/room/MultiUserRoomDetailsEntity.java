package me.lahirudilhara.webchat.entities.room;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MultiUserRoomDetailsEntity extends RoomDetailsEntity{
    private Boolean closed;
    private Boolean isPrivate;
}
