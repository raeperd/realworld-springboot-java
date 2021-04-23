package io.github.raeperd.realworld.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private long id;

    private String email;
    private String username;
    private String bio;
    private String image;
    private String password;

    public User(String email, String username, String password) {
        this(email, username, null, null);
        this.password = password;
    }

    protected User(String email, String username, String bio, String image) {
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.image = image;
    }

    protected User() {
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
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
}
