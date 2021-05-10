package io.github.raeperd.realworld.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PasswordTest {

    @Test
    void when_create_password_expect_to_encode(@Mock PasswordEncoder passwordEncoder) {
        final var rawPassword = "rawPassword";

        Password.of(rawPassword, passwordEncoder);

        then(passwordEncoder).should(times(1)).encode(rawPassword);
    }

    @Test
    void when_matches_with_same_password_encoder_expect_return_true() {
        final var passwordEncoder = new BCryptPasswordEncoder();
        final var rawPassword = "rawString";
        final var password = Password.of(rawPassword, passwordEncoder);

        assertThat(password.matches(rawPassword, passwordEncoder)).isTrue();
    }
}