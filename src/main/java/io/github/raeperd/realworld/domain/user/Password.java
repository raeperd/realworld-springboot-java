package io.github.raeperd.realworld.domain.user;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
class Password {

    @Column(name = "password", nullable = false)
    private String encodedPassword;

    static Password of(String rawPassword, PasswordEncoder passwordEncoder) {
        return new Password(passwordEncoder.encode(rawPassword));
    }

    private Password(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    protected Password() {
    }

    boolean matchesPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
