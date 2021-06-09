package io.github.raeperd.realworld.application.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ofNullable(request.getHeader(AUTHORIZATION))
                .map(authHeader -> authHeader.substring("Token ".length()))
                .map(JWT::new)
                .ifPresent(getContext()::setAuthentication);
        filterChain.doFilter(request, response);
    }

    @SuppressWarnings("java:S2160")
    static class JWT extends AbstractAuthenticationToken {

        private final String token;

        private JWT(String token) {
            super(null);
            this.token = token;
        }

        @Override
        public Object getPrincipal() {
            return token;
        }

        @Override
        public Object getCredentials() {
            return null;
        }
    }
}
