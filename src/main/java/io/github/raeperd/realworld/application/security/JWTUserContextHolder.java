package io.github.raeperd.realworld.application.security;

import io.github.raeperd.realworld.application.security.JWTAuthenticationFilter.JWTAuthenticationToken;
import io.github.raeperd.realworld.domain.User;
import io.github.raeperd.realworld.domain.UserContextHolder;
import io.github.raeperd.realworld.domain.UserRepository;
import io.github.raeperd.realworld.domain.jwt.JWTPayload;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

class JWTUserContextHolder implements UserContextHolder {

    private final UserRepository userRepository;

    JWTUserContextHolder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getCurrentUser() {
        return ofNullable(getContext().getAuthentication())
                .map(JWTAuthenticationToken.class::cast)
                .map(JWTAuthenticationToken::getPrincipal)
                .map(JWTPayload.class::cast)
                .map(JWTPayload::getUserId)
                .flatMap(userRepository::findById);
    }

}
