package me.lahirudilhara.webchat.dto.api.room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddDualUserRoomDTO {
    @NotBlank(message = "The room name cannot be blank")
    private String name;

    @NotNull(message = "The userId cannot be null")
    @Min(value = 1,message = "The userId should be more than 0")
    private Integer userId;

    public AddDualUserRoomDTO() {
    }

    public AddDualUserRoomDTO(String name, Integer userId) {
        this.name = name;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
