package me.lahirudilhara.webchat.models.room;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Entity(name = "dual_user_room")
public class DualUserRoom extends Room{
}
