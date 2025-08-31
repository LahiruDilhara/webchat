package me.lahirudilhara.webchat.common.types;

public class Failure {
    private String message;
    private int code;

    public Failure(String message) {
        this.message = message;
        this.code = 500;
    }

    public Failure(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
