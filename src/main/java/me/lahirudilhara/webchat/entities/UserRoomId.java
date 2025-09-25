package me.lahirudilhara.webchat.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserRoomId implements Serializable {
    private Integer userId;
    private Integer roomId;
}
