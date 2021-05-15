package io.github.raeperd.realworld.application;

import io.github.raeperd.realworld.domain.jwt.JWTSerializer;
import io.github.raeperd.realworld.domain.user.UserService;
import io.github.raeperd.realworld.infrastructure.jwt.UserJWTPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static io.github.raeperd.realworld.application.UserModel.fromUserAndToken;
import static org.springframework.http.ResponseEntity.of;

@RestController
class UserRestController {

    private final UserService userService;
    private final JWTSerializer jwtSerializer;

    UserRestController(UserService userService, JWTSerializer jwtSerializer) {
        this.userService = userService;
        this.jwtSerializer = jwtSerializer;
    }

    @PostMapping("/users")
    public UserModel postUser(@Valid @RequestBody UserPostRequestDTO dto) {
        final var userSaved = userService.signUp(dto.toSignUpRequest());
        return fromUserAndToken(userSaved, jwtSerializer.jwtFromUser(userSaved));
    }

    @GetMapping("/user")
    public ResponseEntity<UserModel> getUser(@AuthenticationPrincipal UserJWTPayload jwtPayload) {
        return of(userService.getUserById(jwtPayload.getUserId())
                .map(user -> UserModel.fromUserAndToken(user, getCurrentCredential())));
    }

    private static String getCurrentCredential() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getCredentials()
                .toString();
    }
}
