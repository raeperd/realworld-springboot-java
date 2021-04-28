package io.github.raeperd.realworld.domain.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JWTConfiguration {

    private static final String SECRET = "IO_GITHUB_RAEPERD_SOME_SECRET";
    private static final long DURATION_SECONDS = 800;

    private final HS256JWTService jwtService;

    JWTConfiguration() {
        this.jwtService = new HS256JWTService(SECRET, DURATION_SECONDS);
    }

    @Bean
    JWTGenerator jwtGenerator() {
        return jwtService;
    }

    @Bean
    JWTParser jwtParser() {
        return jwtService;
    }

}
