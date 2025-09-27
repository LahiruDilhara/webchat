package me.lahirudilhara.webchat.entities.user;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private Integer id;
    private String username;
    private String password;
    private Instant lastSeen;
}
