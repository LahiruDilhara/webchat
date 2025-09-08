package me.lahirudilhara.webchat.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextMessageResponseDTO extends MessageResponseDTO {
    private String content;
    private Instant editedAt;
}
