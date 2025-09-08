package me.lahirudilhara.webchat.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private Integer id;
    private String username;
    private String password;
}
