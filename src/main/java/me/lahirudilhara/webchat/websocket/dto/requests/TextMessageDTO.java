package me.lahirudilhara.webchat.websocket.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextMessageDTO extends MessageDTO {
    @NotNull(message = "The message cannot be null")
    @NotEmpty(message = "The message cannot be empty")
    @Size(min = 1, max = 4000,message = "The message size should be between 1 and 4000")
    private String message;
}