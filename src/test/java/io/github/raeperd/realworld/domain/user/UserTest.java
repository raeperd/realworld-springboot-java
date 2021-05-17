package io.github.raeperd.realworld.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Email emailMock;
    @Mock
    private UserName userNameMock;
    @Mock
    private Password passwordMock;

    @Test
    void when_create_user_getImage_return_null() {
        final var user = User.of(emailMock, userNameMock, passwordMock);

        assertThat(user.getImage()).isNull();
    }

    @Test
    void when_create_user_getBio_return_null() {
        final var user = User.of(emailMock, userNameMock, passwordMock);

        assertThat(user.getBio()).isNull();
    }

    @Test
    void when_user_have_different_email_expect_not_equal_and_hashCode(
            @Mock Email otherEmail, @Mock UserName otherName, @Mock Password otherPassword) {
        final var user = User.of(emailMock, userNameMock, passwordMock);
        final var userWithSameEmail = User.of(otherEmail, otherName, otherPassword);

        assertThat(userWithSameEmail)
                .isNotEqualTo(user)
                .extracting(User::hashCode)
                .isNotEqualTo(user.hashCode());
    }

    @Test
    void when_user_have_same_email_expect_equal_and_hashCode(@Mock UserName otherName, @Mock Password otherPassword) {
        final var user = User.of(emailMock, userNameMock, passwordMock);
        final var userWithSameEmail = User.of(emailMock, otherName, otherPassword);

        assertThat(userWithSameEmail)
                .isEqualTo(user)
                .hasSameHashCodeAs(user);
    }

    @Test
    void when_view_profile_not_following_user_expect_following_false(@Mock Email otherEmail) {
        final var user = User.of(emailMock, userNameMock, passwordMock);
        final var otherUser = User.of(otherEmail, userNameMock, passwordMock);

        assertThat(user.viewProfile(otherUser))
                .hasFieldOrPropertyWithValue("following", false);
    }

    @Test
    void when_matches_password_expect_password_matches_password() {
        final var user = User.of(emailMock, userNameMock, passwordMock);

        user.matchesPassword("some-password", passwordEncoder);

        verify(passwordMock, times(1)).matchesPassword("some-password", passwordEncoder);
    }

    @Test
    void when_changeEmail_expect_getEmail_return_new_email(@Mock Email emailToChange) {
        final var user = User.of(emailMock, userNameMock, passwordMock);

        user.changeEmail(emailToChange);

        assertThat(user.getEmail()).isEqualTo(emailToChange);
    }

    @Test
    void when_changePassword_expect_matchesPassword_matches_new_password(@Mock Password passwordToChange) {
        final var user = User.of(emailMock, userNameMock, passwordMock);

        user.changePassword(passwordToChange);

        user.matchesPassword("some-password", passwordEncoder);
        verify(passwordToChange, times(1)).matchesPassword("some-password", passwordEncoder);
    }

    @Test
    void when_changeName_expect_getName_return_new_name(@Mock UserName userNameToChange) {
        final var user = User.of(emailMock, userNameMock, passwordMock);

        user.changeName(userNameToChange);

        assertThat(user.getName()).isEqualTo(userNameToChange);
    }

    @Test
    void when_changeBio_expect_getBio_return_new_bio() {
        final var user = User.of(emailMock, userNameMock, passwordMock);

        user.changeBio("new bio");

        assertThat(user.getBio()).isEqualTo("new bio");
    }

    @Test
    void when_changeImage_expect_getImage_return_new_image(@Mock Image imageToChange) {
        final var user = User.of(emailMock, userNameMock, passwordMock);

        user.changeImage(imageToChange);

        assertThat(user.getImage()).isEqualTo(imageToChange);
    }
}