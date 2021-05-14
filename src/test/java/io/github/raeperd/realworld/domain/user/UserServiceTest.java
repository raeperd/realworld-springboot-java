package io.github.raeperd.realworld.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void initializeUserService() {
        this.userService = new UserService(passwordEncoder, userRepository);
    }

    @Test
    void when_signUp_expect_password_encoded(@Mock UserSignUpRequest request) {
        when(request.getRawPassword()).thenReturn("rawPassword");

        userService.signUp(request);

        verify(passwordEncoder, times(1)).encode("rawPassword");
    }
}