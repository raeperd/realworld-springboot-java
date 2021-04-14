package io.github.raeperd.realworld.domain;

import org.junit.jupiter.api.Test;

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
}