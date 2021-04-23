package io.github.raeperd.realworld.domain.jwt;

import org.junit.jupiter.api.Test;

import static io.github.raeperd.realworld.domain.jwt.Base64URL.encodeFromString;
import static org.assertj.core.api.Assertions.assertThat;

class Base64URLTest {

    @Test
    void when_encode_return_expected_string() {
        assertThat(encodeFromString("something")).isEqualTo("c29tZXRoaW5n");
    }
}