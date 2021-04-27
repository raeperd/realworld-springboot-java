package io.github.raeperd.realworld.domain.jwt;

public interface JWTParser {

    JWTPayload validateToken(String token);

}
