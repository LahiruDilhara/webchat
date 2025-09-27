package me.lahirudilhara.webchat.entities;

import lombok.*;
import me.lahirudilhara.webchat.entities.user.BaseUserEntity;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseUserEntity {
    private Instant lastSeen;
}
