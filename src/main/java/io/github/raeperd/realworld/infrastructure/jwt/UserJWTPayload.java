package io.github.raeperd.realworld.infrastructure.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.raeperd.realworld.domain.jwt.JWTPayload;
import io.github.raeperd.realworld.domain.user.User;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.time.Instant.now;

public class UserJWTPayload implements JWTPayload {

    private final long sub;
    private final String name;
    private final long iat;

    static UserJWTPayload of(User user, long epochSecondExpired) {
        return new UserJWTPayload(user.getId(), valueOf(user.getEmail()), epochSecondExpired);
    }

    @JsonCreator
    UserJWTPayload(@JsonProperty("sub") long sub,
                   @JsonProperty("name") String name,
                   @JsonProperty("iat") long iat) {
        this.sub = sub;
        this.name = name;
        this.iat = iat;
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
