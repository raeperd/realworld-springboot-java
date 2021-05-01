package io.github.raeperd.realworld.infrastructure;

import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class JPAConfiguration {

    @Bean
    public AuditorAware<User> articleAuthorProvider(UserContextHolder userContextHolder) {
        return userContextHolder::getCurrentUser;
    }
}
