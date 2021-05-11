package io.github.raeperd.realworld.domain.user;

public class UserSignInRequest {

    private final Email email;
    private final String username;
    private final String rawPassword;

    public static UserSignInRequest of(Email email, String username, String rawPassword) {
        return new UserSignInRequest(email, username, rawPassword);
    }

    private UserSignInRequest(Email email, String username, String rawPassword) {
        this.email = email;
        this.username = username;
        this.rawPassword = rawPassword;
    }

    Email getEmail() {
        return email;
    }

    String getUsername() {
        return username;
    }

    String getRawPassword() {
        return rawPassword;
    }
}
