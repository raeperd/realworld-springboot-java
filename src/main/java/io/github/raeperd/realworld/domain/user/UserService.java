package io.github.raeperd.realworld.domain.user;

import io.github.raeperd.realworld.domain.jwt.JWTGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static io.github.raeperd.realworld.domain.user.AuthorizedUser.fromUser;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JWTGenerator jwtGenerator;
    private final UserContextHolder userContextHolder;

    public UserService(UserRepository userRepository, JWTGenerator jwtGenerator, UserContextHolder userContextHolder) {
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
        this.userContextHolder = userContextHolder;
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

    @Transactional(readOnly = true)
    public AuthorizedUser refreshUserAuthorization() {
        return userContextHolder.getCurrentUser()
                .map(this::authorizeUser)
                .orElseThrow(IllegalStateException::new);
    }

    @Transactional
    public AuthorizedUser updateUser(UserUpdateCommand updateCommand) {
        return userContextHolder.getCurrentUser()
                .map(user -> user.updateUser(updateCommand))
                .map(userRepository::save)
                .map(this::authorizeUser)
                .orElseThrow(IllegalStateException::new);
    }

    private AuthorizedUser authorizeUser(User user) {
        return fromUser(user, jwtGenerator.generateTokenFromUser(user));
    }

}
