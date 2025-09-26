package me.lahirudilhara.webchat.dto.api.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoomStatResponseDTO extends RoomResponseDTO {
    private Integer unreadMessagesCount;
    private Instant lastAccessedAt;
    private Integer memberCount;
}
