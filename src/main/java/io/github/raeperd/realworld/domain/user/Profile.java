package io.github.raeperd.realworld.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Profile {

    @Column
    private String username;

    @Column
    private String bio;

    @Column
    private String image;

    @Transient
    private boolean following;

    public static Profile fromUser(User user) {
        return of(user.getUsername(), user.getBio(), user.getImage());
    }

    static Profile of(String username, String bio, String image) {
        return new Profile(username, bio, image, false);
    }

    protected Profile() {
    }

    private Profile(String username, String bio, String image, boolean following) {
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.following = following;
    }

    public Profile withFollowing(boolean following) {
        this.following = following;
        return this;
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

    public boolean isFollowing() {
        return following;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
