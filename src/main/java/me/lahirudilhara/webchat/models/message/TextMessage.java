package me.lahirudilhara.webchat.models.message;

import jakarta.persistence.Entity;
import me.lahirudilhara.webchat.models.Room;
import me.lahirudilhara.webchat.models.User;

import java.time.Instant;

@Entity(name = "textMessage")
public class TextMessage extends BaseMessage{
    private String content;
    private Instant editedAt;

    public TextMessage() {
    }

    public TextMessage(Integer id, Instant createdAt, boolean deleted, User sender, Room room, String content, Instant editedAt) {
        super(id, createdAt, deleted, sender, room);
        this.content = content;
        this.editedAt = editedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(Instant editedAt) {
        this.editedAt = editedAt;
    }
}
