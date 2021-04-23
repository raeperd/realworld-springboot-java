package io.github.raeperd.realworld.domain.jwt;

import io.github.raeperd.realworld.domain.User;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static io.github.raeperd.realworld.domain.User.createNewUser;
import static org.assertj.core.api.Assertions.assertThat;

class HS256JWTServiceTest {

    private static final String SECRET = "SOME_SECRET";

    private final JWTService jwtService = new HS256JWTService(SECRET, 1000);

    private final User user = createNewUser("user", "user@email.com", "password");

    @Test
    void when_generateToken_expect_result_startsWith_encodedHeader() {
        final var token = jwtService.generateTokenFromUser(user);

        assertThat(token).startsWith(Base64URL.encodeFromString("{\"alg\":\"HS256\",\"type\":\"JWT\"}"));
    }

    @Test
    void when_generateToken_return_value_can_be_verified() {
        final var token = jwtService.generateTokenFromUser(user);
        final var indexOfSignature = token.lastIndexOf('.') + 1;

        final var message = token.substring(0, indexOfSignature - 1);
        final var signature = token.substring(indexOfSignature);

        assertThat(Base64URL.encodeFromBytes(HmacSHA256.sign(SECRET.getBytes(StandardCharsets.UTF_8), message)))
                .isEqualTo(signature);
    }
}