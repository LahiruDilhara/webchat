package me.lahirudilhara.webchat.dto.api.auth;

import jakarta.validation.constraints.NotBlank;

public class SignUpDTO {
    @NotBlank(message = "The username cannot be blank")
    private String username;

    @NotBlank(message = "The password cannot be blank")
    private String password;

    public SignUpDTO() {
    }

    public SignUpDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
