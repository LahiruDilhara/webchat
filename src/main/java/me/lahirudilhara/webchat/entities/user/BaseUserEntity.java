package me.lahirudilhara.webchat.entities.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserEntity {
    private Integer id;
    private String username;
    private String password;
}
