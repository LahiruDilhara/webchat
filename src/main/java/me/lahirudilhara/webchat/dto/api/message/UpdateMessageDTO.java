package me.lahirudilhara.webchat.dto.api.message;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMessageDTO {
    @NotNull(message = "message is required")
    private String message;

    @NotNull(message = "The room id cannot be null")
    @Min(value = 0)
    private Integer roomId;
}
