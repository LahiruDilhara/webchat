package me.lahirudilhara.webchat.models.message;

import jakarta.persistence.Entity;

@Entity(name = "fileMessage")
public class FileMessage extends BaseMessage {
    private String fileType;

}
