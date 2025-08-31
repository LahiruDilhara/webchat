package me.lahirudilhara.webchat.common.exceptions;

public class BaseWebSocketException extends  RuntimeException{

    public BaseWebSocketException(String error) {
        super(error);
    }
}
