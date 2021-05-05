package io.github.raeperd.realworld.application.security;

import io.github.raeperd.realworld.application.security.JWTAuthenticationFilter.JWTAuthenticationToken;
import io.github.raeperd.realworld.domain.jwt.JWTPayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JWTAuthenticationFilterTest {

    @Test
    void when_get_principal_expect_return_jwtPayload(@Mock JWTPayload jwtPayload) {
        final var jwtAuthenticationToken = new JWTAuthenticationToken(jwtPayload);

        assertThat(jwtAuthenticationToken.getPrincipal()).isEqualTo(jwtPayload);
    }

    @Test
    void when_get_credentials_expect_return_null(@Mock JWTPayload jwtPayload) {
        final var jwtAuthenticationToken = new JWTAuthenticationToken(jwtPayload);

        assertThat(jwtAuthenticationToken.getCredentials()).isNull();
    }
}