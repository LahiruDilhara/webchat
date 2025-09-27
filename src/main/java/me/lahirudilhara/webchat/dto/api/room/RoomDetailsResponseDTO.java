package me.lahirudilhara.webchat.dto.api.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.lahirudilhara.webchat.dto.api.user.UserResponseDTO;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailsResponseDTO extends RoomResponseDTO {
    private Integer unreadMessagesCount;
    private List<UserResponseDTO> roomMembers;
}
