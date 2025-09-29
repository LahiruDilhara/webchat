package me.lahirudilhara.webchat.dto.api.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DualUserRoomResponseDTO extends RoomResponseDTO {
    private String user1Name;
    private String user2Name;
    private String type = "DualUserRoom";
}
