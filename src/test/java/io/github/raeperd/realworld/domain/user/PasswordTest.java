package io.github.raeperd.realworld.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PasswordTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void when_create_password_expect_passwordEncoder_encode() {
        Password.of("raw-password", passwordEncoder);

        verify(passwordEncoder, times(1)).encode("raw-password");
    }

    @Test
    void when_matches_password_expect_passwordEncoder_matches() {
        given(passwordEncoder.encode("raw-password")).willReturn("encoded-password");

        final var password = Password.of("raw-password", passwordEncoder);
        password.matchesPassword("raw-password", passwordEncoder);

        then(passwordEncoder).should(times(1)).matches("raw-password", "encoded-password");
    }

}