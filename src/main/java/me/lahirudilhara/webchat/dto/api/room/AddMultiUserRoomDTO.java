package me.lahirudilhara.webchat.dto.api.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMultiUserRoomDTO {
    @NotNull(message = "isPrivate is required")
    private Boolean isPrivate;

    @NotBlank(message = "The room name cannot be blank")
    private String name;
}
