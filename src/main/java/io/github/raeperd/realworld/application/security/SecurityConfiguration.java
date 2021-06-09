package io.github.raeperd.realworld.application.security;

import io.github.raeperd.realworld.domain.jwt.JWTDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers(POST, "/users", "/users/login").permitAll()
                .antMatchers(GET, "/profiles/**").permitAll()
                .antMatchers(GET, "/articles/**").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    JWTAuthenticationProvider jwtAuthenticationProvider(JWTDeserializer jwtDeserializer) {
        return new JWTAuthenticationProvider(jwtDeserializer);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
