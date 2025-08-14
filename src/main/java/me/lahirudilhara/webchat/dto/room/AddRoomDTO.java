package me.lahirudilhara.webchat.dto.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddRoomDTO {
    @NotNull(message = "isPrivate is required")
    private Boolean isPrivate;

    @NotNull(message = "multiUser is required")
    private Boolean multiUser;

    @NotBlank(message = "The room name cannot be blank")
    private String name;

    public AddRoomDTO() {
    }

    public AddRoomDTO(Boolean isPrivate, Boolean multiUser, String name) {
        this.isPrivate = isPrivate;
        this.multiUser = multiUser;
        this.name = name;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Boolean getMultiUser() {
        return multiUser;
    }

    public void setMultiUser(Boolean multiUser) {
        this.multiUser = multiUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
