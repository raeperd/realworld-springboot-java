package io.github.raeperd.realworld.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Test
    void expect_user_has_protected_no_args_constructor() {
        class ChildUser extends User {
            public ChildUser() {
                super();
            }
        }
        final var childUser = new ChildUser();

        assertThat(childUser).isInstanceOf(User.class);
    }

    @MethodSource("provideUserUpdateCommandWithName")
    @ParameterizedTest
    void when_update_user_with_single_property_expect_return_user_updated(UserUpdateCommand command, String property) {
        final var user = new User(Email.of("some-email"), "some-username", "some-password");

        user.updateUser(command);

        assertThat(user).hasFieldOrPropertyWithValue(property, "updated-" + property);
    }

    private static Stream<Arguments> provideUserUpdateCommandWithName() {
        return Stream.of(
                Arguments.of(UserUpdateCommand.builder().username("updated-username").build(), "username"),
                Arguments.of(UserUpdateCommand.builder().bio("updated-bio").build(), "bio"),
                Arguments.of(UserUpdateCommand.builder().image("updated-image").build(), "image")
        );
    }

    @Test
    void when_follow_user_expect_following_profile() {
        final var user = new User(Email.of("some-email"), "some-username", "some-password");
        final var celebrity = new User(Email.of("other-email"), "celeb", "some-password");

        user.followUser(celebrity);

        assertThat(user.viewProfile(celebrity).isFollowing()).isTrue();
    }

    @Test
    void same_user_generate_same_hash_code() {
        final var user = new User(Email.of("some-email"), "some-username", "some-password");

        assertThat(user).hasSameHashCodeAs(user);
    }

    @Test
    void when_change_password_expect_encode_new_password(@Mock PasswordEncoder passwordEncoder) {
        final var newRawString = "newRawPassword";
        final var user = new User(Email.of("some-email"), "some-username", "some-password");

        user.changePassword("newRawPassword", passwordEncoder);

        then(passwordEncoder).should(times(1)).encode(newRawString);
    }

}