package me.lahirudilhara.webchat.dto;

import jakarta.validation.constraints.NotBlank;

public class AddRoomDto {
    private boolean isPrivate;

    @NotBlank(message = "The room name cannot be blank")
    private String name;
}
