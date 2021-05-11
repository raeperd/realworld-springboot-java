package io.github.raeperd.realworld.domain.user;

import io.github.raeperd.realworld.domain.jwt.JWTGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private JWTGenerator jwtGenerator;
    @Mock
    private UserContextHolder userContextHolder;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void initializeService() {
        this.userService = new UserService(userRepository, jwtGenerator, userContextHolder, passwordEncoder);
    }

    @Test
    void when_signIn_expect_encode_password(@Mock User user) {
        final var request = UserSignInRequest.of(Email.of("email"), "username", "rawPassword");
        given(passwordEncoder.encode("rawPassword")).willReturn("encoded");
        given(userRepository.save(any(User.class))).willReturn(user);

        userService.signIn(request);

        then(passwordEncoder).should(times(1)).encode("rawPassword");
    }

    @Test
    void when_login_expect_userRepository_findFirstByEmailAndPassword_called(@Mock Email email) {
        final var password = "password";

        userService.login(email, password);

        then(userRepository).should(times(1))
                .findFirstByEmail(email);
    }

    @Test
    void when_login_expect_jwtService_to_generateToken(@Mock Email email, @Mock User user) {
        given(userRepository.findFirstByEmail(any(Email.class))).willReturn(of(user));
        given(user.matchPassword(anyString(), eq(passwordEncoder))).willReturn(true);

        userService.login(email, "password");

        then(jwtGenerator).should(times(1)).generateTokenFromUser(user);
    }

    @Test
    void when_getCurrentUser_return_empty_throw_IllegalStateException(@Mock UserUpdateCommand userUpdateCommand) {
        when(userContextHolder.getCurrentUser()).thenReturn(empty());

        assertThatThrownBy(() ->
                userService.updateUser(userUpdateCommand)
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void when_there_is_password_to_update_expect_user_changes_password(@Mock User user) {
        final var command = UserUpdateCommand.builder().password("passwordToUpdate").build();
        given(userContextHolder.getCurrentUser()).willReturn(of(user));

       userService.updateUser(command);

       then(user).should(times(1)).changePassword("passwordToUpdate", passwordEncoder);
    }

}
