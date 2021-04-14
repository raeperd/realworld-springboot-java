package io.github.raeperd.realworld.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void initializeService() {
        this.userService = new UserService(userRepository);
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
    void when_createUser_expect_userRepository_save_called(@Mock User user) {
        userService.createUser(user);

        then(userRepository).should(times(1)).save(user);
    }

}
