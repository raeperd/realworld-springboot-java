package io.github.raeperd.realworld.application;

import io.github.raeperd.realworld.domain.User;
import lombok.Getter;

@Getter
public class UserLoginRequestDTO {

    private final String email;
    private final String password;

    public UserLoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User toUser() {
        return User.fromEmailAndPassword(email, password);
    }
}
