package io.github.raeperd.realworld.domain;

import io.github.raeperd.realworld.domain.jwt.JWTGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
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

    @BeforeEach
    void initializeService() {
        this.userService = new UserService(userRepository, jwtGenerator, userContextHolder);
    }

    @Test
    void when_signUp_expect_userRepository_save_called(@Mock User user) {
        given(userRepository.save(user)).willReturn(user);

        userService.signUp(user);

        then(userRepository).should(times(1)).save(user);
    }

    @Test
    void when_signUp_expect_jwtService_to_generateToken(@Mock User user) {
        final var mockedToken = "MOCKED_TOKEN";
        when(userRepository.save(user)).thenReturn(user);
        when(jwtGenerator.generateTokenFromUser(user)).thenReturn(mockedToken);

        assertThat(userService.signUp(user))
                .extracting(AuthorizedUser::getToken)
                .isEqualTo(mockedToken);
    }

    @Test
    void when_login_expect_userRepository_findFirstByEmailAndPassword_called() {
        final var email = "email";
        final var password = "password";

        userService.login(email, password);

        then(userRepository).should(times(1))
                .findFirstByEmailAndPassword(email, password);
    }

    @Test
    void when_login_expect_jwtService_to_generateToken(@Mock User user) {
        given(userRepository.findFirstByEmailAndPassword(anyString(), anyString())).willReturn(of(user));

        userService.login("email", "password");

        then(jwtGenerator).should(times(1)).generateTokenFromUser(user);
    }

    @Test
    void when_getCurrentUser_return_empty_throw_IllegalStateException(@Mock UserUpdateCommand userUpdateCommand) {
        when(userContextHolder.getCurrentUser()).thenReturn(empty());

        assertThatThrownBy(() ->
                userService.updateUser(userUpdateCommand)
        ).isInstanceOf(IllegalStateException.class);
    }

}
