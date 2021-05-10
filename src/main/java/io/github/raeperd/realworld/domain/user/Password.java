package io.github.raeperd.realworld.domain.user;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Embeddable;

@Embeddable
class Password {

    private String encodedPassword;

    static Password of(String rawPassword, PasswordEncoder passwordEncoder) {
        return new Password(passwordEncoder.encode(rawPassword));
    }

    private Password(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    protected Password() {
    }

    boolean matches(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
