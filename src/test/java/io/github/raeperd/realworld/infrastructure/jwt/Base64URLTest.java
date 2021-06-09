package io.github.raeperd.realworld.infrastructure.jwt;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.github.raeperd.realworld.infrastructure.jwt.Base64URL.base64URLFromString;
import static io.github.raeperd.realworld.infrastructure.jwt.Base64URL.stringFromBase64URL;
import static org.assertj.core.api.Assertions.assertThat;

class Base64URLTest {

    private final static String RAW_STRING = "something";
    private final static String ENCODED_STRING = "c29tZXRoaW5n";

    @Test
    void when_encode_return_expected_string() {
        assertThat(base64URLFromString(RAW_STRING)).isEqualTo(ENCODED_STRING);
    }

    @Test
    void when_decode_return_expected_string() {
        assertThat(stringFromBase64URL(ENCODED_STRING)).isEqualTo(RAW_STRING);
    }

    @Test
    void when_encode_and_then_decode_expect_same() {
        final var rawString = generateRandomString();

        final var encodedString = base64URLFromString(rawString);
        assertThat(stringFromBase64URL(encodedString)).isEqualTo(rawString);
    }

    private String generateRandomString() {
        final var bytes = new byte[7];
        new Random().nextBytes(bytes);
        return new String(bytes);
    }

}