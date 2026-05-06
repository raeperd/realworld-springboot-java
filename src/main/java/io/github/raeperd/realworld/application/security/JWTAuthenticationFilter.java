package io.github.raeperd.realworld.application.security;

import io.github.raeperd.realworld.domain.jwt.JWTDeserializer;
import io.github.raeperd.realworld.domain.jwt.JWTPayload;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Collections.singleton;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTDeserializer jwtDeserializer;

    JWTAuthenticationFilter(JWTDeserializer jwtDeserializer) {
        this.jwtDeserializer = jwtDeserializer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ofNullable(request.getHeader(AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith("Token "))
                .map(authHeader -> authHeader.substring("Token ".length()))
                .map(token -> new JWT(token, jwtDeserializer.jwtPayloadFromJWT(token)))
                .ifPresent(getContext()::setAuthentication);
        filterChain.doFilter(request, response);
    }

    @SuppressWarnings("java:S2160")
    static class JWT extends AbstractAuthenticationToken {

        private final String token;
        private final JWTPayload jwtPayload;

        private JWT(String token, JWTPayload jwtPayload) {
            super(singleton(new SimpleGrantedAuthority("USER")));
            this.token = token;
            this.jwtPayload = jwtPayload;
            setAuthenticated(true);
        }

        @Override
        public Object getPrincipal() {
            return jwtPayload;
        }

        @Override
        public Object getCredentials() {
            return token;
        }
    }
}
