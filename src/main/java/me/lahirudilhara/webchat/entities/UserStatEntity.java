package me.lahirudilhara.webchat.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatEntity {
    private String username;
    private Instant lastSeenAt;
}
