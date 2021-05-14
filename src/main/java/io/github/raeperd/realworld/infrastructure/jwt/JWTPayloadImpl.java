package io.github.raeperd.realworld.infrastructure.jwt;

import io.github.raeperd.realworld.domain.jwt.JWTPayload;
import io.github.raeperd.realworld.domain.user.Email;
import io.github.raeperd.realworld.domain.user.User;

import static java.lang.String.format;
import static java.time.Instant.now;

class JWTPayloadImpl implements JWTPayload {

    private final long sub;
    private final Email name;
    private final long iat;

    JWTPayloadImpl(User user, long epochSecondExpired) {
        this.sub = user.getId();
        this.name = user.getEmail();
        this.iat = epochSecondExpired;
    }

    @Override
    public long getUserId() {
        return sub;
    }

    @Override
    public boolean isExpired() {
        return iat < now().getEpochSecond();
    }

    @Override
    public String toString() {
        return format("{\"sub\":%d,\"name\":\"%s\",\"iat\":%d}", sub, name, iat);
    }
}
