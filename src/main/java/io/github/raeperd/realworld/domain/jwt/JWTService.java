package io.github.raeperd.realworld.domain.jwt;

import io.github.raeperd.realworld.domain.User;

public interface JWTService {

    String generateTokenFromUser(User user);

}
