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
public class RoomDetailsResponseDTO {
    private Integer id;
    private String name;
    private Instant createdAt;
    private String createdBy;
    private Integer unreadMessagesCount;
    private List<UserResponseDTO> roomMembers;
}
