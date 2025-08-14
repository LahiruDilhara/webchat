package me.lahirudilhara.webchat.core.exceptions;

import org.springframework.http.HttpStatus;

public class CredentialsFailedException extends BaseException{

    public CredentialsFailedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
