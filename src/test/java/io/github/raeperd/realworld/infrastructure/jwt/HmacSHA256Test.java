package io.github.raeperd.realworld.infrastructure.jwt;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class HmacSHA256Test {

    @Test
    void when_invalid_secret_expect_throw_exception() {
        assertThatThrownBy(
                () -> HmacSHA256.sign(null, "test")
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void when_sign_expect_matched_return() {
        assertThat(HmacSHA256.sign("secret".getBytes(StandardCharsets.UTF_8), "plain"))
                .asHexString()
                .isEqualTo("A237566E044B73E6A1E54BD59974547487FA5F8143025CE0D04D82E7EE4C5E34");
    }
}