package io.github.raeperd.realworld.domain.jwt;

public interface JWTPayload {

    long getUserId();
    boolean isExpired();

}
