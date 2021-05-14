package io.github.raeperd.realworld.infrastructure.jwt;

import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static java.lang.String.format;
import static java.time.Instant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTPayloadImplTest {

    @Test
    void when_getUserId_expect_return_user_id(@Mock User user) {
        when(user.getId()).thenReturn(1L);
        final var jwtPayload = new JWTPayloadImpl(user, now().getEpochSecond());

        assertThat(jwtPayload.getUserId()).isOne();
    }

    @Test
    void when_expired_expect_isExpired_return_true(@Mock User user) {
        final var jwtPayload = new JWTPayloadImpl(user, MIN.getEpochSecond());

        assertThat(jwtPayload.isExpired()).isTrue();
    }

    @Test
    void when_not_expired_expect_isExpired_return_false(@Mock User user) {
        final var jwtPayload = new JWTPayloadImpl(user, MAX.getEpochSecond());

        assertThat(jwtPayload.isExpired()).isFalse();
    }

    @Test
    void when_to_string_expect_return_shortest_json_string() {
        final var sampleUser = sampleUserWithIdAndEmail(2L, "user@email.com");
        final var jwtPayload = new JWTPayloadImpl(sampleUser, MAX.getEpochSecond());

        assertThat(jwtPayload)
                .hasToString(format("{\"sub\":%d,\"name\":\"%s\",\"iat\":%d}",
                        2L, "user@email.com", MAX.getEpochSecond()));
    }

    private User sampleUserWithIdAndEmail(long id, String email) {
        final var sampleUser = UserTestUtils.userWithEmailAndName(email, "name");
        ReflectionTestUtils.setField(sampleUser, "id", id);
        return sampleUser;
    }
}