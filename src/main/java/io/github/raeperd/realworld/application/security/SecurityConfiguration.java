package io.github.raeperd.realworld.application.security;

import io.github.raeperd.realworld.domain.jwt.JWTParser;
import io.github.raeperd.realworld.domain.user.UserContextHolder;
import io.github.raeperd.realworld.domain.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JWTParser jwtParser;

    public SecurityConfiguration(JWTParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED)).and()
                .authorizeRequests()
                .antMatchers(POST, "/users", "/users/login").permitAll()
                .antMatchers(GET, "/articles").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(jwtParser);
    }

    @Bean
    public UserContextHolder userContextHolder(UserRepository userRepository) {
        return new JWTUserContextHolder(userRepository);
    }

}
