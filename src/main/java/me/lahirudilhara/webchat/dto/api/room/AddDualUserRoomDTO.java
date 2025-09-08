package me.lahirudilhara.webchat.dto.api.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddDualUserRoomDTO {
    @NotBlank(message = "The room name cannot be blank")
    private String name;

    @NotBlank(message = "The addingUsername cannot be blank")
    @NotNull(message = "The addingUsername cannot be null")
    private String addingUsername;
}
