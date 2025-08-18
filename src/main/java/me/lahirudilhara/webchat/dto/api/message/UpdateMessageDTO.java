package me.lahirudilhara.webchat.dto.api.message;

import jakarta.validation.constraints.NotNull;

public class UpdateMessageDTO {
    @NotNull(message = "message is required")
    private String message;

    public UpdateMessageDTO() {
    }

    public UpdateMessageDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
