package io.github.raeperd.realworld.application.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@WithSecurityContext(factory = WithMockJWTUserContextFactory.class)
public @interface WithMockJWTUser {

    long userId() default 0L;

}
