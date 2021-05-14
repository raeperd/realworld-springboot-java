package io.github.raeperd.realworld.domain.user;

public class UserSignUpRequest {

    private final Email email;
    private final UserName userName;
    private final String rawPassword;

    public UserSignUpRequest(Email email, UserName userName, String rawPassword) {
        this.email = email;
        this.userName = userName;
        this.rawPassword = rawPassword;
    }

    public Email getEmail() {
        return email;
    }

    public UserName getUserName() {
        return userName;
    }

    public String getRawPassword() {
        return rawPassword;
    }

}
