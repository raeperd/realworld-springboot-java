package io.github.raeperd.realworld.domain.user;

import org.springframework.test.util.ReflectionTestUtils;

public class UserTestUtils {

    public static User userWithEmailAndName(String email, String name) {
        return User.of(
                new Email(email),
                new UserName(name),
                null
        );
    }

    public static User userWithIdAndEmail(long id, String email) {
        final var sampleUser = UserTestUtils.userWithEmailAndName(email, "name");
        ReflectionTestUtils.setField(sampleUser, "id", id);
        return sampleUser;
    }

}
