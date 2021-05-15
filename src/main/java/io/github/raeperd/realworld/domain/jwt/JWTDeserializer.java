package io.github.raeperd.realworld.domain.jwt;

public interface JWTDeserializer {

    JWTPayload jwtPayloadFromJWT(String jwtToken);

}
