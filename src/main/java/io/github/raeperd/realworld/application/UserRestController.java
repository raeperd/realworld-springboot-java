package io.github.raeperd.realworld.application;

import io.github.raeperd.realworld.domain.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static io.github.raeperd.realworld.application.UserModel.fromUser;

@RequestMapping("/users")
@RestController
class UserRestController {

    private final UserService userService;

    UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserModel postUser(@Valid @RequestBody UserPostRequestDTO dto) {
        final var userSaved = userService.signUp(dto.toSignUpRequest());
        return fromUser(userSaved);
    }
}
