package me.lahirudilhara.webchat.models.message;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "textMessage")
public class TextMessage extends Message {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    private Instant editedAt;
}
