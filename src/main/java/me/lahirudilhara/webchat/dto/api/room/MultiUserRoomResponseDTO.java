package me.lahirudilhara.webchat.dto.api.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiUserRoomResponseDTO extends RoomResponseDTO{
    private Boolean closed;
    private Integer memberCount;
    private String type = "MultiUserRoom";
}
