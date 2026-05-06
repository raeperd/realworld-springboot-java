package io.github.raeperd.realworld.application.security;

import io.github.raeperd.realworld.domain.jwt.JWTDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@EnableConfigurationProperties(SecurityConfigurationProperties.class)
@Configuration
public class SecurityConfiguration implements WebMvcConfigurer {

    private final SecurityConfigurationProperties properties;

    SecurityConfiguration(SecurityConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(POST, "/users", "/users/login");
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JWTDeserializer jwtDeserializer) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> {});
        http.formLogin(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);
        http.addFilterBefore(new JWTAuthenticationFilter(jwtDeserializer), UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(GET, "/profiles/*").permitAll()
                .requestMatchers(GET, "/articles/**").permitAll()
                .requestMatchers(GET, "/tags/**").permitAll()
                .anyRequest().authenticated());
        return http.build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "HEAD", "POST", "DELETE", "PUT")
                .allowedOrigins(properties.getAllowedOrigins().toArray(new String[0]))
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

@ConfigurationProperties("security")
class SecurityConfigurationProperties {
    private final List<String> allowedOrigins;

    SecurityConfigurationProperties(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }
}
