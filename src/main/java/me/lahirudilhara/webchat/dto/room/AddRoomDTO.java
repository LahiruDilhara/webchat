package me.lahirudilhara.webchat.dto.room;

import jakarta.validation.constraints.NotBlank;

public class AddRoomDTO {
    private boolean isPrivate;

    @NotBlank(message = "The room name cannot be blank")
    private String name;
}
