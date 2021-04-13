package io.github.raeperd.realworld.application;

import io.github.raeperd.realworld.domain.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.of;

@RestController
public class UserRestController {

    private final UserRepository userRepository;

    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginRequestDTO loginRequest) {
        return of(userRepository.findFirstByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .map(UserResponseDTO::fromUser));
    }

}
