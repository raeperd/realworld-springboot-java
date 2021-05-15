package io.github.raeperd.realworld.infrastructure.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.nio.charset.StandardCharsets;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;
import static io.github.raeperd.realworld.domain.user.UserTestUtils.userWithIdAndEmail;
import static io.github.raeperd.realworld.infrastructure.jwt.Base64URL.base64URLFromBytes;
import static io.github.raeperd.realworld.infrastructure.jwt.Base64URL.base64URLFromString;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@JsonTest
class HmacSHA256JWTServiceTest {

    private static final String JWT_HEADER_EXPECTED = base64URLFromString("{\"alg\":\"HS256\",\"type\":\"JWT\"}");
    private static final byte[] SECRET = "SOME_SECRET".getBytes(StandardCharsets.UTF_8);

    private HmacSHA256JWTService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void initializeService() {
        objectMapper.registerModule(new ParameterNamesModule(PROPERTIES));
        this.jwtService = new HmacSHA256JWTService(SECRET, 3, objectMapper);
    }

    @Test
    void when_generateToken_expect_result_startsWith_encodedHeader() {
        final var user = userWithIdAndEmail(1L, "user@email.com");

        final var token = jwtService.jwtFromUser(user);

        assertThat(token).startsWith(base64URLFromString("{\"alg\":\"HS256\",\"type\":\"JWT\"}"));
    }

    @Test
    void when_JWTPayloadFromString_with_malformed_token_expect_IllegalArgumentException() {
        assertThatThrownBy(() ->
                jwtService.jwtPayloadFromJWT("MALFORMED_TOKEN-2-_without_dot")
        ).isInstanceOf(IllegalArgumentException.class).hasMessageStartingWith("Malformed JWT:");
    }

    @Test
    void when_JWTPayloadFromString_with_not_starts_with_header_expect_IllegalArgumentException() {
        assertThatThrownBy(() ->
                jwtService.jwtPayloadFromJWT("HEADER_bas64-._base64-payload.SIGN")
        ).isInstanceOf(IllegalArgumentException.class).hasMessageStartingWith("Malformed JWT! Token must starts with header");
    }

    @Test
    void when_JWTPayloadFromString_with_invalid_sign_expect_IllegalArgumentException() {
        assertThatThrownBy(() ->
                jwtService.jwtPayloadFromJWT(JWT_HEADER_EXPECTED + "._base64-payload.INVALID_1_SIGN")
        ).isInstanceOf(IllegalArgumentException.class).hasMessageStartingWith("Token has invalid signature");
    }

    @Test
    void when_JWTPayloadFromString_with_invalid_payload_expect_IllegalArgumentException() {
        final var invalidPayloadToken = invalidPayloadToken();

        assertThatThrownBy(() ->
                jwtService.jwtPayloadFromJWT(invalidPayloadToken)
        ).isInstanceOf(IllegalArgumentException.class).hasMessageStartingWith("Malformed JWT");
    }

    @Test
    void when_JWTPayloadFromString_token_has_expired_expect_InvalidJWTException() {
        final var expiredToken = expiredToken();

        assertThatThrownBy(() ->
                jwtService.jwtPayloadFromJWT(expiredToken)
        ).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Token expired");
    }

    @Test
    void when_JWTPayloadFromString_with_valid_token_expect_return_valid() {
        final var user = userWithIdAndEmail(1L, "user@email.com");

        final var token = jwtService.jwtFromUser(user);
        final var payloadFromToken = jwtService.jwtPayloadFromJWT(token);

        assertThat(payloadFromToken)
                .matches(payload -> !payload.isExpired())
                .matches(payload -> payload.getUserId() == 1L)
                .matches(payload -> valueOf(payload).startsWith(format("{\"sub\":%d,\"name\":\"%s\",", 1L, user.getEmail())));
    }

    private String invalidPayloadToken() {
        final var message = format("%s.%s", JWT_HEADER_EXPECTED, "INVALID_PAYLOAD");
        return base64URLFromBytes(HmacSHA256.sign(SECRET, message));
    }

    private String expiredToken() {
        final var user = userWithIdAndEmail(1L, "user@email.com");
        return new HmacSHA256JWTService(SECRET, -1, new ObjectMapper())
                .jwtFromUser(user);
    }

}