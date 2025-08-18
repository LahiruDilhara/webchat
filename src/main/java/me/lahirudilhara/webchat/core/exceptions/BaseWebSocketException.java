package me.lahirudilhara.webchat.core.exceptions;

public class BaseWebSocketException extends  RuntimeException{

    public BaseWebSocketException(String error) {
        super(error);
    }
}
