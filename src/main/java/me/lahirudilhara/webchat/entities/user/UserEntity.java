package me.lahirudilhara.webchat.entities.user;

import lombok.*;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseUserEntity {
    private Instant lastSeen;
}
