package me.lahirudilhara.webchat.common.types;

public class Failure {
    private String message;

    public Failure(String message) {
        this.message = message;
    }

    public Failure(String message, int code) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
