package me.lahirudilhara.webchat.dto.api.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDTO {
    private Integer id;
    private String name;
    private Instant createdAt;
    private Integer memberCount;
    private String type = "Room";
}
