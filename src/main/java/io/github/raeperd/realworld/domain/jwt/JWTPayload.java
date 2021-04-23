package io.github.raeperd.realworld.domain.jwt;

import io.github.raeperd.realworld.domain.User;

import static java.lang.String.format;

class JWTPayload {

    private final long sub;
    private final String name;
    private final long iat;

    static JWTPayload fromUser(User user, long expireEpochSecond) {
        return new JWTPayload(user.getId(), user.getUsername(), expireEpochSecond);
    }

    JWTPayload(long sub, String name, long iat) {
        this.sub = sub;
        this.name = name;
        this.iat = iat;
    }

    @Override
    public String toString() {
        return format("{\"sub\":%d,\"name\":\"%s\",\"iat\":%d}", sub, name, iat);
    }
}
