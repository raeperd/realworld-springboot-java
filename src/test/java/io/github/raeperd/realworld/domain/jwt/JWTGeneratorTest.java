package io.github.raeperd.realworld.domain.jwt;

import io.github.raeperd.realworld.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTGeneratorTest {

    private static final String SECRET = "SOME_SECRET";

    private final JWTGenerator jwtGenerator = new HS256JWTService(SECRET, 1000);

    @Mock
    private User user;

    @BeforeEach
    void initializeUser() {
        when(user.getId()).thenReturn(1L);
        when(user.getEmail()).thenReturn("user@email.com");
    }

    @Test
    void when_generateToken_expect_result_startsWith_encodedHeader() {
        final var token = jwtGenerator.generateTokenFromUser(user);

        assertThat(token).startsWith(Base64URL.encodeFromString("{\"alg\":\"HS256\",\"type\":\"JWT\"}"));
    }

    @Test
    void when_generateToken_return_value_can_be_verified() {
        final var token = jwtGenerator.generateTokenFromUser(user);
        final var indexOfSignature = token.lastIndexOf('.') + 1;

        final var message = token.substring(0, indexOfSignature - 1);
        final var signature = token.substring(indexOfSignature);

        assertThat(Base64URL.encodeFromBytes(HmacSHA256.sign(SECRET.getBytes(StandardCharsets.UTF_8), message)))
                .isEqualTo(signature);
    }
}