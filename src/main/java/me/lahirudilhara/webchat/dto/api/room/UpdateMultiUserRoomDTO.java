package me.lahirudilhara.webchat.dto.api.room;


import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateMultiUserRoomDTO {
    private Boolean isPrivate;

    @Size(min = 4, max = 100,message = "The name must be between 4 and 100 character length")
    private String name;

    private Boolean closed;
}
