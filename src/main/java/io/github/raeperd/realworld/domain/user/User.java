package io.github.raeperd.realworld.domain.user;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class User {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @OneToMany
    private final Collection<User> followingUsers = new ArrayList<>();

    @Embedded
    private Email email;

    @Embedded
    private Profile profile;

    @Embedded
    private Password password;

    static User of(Email email, String username, Password password) {
        return new User(email, username, password);
    }

    private User(Email email, String username, Password password) {
        this.email = email;
        this.profile = Profile.of(username, null, null);
        this.password = password;
    }

    public User(Email email, String username, String encodedPassword) {
        this(email, username, null, null);
        // TODO
        this.password = null;
    }

    User(Email email, String username, String bio, String image) {
        this.id = null;
        this.email = email;
        this.profile = Profile.of(username, bio, image);
    }

    protected User() {
    }

    boolean matchPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return password.matches(rawPassword, passwordEncoder);
    }

    public void changePassword(String newRawPassword, PasswordEncoder passwordEncoder) {
        this.password = Password.of(newRawPassword, passwordEncoder);
    }

    User updateUser(UserUpdateCommand updateCommand) {
        updateCommand.getEmailToUpdate().ifPresent(emailToUpdate -> email = emailToUpdate);
        updateCommand.getUsernameToUpdate().ifPresent(usernameToUpdate -> profile.setUsername(usernameToUpdate));
        updateCommand.getBioToUpdate().ifPresent(bioToUpdate -> profile.setBio(bioToUpdate));
        updateCommand.getImageToUpdate().ifPresent(imageToUpdate -> profile.setImage(imageToUpdate));
        return this;
    }

    public User followUser(User user) {
        followingUsers.add(user);
        return this;
    }

    public User unfollowUser(User user) {
        followingUsers.removeIf(followed -> followed.getId().equals(user.getId()));
        return this;
    }

    public Profile viewProfile(User otherUser) {
        return otherUser.getProfile().withFollowing(followingUsers.contains(otherUser));
    }

    public Profile getProfile() {
        return profile;
    }

    public Collection<User> getFollowingUsers() {
        return followingUsers;
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getUsername() {
        return profile.getUsername();
    }

    public String getBio() {
        return profile.getBio();
    }

    public String getImage() {
        return profile.getImage();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
