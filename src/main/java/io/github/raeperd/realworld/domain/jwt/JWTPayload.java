package io.github.raeperd.realworld.domain.jwt;

import io.github.raeperd.realworld.domain.user.User;

import static java.lang.String.format;
import static java.time.Instant.now;

public class JWTPayload {

    private final long sub;
    private final String name;
    private final long iat;

    static JWTPayload fromUser(User user, long expireEpochSecond) {
        return new JWTPayload(user.getId(), user.getEmail(), expireEpochSecond);
    }

    JWTPayload(long sub, String name, long iat) {
        this.sub = sub;
        this.name = name;
        this.iat = iat;
    }

    boolean isExpired() {
        return iat < now().getEpochSecond();
    }

    public long getUserId() {
        return sub;
    }

    @Override
    public String toString() {
        return format("{\"sub\":%d,\"name\":\"%s\",\"iat\":%d}", sub, name, iat);
    }
}
