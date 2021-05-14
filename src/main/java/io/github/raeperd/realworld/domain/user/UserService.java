package io.github.raeperd.realworld.domain.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public User signUp(UserSignUpRequest request) {
        final var encodedPassword = Password.of(request.getRawPassword(), passwordEncoder);
        return userRepository.save(new User(request.getEmail(),
                request.getUserName(),
                encodedPassword));
    }

}
