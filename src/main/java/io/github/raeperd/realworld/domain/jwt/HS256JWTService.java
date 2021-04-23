package io.github.raeperd.realworld.domain.jwt;

import io.github.raeperd.realworld.domain.User;

import java.nio.charset.StandardCharsets;

import static java.time.Instant.now;

class HS256JWTService implements JWTService {

    private static final String BASE64URL_ENCODED_HEADER = Base64URL.encodeFromString("{\"alg\":\"HS256\",\"type\":\"JWT\"}");

    private final byte[] secret;
    private final long durationSeconds;

    HS256JWTService(String secret, long durationSeconds) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.durationSeconds = durationSeconds;
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

}
