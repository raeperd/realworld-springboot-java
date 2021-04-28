package io.github.raeperd.realworld.application.security;

import io.github.raeperd.realworld.domain.jwt.JWTParser;
import io.github.raeperd.realworld.domain.jwt.JWTPayload;
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

    private final JWTParser jwtParser;

    public JWTAuthenticationFilter(JWTParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ofNullable(request.getHeader(AUTHORIZATION))
                .map(headerValue -> headerValue.substring("Token ".length()))
                .map(jwtParser::validateToken)
                .map(JWTAuthenticationToken::new)
                .ifPresent(authentication -> getContext().setAuthentication(authentication));
        filterChain.doFilter(request, response);
    }

    @SuppressWarnings("java:S2160")
    static class JWTAuthenticationToken extends AbstractAuthenticationToken {

        private final transient JWTPayload principal;

        JWTAuthenticationToken(JWTPayload jwtPayload) {
            super(null);
            this.principal = jwtPayload;
            this.setAuthenticated(true);
        }

        @Override
        public Object getPrincipal() {
            return principal;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

    }
}
