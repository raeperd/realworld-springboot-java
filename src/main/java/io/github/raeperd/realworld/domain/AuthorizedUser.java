package io.github.raeperd.realworld.domain;

public class AuthorizedUser extends User {

    private final String token;

    static AuthorizedUser fromUser(User user, String token) {
        return new AuthorizedUser(user.getEmail(), user.getUsername(), user.getBio(), user.getImage(), token);
    }

    private AuthorizedUser(String email, String username, String bio, String image, String token) {
        super(email, username, bio, image);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
