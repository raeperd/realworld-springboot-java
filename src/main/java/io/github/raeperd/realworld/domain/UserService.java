package io.github.raeperd.realworld.domain;

import io.github.raeperd.realworld.domain.jwt.JWTGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static io.github.raeperd.realworld.domain.AuthorizedUser.fromUser;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JWTGenerator jwtGenerator;

    public UserService(UserRepository userRepository, JWTGenerator jwtGenerator) {
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
    }

    @Transactional(readOnly = true)
    public Optional<AuthorizedUser> login(String email, String password) {
        return userRepository.findFirstByEmailAndPassword(email, password)
                .map(this::authorizeUser);
    }

    @Transactional
    public AuthorizedUser signUp(User user) {
        return authorizeUser(userRepository.save(user));
    }

    // TODO
    @Transactional(readOnly = true)
    public AuthorizedUser findUserById(long id) {
        return null;
    }

    private AuthorizedUser authorizeUser(User user) {
        return fromUser(user, jwtGenerator.generateTokenFromUser(user));
    }

}
