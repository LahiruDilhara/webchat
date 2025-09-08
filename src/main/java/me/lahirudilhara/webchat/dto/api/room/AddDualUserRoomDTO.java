package me.lahirudilhara.webchat.dto.api.room;

import jakarta.validation.constraints.Min;
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

    @NotNull(message = "The userId cannot be null")
    @Min(value = 1,message = "The userId should be more than 0")
    private Integer userId;
}
