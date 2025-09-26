package me.lahirudilhara.webchat.dto.api.stat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatResponseDTO {
    private String username;
    private Instant lastSeenAt;
}
