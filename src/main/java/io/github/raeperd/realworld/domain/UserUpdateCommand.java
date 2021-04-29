package io.github.raeperd.realworld.domain;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class UserUpdateCommand {

    private final String email;
    private final String username;
    private final String password;
    private final String bio;
    private final String image;

    public static class Builder {

        private String email;
        private String username;
        private String password;
        private String bio;
        private String image;

        public Builder() {
            // no essential fields
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public UserUpdateCommand build() {
            return new UserUpdateCommand(this);
        }

    }

    private UserUpdateCommand(Builder builder) {
        this.email = builder.email;
        this.username = builder.username;
        this.password = builder.password;
        this.bio = builder.bio;
        this.image = builder.image;
    }

    public Optional<String> getEmailToUpdate() {
        return ofNullable(email);
    }

    public Optional<String> getUsernameToUpdate() {
        return ofNullable(username);
    }

    public Optional<String> getPasswordToUpdate() {
        return ofNullable(password);
    }

    public Optional<String> getBioToUpdate() {
        return ofNullable(bio);
    }

    public Optional<String> getImageToUpdate() {
        return ofNullable(image);
    }
}
