package io.github.raeperd.realworld.domain.user;

import io.github.raeperd.realworld.domain.jwt.JWTGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static io.github.raeperd.realworld.domain.user.AuthorizedUser.fromUser;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JWTGenerator jwtGenerator;
    private final UserContextHolder userContextHolder;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JWTGenerator jwtGenerator, UserContextHolder userContextHolder, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
        this.userContextHolder = userContextHolder;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<AuthorizedUser> login(Email email, String password) {
        return userRepository.findFirstByEmail(email)
                .filter(user -> user.matchPassword(password, passwordEncoder))
                .map(this::authorizeUser);
    }

    @Transactional
    public AuthorizedUser signIn(UserSignInRequest request) {
        return authorizeUser(userRepository.save(userFromSignInRequest(request)));
    }

    @Transactional(readOnly = true)
    public AuthorizedUser refreshUserAuthorization() {
        return userContextHolder.getCurrentUser()
                .map(this::authorizeUser)
                .orElseThrow(IllegalStateException::new);
    }

    // TODO: This can be improved using Profile class
    @Transactional
    public AuthorizedUser updateUser(UserUpdateCommand updateCommand) {
        final var currentUser = userContextHolder.getCurrentUser()
                .orElseThrow(IllegalStateException::new);
        updateCommand.getPasswordToUpdate()
                .ifPresent(passwordToUpdate -> currentUser.changePassword(passwordToUpdate, passwordEncoder));
        currentUser.updateUser(updateCommand);
        return authorizeUser(currentUser);
    }

    private User userFromSignInRequest(UserSignInRequest request) {
        return User.of(request.getEmail(),
                request.getUsername(),
                Password.of(request.getRawPassword(), passwordEncoder));
    }

    private AuthorizedUser authorizeUser(User user) {
        return fromUser(user, jwtGenerator.generateTokenFromUser(user));
    }

}
