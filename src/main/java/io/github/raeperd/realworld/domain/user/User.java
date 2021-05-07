package io.github.raeperd.realworld.domain.user;

import io.github.raeperd.realworld.domain.user.profile.Profile;

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

    private String username;
    private String bio;
    private String image;
    private String password;

    public User(Email email, String username, String password) {
        this(email, username, null, null);
        this.password = password;
    }

    User(Email email, String username, String bio, String image) {
        this.id = null;
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.image = image;
    }

    protected User() {
    }

    User updateUser(UserUpdateCommand updateCommand) {
        updateCommand.getEmailToUpdate().ifPresent(emailToUpdate -> this.email = emailToUpdate);
        updateCommand.getUsernameToUpdate().ifPresent(usernameToUpdate -> this.username = usernameToUpdate);
        updateCommand.getBioToUpdate().ifPresent(bioToUpdate -> this.bio = bioToUpdate);
        updateCommand.getImageToUpdate().ifPresent(imageToUpdate -> this.image = imageToUpdate);
        updateCommand.getPasswordToUpdate().ifPresent(passwordToUpdate -> this.password = passwordToUpdate);
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
        return new Profile(otherUser.getUsername(), otherUser.getBio(), otherUser.getImage(),
                followingUsers.contains(otherUser));
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
        return username;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
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
