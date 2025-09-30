package me.lahirudilhara.webchat.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextMessageResponseDTO extends MessageResponseDTO {
    private String content;
    private Instant editedAt;
    private final String type = "TextMessage";
}
