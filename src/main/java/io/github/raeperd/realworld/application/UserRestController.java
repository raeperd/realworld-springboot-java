package io.github.raeperd.realworld.application;

import io.github.raeperd.realworld.domain.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.of;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(CREATED)
    @PostMapping("/users")
    public UserResponseDTO postUser(@RequestBody UserPostRequestDTO postRequest) {
        return UserResponseDTO.fromAuthorizedUser(
                userService.signUp(postRequest.toUser()));
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginRequestDTO loginRequest) {
        return of(userService.login(loginRequest.getEmail(), loginRequest.getPassword())
                .map(UserResponseDTO::fromAuthorizedUser));
    }

    @GetMapping("/user")
    public UserResponseDTO getUser() {
        return UserResponseDTO.fromAuthorizedUser(
                userService.findUserById(getCurrentUserId())
        );
    }

    private long getCurrentUserId() {
        return (long) ofNullable(getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .orElseThrow(IllegalStateException::new);
    }

}
