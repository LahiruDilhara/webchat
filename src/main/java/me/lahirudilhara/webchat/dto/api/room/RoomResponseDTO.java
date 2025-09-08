package me.lahirudilhara.webchat.dto.api.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lahirudilhara.webchat.dto.api.user.UserResponseDTO;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDTO {
    private Integer id;
    private String name;
    private Boolean isPrivate;
    private Instant createdAt;
    private Boolean closed;
    private String createdBy;
    private Boolean multiUser;
}
