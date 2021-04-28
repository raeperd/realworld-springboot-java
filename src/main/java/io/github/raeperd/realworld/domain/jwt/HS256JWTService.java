package io.github.raeperd.realworld.domain.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.github.raeperd.realworld.domain.User;
import io.github.raeperd.realworld.domain.jwt.exception.InvalidJWTException;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;
import static java.lang.String.format;
import static java.time.Instant.now;

class HS256JWTService implements JWTGenerator, JWTParser {

    private static final String BASE64URL_ENCODED_HEADER = Base64URL.encodeFromString("{\"alg\":\"HS256\",\"type\":\"JWT\"}");
    private static final String BASE64URL_PATTERN = "[\\w_\\-]+";
    private static final Pattern BASE64URL_TOKEN_PATTERN = Pattern.compile(format("^(%s\\.)(%s\\.)(%s)$",
            BASE64URL_PATTERN, BASE64URL_PATTERN, BASE64URL_PATTERN));

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

    @Override
    public JWTPayload validateToken(String token) {
        if (!BASE64URL_TOKEN_PATTERN.matcher(token).matches()) {
            throw new InvalidJWTException("Malformed token: " + token);
        }

        final var splicedTokens = token.split("\\.");
        if (!splicedTokens[0].equals(BASE64URL_ENCODED_HEADER)) {
            throw new InvalidJWTException("Token must starts with valid header");
        }

        final var signature = HmacSHA256.sign(secret, splicedTokens[0] + "." + splicedTokens[1]);
        if (!Base64URL.encodeFromBytes(signature).equals(splicedTokens[2])) {
            throw new InvalidJWTException("Token has invalid signature");
        }

        try {
            final var jwtPayload = objectMapper.readValue(
                    Base64URL.decodeFromString(splicedTokens[1]), JWTPayload.class);
            if (jwtPayload.isExpired()) {
                throw new InvalidJWTException("Token expired");
            }
            return jwtPayload;
        } catch (JsonProcessingException e) {
            throw new InvalidJWTException("Token has invalid payload: " + e.getMessage());
        }
    }
}
