package me.lahirudilhara.webchat.core.exceptions;

import org.springframework.http.HttpStatus;

public class RoomNotFoundException extends BaseException{
    public RoomNotFoundException() {
        super("The room not found", HttpStatus.NOT_FOUND);
    }
}
