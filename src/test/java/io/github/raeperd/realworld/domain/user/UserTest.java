package io.github.raeperd.realworld.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void when_matches_password_expect_password_matches_password(@Mock Email email, @Mock UserName userName, @Mock Password password) {
        final var user = new User(email, userName, password);

        user.matchesPassword("some-password", passwordEncoder);

        verify(password, times(1)).matchesPassword("some-password", passwordEncoder);
    }
}