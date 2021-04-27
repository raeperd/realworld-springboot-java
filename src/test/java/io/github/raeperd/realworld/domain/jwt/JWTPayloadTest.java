package io.github.raeperd.realworld.domain.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;
import static org.assertj.core.api.Assertions.assertThat;

class JWTPayloadTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new ParameterNamesModule(PROPERTIES));

    @Test
    void when_readValue_with_toString_value_expect_same() throws JsonProcessingException {
        final var jwtPayload = new JWTPayload(1L, "some@email.com", 10L);

        assertThat(objectMapper.readValue(jwtPayload.toString(), JWTPayload.class))
                .hasToString(jwtPayload.toString());
    }
}