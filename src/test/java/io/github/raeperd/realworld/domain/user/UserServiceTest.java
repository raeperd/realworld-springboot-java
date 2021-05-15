package io.github.raeperd.realworld.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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
        given(request.getRawPassword()).willReturn("raw-password");

        userService.signUp(request);

        then(passwordEncoder).should(times(1)).encode("raw-password");
    }

    @Test
    void when_login_expect_user_matches_password(@Mock Email email, @Mock User user) {
        given(userRepository.findFirstByEmail(email)).willReturn(of(user));

        userService.login(email, "raw-password");

        then(user).should(times(1)).matchesPassword("raw-password", passwordEncoder);
    }
}