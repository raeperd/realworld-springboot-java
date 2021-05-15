package io.github.raeperd.realworld.application.security;

import io.github.raeperd.realworld.infrastructure.jwt.UserJWTPayload;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static java.util.Collections.singleton;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WithMockJWTUserContextFactory implements WithSecurityContextFactory<WithMockJWTUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockJWTUser annotation) {
        final var payload = mock(UserJWTPayload.class);
        when(payload.getUserId()).thenReturn(annotation.userId());

        final var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new MockJWTAuthentication(payload));
        return context;
    }

    private static class MockJWTAuthentication extends AbstractAuthenticationToken {

        private final UserJWTPayload jwtPayload;

        private MockJWTAuthentication(UserJWTPayload jwtPayload) {
            super(singleton(new SimpleGrantedAuthority("USER")));
            super.setAuthenticated(true);
            this.jwtPayload = jwtPayload;
        }

        @Override
        public Object getPrincipal() {
            return jwtPayload;
        }

        @Override
        public Object getCredentials() {
            return "MOCKED CREDENTIAL";
        }
    }

}
