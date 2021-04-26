package io.github.raeperd.realworld.domain.jwt;

public interface JWTParser {

    long parseSubjectFromToken(String token);
}
