package io.github.raeperd.realworld.domain.jwt;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@WithSecurityContext(factory = WIthMockJWTSecurityContextFactory.class)
public @interface WithMockJWT {

    long userId() default 1L;

}
