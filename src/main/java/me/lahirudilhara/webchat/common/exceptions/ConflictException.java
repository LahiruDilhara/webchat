package me.lahirudilhara.webchat.common.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException{
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
