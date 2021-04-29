package io.github.raeperd.realworld.domain.user;

import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserUpdateCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    void when_update_all_possible_field_expect_return_user_updated() {
        final var user = new User(null, null, null);
        final var updateCommand = new UserUpdateCommand.Builder()
                .email("updated-email")
                .username("updated-username")
                .bio("updated-bio")
                .image("updated-image")
                .password("updated-password")
                .build();

        user.updateUser(updateCommand);

        assertThat(user).hasNoNullFieldsOrPropertiesExcept("id", "followingUsers");
    }

    @MethodSource("provideUserUpdateCommandWithName")
    @ParameterizedTest
    void when_update_user_with_single_property_expect_return_user_updated(UserUpdateCommand command, String property) {
        final var user = new User("some-email", "some-username", "some-password");

        user.updateUser(command);

        assertThat(user).hasFieldOrPropertyWithValue(property, "updated-" + property);
    }

    private static Stream<Arguments> provideUserUpdateCommandWithName() {
        return Stream.of(
                Arguments.of(new UserUpdateCommand.Builder().email("updated-email").build(), "email"),
                Arguments.of(new UserUpdateCommand.Builder().username("updated-username").build(), "username"),
                Arguments.of(new UserUpdateCommand.Builder().bio("updated-bio").build(), "bio"),
                Arguments.of(new UserUpdateCommand.Builder().image("updated-image").build(), "image"),
                Arguments.of(new UserUpdateCommand.Builder().password("updated-password").build(), "password")
        );
    }

    @Test
    void when_follow_user_expect_following_profile() {
        final var user = new User("some-email", "some-username", "some-password");
        final var celebrity = new User("other-email", "celeb", "some-password");

        user.followUser(celebrity);

        assertThat(user.viewProfile(celebrity).isFollowing()).isTrue();
    }
}