package io.github.raeperd.realworld.domain.user;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class UserUpdateRequest {

    private final Email emailToUpdate;
    private final UserName userNameToUpdate;
    private final String passwordToUpdate;
    private final Image imageToUpdate;
    private final String bioToUpdate;

    public static UserUpdateRequestBuilder builder() {
        return new UserUpdateRequestBuilder();
    }

    Optional<Email> getEmailToUpdate() {
        return ofNullable(emailToUpdate);
    }

    Optional<UserName> getUserNameToUpdate() {
        return ofNullable(userNameToUpdate);
    }

    Optional<String> getPasswordToUpdate() {
        return ofNullable(passwordToUpdate);
    }

    Optional<Image> getImageToUpdate() {
        return ofNullable(imageToUpdate);
    }

    Optional<String> getBioToUpdate() {
        return ofNullable(bioToUpdate);
    }

    private UserUpdateRequest(UserUpdateRequestBuilder builder) {
        this.emailToUpdate = builder.emailToUpdate;
        this.userNameToUpdate = builder.userNameToUpdate;
        this.passwordToUpdate = builder.passwordToUpdate;
        this.imageToUpdate = builder.imageToUpdate;
        this.bioToUpdate = builder.bioToUpdate;
    }

    public static class UserUpdateRequestBuilder {
        private Email emailToUpdate;
        private UserName userNameToUpdate;
        private String passwordToUpdate;
        private Image imageToUpdate;
        private String bioToUpdate;

        public UserUpdateRequestBuilder emailToUpdate(Email emailToUpdate) {
            this.emailToUpdate = emailToUpdate;
            return this;
        }

        public UserUpdateRequestBuilder userNameToUpdate(UserName userNameToUpdate) {
            this.userNameToUpdate = userNameToUpdate;
            return this;
        }

        public UserUpdateRequestBuilder passwordToUpdate(String passwordToUpdate) {
            this.passwordToUpdate = passwordToUpdate;
            return this;
        }

        public UserUpdateRequestBuilder imageToUpdate(Image imageToUpdate) {
            this.imageToUpdate = imageToUpdate;
            return this;
        }

        public UserUpdateRequestBuilder bioToUpdate(String bioToUpdate) {
            this.bioToUpdate = bioToUpdate;
            return this;
        }

        public UserUpdateRequest build() {
            return new UserUpdateRequest(this);
        }
    }
}
