package io.github.raeperd.realworld.domain.jwt;

import io.github.raeperd.realworld.domain.User;
import io.github.raeperd.realworld.domain.jwt.exception.InvalidJWTException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTParserTest {

    private static final String HEADER_EXPECTED = Base64URL.encodeFromString("{\"alg\":\"HS256\",\"type\":\"JWT\"}");
    private static final String SOME_SECRET = "SOME_SECRET";

    private final JWTParser jwtParser = new HS256JWTService(SOME_SECRET, 0);

    private static final Pattern TOKEN_PATTERN = Pattern.compile("^([\\w_\\-]+\\.)([\\w_\\-]+\\.)([\\w_\\-]+)$");

    @Test
    void when_find_expect_true() {
        final var matcher = TOKEN_PATTERN.matcher("HEADER_bas64-._base64-payload.SIGN");
        assertThat(matcher.matches()).isTrue();
    }

    @Test
    void when_validateToken_with_malformed_token_expect_InvalidJWTException() {
        assertThatThrownBy(() ->
                jwtParser.validateToken("MALFORMED_TOKEN-2-_without_dot")
        ).isInstanceOf(InvalidJWTException.class).hasMessageStartingWith("Malformed token");
    }

    @Test
    void when_token_not_starts_with_header_expect_InvalidJWTException() {
        assertThatThrownBy(() ->
                jwtParser.validateToken("HEADER_bas64-._base64-payload.SIGN")
        ).isInstanceOf(InvalidJWTException.class).hasMessageStartingWith("Token must starts with");
    }

    @Test
    void when_token_has_invalid_sign_expect_InvalidJWTException() {
        assertThatThrownBy(() ->
                jwtParser.validateToken(HEADER_EXPECTED + "._base64-payload.INVALID_1_SIGN")
        ).isInstanceOf(InvalidJWTException.class).hasMessageStartingWith("Token has invalid signature");
    }

    @Test
    void when_token_has_invalid_payload_expect_InvalidJWTException(@Mock User user) {
        final var invalidPayloadToken = generateInvalidPayloadToken();

        assertThatThrownBy(() ->
                jwtParser.validateToken(invalidPayloadToken)
        ).isInstanceOf(InvalidJWTException.class).hasMessageStartingWith("Token has invalid payload");
    }

    private String generateInvalidPayloadToken() {
        final var message = HEADER_EXPECTED + ".INVALID_PAYLOAD";
        final var signature = Base64URL.encodeFromBytes(
                HmacSHA256.sign(SOME_SECRET.getBytes(StandardCharsets.UTF_8), message));
        return message + "." + signature;
    }

    @Test
    void when_token_has_expired_expect_InvalidJWTException(@Mock User user) {
        when(user.getId()).thenReturn(1L);
        when(user.getEmail()).thenReturn("user@email.com");
        final var expiredToken = generateExpiredToken(user);

        assertThatThrownBy(() ->
                jwtParser.validateToken(expiredToken)
        ).isInstanceOf(InvalidJWTException.class).hasMessageStartingWith("Token expired");
    }

    private String generateExpiredToken(User user) {
        return generateToken(user, -1);
    }

    private String generateToken(User user, long durationSeconds) {
        return new HS256JWTService("SOME_SECRET", durationSeconds)
                .generateTokenFromUser(user);
    }

    @Test
    void when_valid_token_expect_return_jwtPayload(@Mock User user) {
        when(user.getId()).thenReturn(1L);
        when(user.getEmail()).thenReturn("user@email.com");
        final var token = generateToken(user, 2L);

        assertThat(jwtParser.validateToken(token))
                .extracting(JWTPayload::getSubject)
                .isEqualTo(1L);
    }
}
