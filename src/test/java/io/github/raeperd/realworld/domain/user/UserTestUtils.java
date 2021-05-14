package io.github.raeperd.realworld.domain.user;

public class UserTestUtils {

    public static User userWithEmailAndName(String email, String name) {
        return new User(
                new Email(email),
                new UserName(name),
                null
        );
    }
}
