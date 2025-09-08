package me.lahirudilhara.webchat.dto.wc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lahirudilhara.webchat.dto.message.TextMessageResponseDTO;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextMessageAckResponseDTO extends TextMessageResponseDTO {
    private String uuid;
}
