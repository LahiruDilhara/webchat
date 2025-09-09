package me.lahirudilhara.webchat.common.exceptions;

import org.springframework.http.HttpStatus;

public class MessageNotFoundException extends BaseException {
    public MessageNotFoundException() {
        super("The message not found", HttpStatus.NOT_FOUND);
    }
}
