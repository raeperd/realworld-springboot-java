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

    public static User databaseUser() {
        final var password = new Password();
        ReflectionTestUtils.setField(password, "encodedPassword", "$2y$10$Uw0vceuCbx3bVOsXZuP");
        final var databaseUser = User.of(
                new Email("databaseUser@email.com"),
                new UserName("databaseUser"),
                password);
        ReflectionTestUtils.setField(databaseUser, "id", 1L);
        return databaseUser;
    }

}
