package me.lahirudilhara.webchat.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomEntity {
    private Integer id;
    private String name;
    private Boolean isPrivate;
    private Instant createdAt;
    private Boolean closed = false;
    private Boolean multiUser;
    private String createdBy;
}
