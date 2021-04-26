package io.github.raeperd.realworld.domain.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.github.raeperd.realworld.domain.User;

import java.nio.charset.StandardCharsets;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;
import static java.time.Instant.now;

class HS256JWTService implements JWTGenerator, JWTParser {

    private static final String BASE64URL_ENCODED_HEADER = Base64URL.encodeFromString("{\"alg\":\"HS256\",\"type\":\"JWT\"}");

    private final byte[] secret;
    private final long durationSeconds;
    private final ObjectMapper objectMapper;

    HS256JWTService(String secret, long durationSeconds) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.durationSeconds = durationSeconds;
        this.objectMapper = new ObjectMapper().registerModule(new ParameterNamesModule(PROPERTIES));
    }

    @Override
    public String generateTokenFromUser(User user) {
        final var messageToSign = BASE64URL_ENCODED_HEADER + "." + base64EncodedPayLoadFromUser(user);
        final var signature = HmacSHA256.sign(secret, messageToSign);
        return messageToSign + "." + Base64URL.encodeFromBytes(signature);
    }

    private String base64EncodedPayLoadFromUser(User user) {
        return Base64URL.encodeFromString(
                JWTPayload.fromUser(user, now().getEpochSecond() + durationSeconds).toString());
    }

    // TODO
    @Override
    public long parseSubjectFromToken(String token) {
        return 0;
    }

}
