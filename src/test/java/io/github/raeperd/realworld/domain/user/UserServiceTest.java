package io.github.raeperd.realworld.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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

    @Test
    void when_findById_expect_repository_findById() {
        userService.findById(1L);

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void when_findByUsername_expect_repository_findFirstByProfileUserName(@Mock UserName userName) {
        userService.findByUsername(userName);

        verify(userRepository, times(1)).findFirstByProfileUserName(userName);
    }

    @Test
    void when_updateUser_with_invalid_id_expect_NoSuchElementException(@Mock UserUpdateRequest request) {
        when(userRepository.findById(1L)).thenReturn(empty());

        assertThatThrownBy(
                () -> userService.updateUser(1L, request)
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void when_updateUser_expect_userRepository_save(@Mock User user, @Mock UserUpdateRequest request) {
        given(userRepository.findById(1L)).willReturn(of(user));

        userService.updateUser(1L, request);

        then(userRepository).should(times(1)).save(user);
    }

    @Test
    void when_updateUser_email_expect_user_changEmail(@Mock User user, @Mock UserUpdateRequest request, @Mock Email email) {
        given(userRepository.findById(1L)).willReturn(of(user));
        given(request.getEmailToUpdate()).willReturn(of(email));

        userService.updateUser(1L, request);

        then(user).should(times(1)).changeEmail(email);
        verifyNoMoreInteractions(user);
    }

    @Test
    void when_updateUser_name_expect_user_changeName(@Mock User user, @Mock UserUpdateRequest request, @Mock UserName userName) {
        given(userRepository.findById(1L)).willReturn(of(user));
        given(request.getUserNameToUpdate()).willReturn(of(userName));

        userService.updateUser(1L, request);

        then(user).should(times(1)).changeName(userName);
        verifyNoMoreInteractions(user);
    }

    @Test
    void when_updateUser_password_expect_passwordEncoder_encode_password(@Mock User user, @Mock UserUpdateRequest request) {
        given(userRepository.findById(1L)).willReturn(of(user));
        given(request.getPasswordToUpdate()).willReturn(of("new-password"));

        userService.updateUser(1L, request);

        then(passwordEncoder).should(times(1)).encode("new-password");
    }

    @Test
    void when_updateUser_password_expect_user_changesPassword_encoded_password(@Mock User user,
                                                                               @Mock UserUpdateRequest request,
                                                                               @Mock Password password) {
        given(userRepository.findById(1L)).willReturn(of(user));
        given(request.getPasswordToUpdate()).willReturn(of("new-password"));


        try (var mockStatic = mockStatic(Password.class)) {
            mockStatic.when(() -> Password.of("new-password", passwordEncoder)).thenReturn(password);
            userService.updateUser(1L, request);
        }

        then(user).should(times(1)).changePassword(password);
        verifyNoMoreInteractions(user);
    }

    @Test
    void when_update_image_expect_user_changeImage(@Mock User user, @Mock UserUpdateRequest request, @Mock Image image) {
        given(userRepository.findById(1L)).willReturn(of(user));
        given(request.getImageToUpdate()).willReturn(of(image));

        userService.updateUser(1L, request);

        then(user).should(times(1)).changeImage(image);
        verifyNoMoreInteractions(user);
    }

    @Test
    void when_update_bio_expect_user_changes_bio(@Mock User user, @Mock UserUpdateRequest request) {
        given(userRepository.findById(1L)).willReturn(of(user));
        given(request.getBioToUpdate()).willReturn(of("new-bio"));

        userService.updateUser(1L, request);

        then(user).should(times(1)).changeBio("new-bio");
        verifyNoMoreInteractions(user);
    }

}