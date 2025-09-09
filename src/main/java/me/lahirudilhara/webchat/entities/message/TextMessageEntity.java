package me.lahirudilhara.webchat.entities.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextMessageEntity extends MessageEntity{
    private String content;
    private Instant editedAt;
}
