package me.lahirudilhara.webchat.dto.api.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddMultiUserRoomDTO {
    @NotNull(message = "isPrivate is required")
    private Boolean isPrivate;

    @NotBlank(message = "The room name cannot be blank")
    private String name;

    public AddMultiUserRoomDTO() {
    }

    public AddMultiUserRoomDTO(Boolean isPrivate, String name) {
        this.isPrivate = isPrivate;
        this.name = name;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
