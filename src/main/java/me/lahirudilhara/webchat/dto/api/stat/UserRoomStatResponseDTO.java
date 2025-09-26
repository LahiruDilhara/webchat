package me.lahirudilhara.webchat.dto.api.stat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.lahirudilhara.webchat.dto.api.room.RoomResponseDTO;

import java.time.Instant;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoomStatResponseDTO extends RoomResponseDTO {
    private Integer unreadMessagesCount;
    private Instant lastAccessedAt;
    private Integer memberCount;
    private List<UserStatResponseDTO> userStats;
}
