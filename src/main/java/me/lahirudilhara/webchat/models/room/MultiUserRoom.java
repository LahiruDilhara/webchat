package me.lahirudilhara.webchat.models.room;

import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity(name = "multi_user_room")
public class MultiUserRoom extends Room{
    private Boolean closed;
    private Boolean isPrivate;
}
