package io.github.raeperd.realworld.domain.user;

import java.util.Optional;

import static java.util.Optional.ofNullable;

class UserUpdateRequest {

    private final Email emailToUpdate;
    private final UserName userNameToUpdate;
    private final String passwordToUpdate;
    private final Image imageToUpdate;
    private final String bioToUpdate;

    static UserUpdateRequestBuilder builder() {
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

    static class UserUpdateRequestBuilder {
        private Email emailToUpdate;
        private UserName userNameToUpdate;
        private String passwordToUpdate;
        private Image imageToUpdate;
        private String bioToUpdate;

        UserUpdateRequestBuilder emailToUpdate(Email emailToUpdate) {
            this.emailToUpdate = emailToUpdate;
            return this;
        }

        UserUpdateRequestBuilder userNameToUpdate(UserName userNameToUpdate) {
            this.userNameToUpdate = userNameToUpdate;
            return this;
        }

        UserUpdateRequestBuilder passwordToUpdate(String passwordToUpdate) {
            this.passwordToUpdate = passwordToUpdate;
            return this;
        }

        UserUpdateRequestBuilder imageToUpdate(Image imageToUpdate) {
            this.imageToUpdate = imageToUpdate;
            return this;
        }

        UserUpdateRequestBuilder bioToUpdate(String bioToUpdate) {
            this.bioToUpdate = bioToUpdate;
            return this;
        }

        UserUpdateRequest build() {
            return new UserUpdateRequest(this);
        }
    }
}
